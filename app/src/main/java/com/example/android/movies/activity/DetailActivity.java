package com.example.android.movies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movies.Movie;
import com.example.android.movies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by aesebast on 9/19/2016.
 */
public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviedetail);

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            Movie currentMovie = new Movie(intent.getStringExtra(Intent.EXTRA_TEXT));
            ((TextView)findViewById(R.id.title)).setText(currentMovie.getMovieTitle());
            ((TextView)findViewById(R.id.user_rating)).setText(currentMovie.getUserRating());
            ImageView imageView = (ImageView)findViewById(R.id.poster);
            Picasso.with(getApplicationContext()).load(currentMovie.getMoviePoster()).into(imageView);
            ((TextView)findViewById(R.id.synopsis)).setText(currentMovie.getPlotSynopsis());
            ((TextView)findViewById(R.id.length)).setText(currentMovie.getLength());
            ((TextView)findViewById(R.id.year)).setText(currentMovie.getReleaseYear());
        }

    }
}
