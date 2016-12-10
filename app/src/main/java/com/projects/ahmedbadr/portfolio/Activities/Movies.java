package com.projects.ahmedbadr.portfolio.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.projects.ahmedbadr.portfolio.Fragments.MovieDetailsFragment;
import com.projects.ahmedbadr.portfolio.Fragments.MoviesFragment;
import com.projects.ahmedbadr.portfolio.R;

public class Movies extends AppCompatActivity implements MoviesFragment.moviesFragmentCallbacks {

    boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        if (findViewById(R.id.movie_details_container)!= null){
            mTwoPane = true;
        }else mTwoPane = false;

    }

    @Override
    public void posterClick(String movieDetails) {

        if(mTwoPane){
            MovieDetailsFragment detailsFragment = MovieDetailsFragment.getInstace(movieDetails);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.movie_details_container, detailsFragment)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, Details.class);
            intent.putExtra(Intent.EXTRA_TEXT, movieDetails);
            startActivity(intent);
        }

    }
}
