package com.example.android.movies.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.movies.Movie;
import com.example.android.movies.MovieAdapter;
import com.example.android.movies.R;
import com.example.android.movies.activity.DetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/* The class driving the fragment content of list page */
public class MovieFragment extends Fragment {

    /*
       This class extends AsyncTask and performs the calls to TheMoviedb.
     */
    private class FetchMovieTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        /**
         * Take the String representing the list of movies in JSON Format and
         * pull out the data.
         * Creates a movie object and invokes the toString() method of the object.
         */
        private String[] getMovieFromJson(String moviesJsonStr)
                throws JSONException {

            ArrayList<String> movies = new ArrayList<String>();

            // These are the names of the JSON objects that need to be extracted.
            final String MOVIE_LIST = "results";
            final String MOVIE_ID = "id";
            final String MOVIE_TITLE = "title";
            final String MOVIE_POSTER_LOC = "poster_path";
            final String MOVIE_SYNOPSIS = "overview";
            final String MOVIE_DATE = "release_date";
            final String MOVIE_RATING = "vote_average";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = moviesJson.getJSONArray(MOVIE_LIST);

            for (int i = 0; i < movieArray.length(); i++) {
                // Get the JSON object representing the movie
                JSONObject eachMovie = movieArray.getJSONObject(i);
                String id = eachMovie.getString(MOVIE_ID);
                String title = eachMovie.getString(MOVIE_TITLE);
                String movie_poster_loc = "http://image.tmdb.org/t/p/w185/" + eachMovie.getString(MOVIE_POSTER_LOC);
                String movie_plot = eachMovie.getString(MOVIE_SYNOPSIS);
                String release_date = eachMovie.getString(MOVIE_DATE);
                String movie_rating = eachMovie.getString(MOVIE_RATING);
                Movie movie = new Movie(id, title, movie_poster_loc, movie_plot, movie_rating, release_date);
                movies.add(movie.toString());
            }
            return movies.toArray(new String[0]);

        }

        @Override
        protected String[] doInBackground(String... params) {

            Log.v(LOG_TAG, "AsyncTask -> doInBackground() -> Start");

            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpsURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            try {
                // Construct the URL for the MovieDB query query
                final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/" + params[1] + "?";
                final String APIKEY_PARAM = "api_key";

                Log.v(LOG_TAG, "Building the URL");
                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon().appendQueryParameter(APIKEY_PARAM, params[0]).build();
                URL url = new URL(builtUri.toString());

                // Create the request to MovieDB, and open the connection
                Log.v(LOG_TAG, "Connecting to the URL: " + url.toString());
                if (url != null) {
                    urlConnection = (HttpsURLConnection) url.openConnection();
                }
                if (urlConnection != null) {
                    urlConnection.setRequestMethod("GET");
                    //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0");
                    //urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    urlConnection.connect();
                    Log.v(LOG_TAG, "Connection established");
                    int status = urlConnection.getResponseCode();
                    if(status != 200){
                        Log.e(LOG_TAG, "Error while establishing connection to " + url.toString());
                        Log.e(LOG_TAG, "Status: " + status);
                        return  null;
                    }

                    // Read the input stream into a String
                    try {
                        InputStream inputStream = urlConnection.getInputStream();
                        if (inputStream == null) {
                            Log.v(LOG_TAG, "Input Stream returned null");
                            return null;
                        }
                        reader = new BufferedReader(new InputStreamReader(inputStream));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                Log.v(LOG_TAG, "Reading the JSON");
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    Log.e(LOG_TAG, "Empty JSON String");
                    return null;
                }
                moviesJsonStr = buffer.toString();

                Log.d(LOG_TAG, "The JSON Fetched: " + moviesJsonStr);

                try {

                    return getMovieFromJson(moviesJsonStr);

                } catch (JSONException jse) {
                    Log.e(LOG_TAG, jse.getMessage(), jse);
                    jse.printStackTrace();
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] moviesString) {
            ArrayList<Movie> movies = new ArrayList<Movie>();
            if (moviesString != null) {
                for (String movieStr : moviesString) {
                    movies.add(new Movie(movieStr));
                }
                Log.d(LOG_TAG, "Setting the movie details to Adapter");
                mMovieAdapter.setmMovies(movies.toArray(new Movie[0]));
            }
        }
    }

    MovieAdapter mMovieAdapter;
    private final String LOG_TAG = MovieFragment.class.getSimpleName();

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "OnCreateView()");

        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        mMovieAdapter = new MovieAdapter(getActivity());
        gridview.setAdapter(mMovieAdapter);

        //Go to Detail Activity on clicking a grid item
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String detail = mMovieAdapter.getItem(position).toString();
                Intent gotoDetailIntent = new Intent(getActivity(), DetailActivity.class);
                gotoDetailIntent.putExtra(Intent.EXTRA_TEXT, detail);
                startActivity(gotoDetailIntent);
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    /* This method counts the number of columns that must be shown in grid based on device size and orientation*/
    private void refreshGridView() {

        Log.v(LOG_TAG, "Setting the number of columns dynamically");

        int gridViewEntrySize = getResources().getDimensionPixelSize(R.dimen.grip_view_entry_size);
        int gridViewSpacing = getResources().getDimensionPixelSize(R.dimen.grip_view_spacing);

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int numColumns = (display.getWidth() - gridViewSpacing) / (gridViewEntrySize + gridViewSpacing);
        Log.v(LOG_TAG, "Number of Columns: " + numColumns);

        GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);
        gridView.setNumColumns(numColumns);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.v(LOG_TAG, "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
        // Change the number of column on orientation change
        refreshGridView();
    }

    @Override
    public void onResume() {
        Log.v(LOG_TAG, "onResume()");
        super.onResume();
        refreshGridView();
    }

    @Override
    public void onStart() {
        Log.v(LOG_TAG, "onStart()");
        super.onStart();
        updateMovies();
        refreshGridView();
    }

    private void updateMovies() {
        Log.v(LOG_TAG, "updateMovies()");
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute(getString(R.string.api_key), getString(R.string.pref_popular));
    }
}
