package com.example.android.movies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by aesebast on 9/15/2016.
 */
public class MovieAdapter extends BaseAdapter {

    private Context mContext;
    private Movie[] mMovies;
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context c) {
        mContext = c;
    }

    public void setmMovies(Movie[] mMovies) {
        this.mMovies = mMovies;
    }

    public int getCount() {
        if (mMovies != null) {
            return mMovies.length;
        }
        else
            return 0;
    }

    public Object getItem(int position) {
        return mMovies[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(LOG_TAG,"MovieAdapter -> getView()");
        ImageView imageView;
        //if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = (ImageView)convertView.findViewById(R.id.grid_item_icon);
            //imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //} else {
          //  imageView = (ImageView) convertView;
        //}
        Picasso.with(mContext).load(mMovies[position].getMoviePoster()).into(imageView);
        return imageView;
    }


}
