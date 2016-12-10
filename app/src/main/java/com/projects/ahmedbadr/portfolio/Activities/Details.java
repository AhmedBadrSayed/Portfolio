package com.projects.ahmedbadr.portfolio.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.projects.ahmedbadr.portfolio.DataStore.MoviesDB;
import com.projects.ahmedbadr.portfolio.Fragments.MovieDetailsFragment;
import com.projects.ahmedbadr.portfolio.Fragments.ReviewsFragment;
import com.projects.ahmedbadr.portfolio.Fragments.TrailersFragment;
import com.projects.ahmedbadr.portfolio.R;
import com.projects.ahmedbadr.portfolio.Service.APIModel;
import com.projects.ahmedbadr.portfolio.Service.ServiceBuilder;
import com.projects.ahmedbadr.portfolio.Service.ServiceInterfaces;
import com.projects.ahmedbadr.portfolio.Utilities.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Details extends AppCompatActivity {

    @BindView(R.id.iv_movie_background)
    ImageView Poster;
    private String MovieDetails="", PosterPath = "", MovieTitle = "", Overview = "", VoteAverage = "", Date = "", ID = "";
    private String[] DetailsArray;
    private MoviesDB moviesDB;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = this.getSupportActionBar();

        ButterKnife.bind(this);
        moviesDB = new MoviesDB(this);

        Intent intent = this.getIntent();
        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            MovieDetails = intent.getStringExtra(Intent.EXTRA_TEXT);
            viewAdapter(MovieDetails);
            actionBar.setTitle(MovieTitle);
            System.out.println("tittle "+MovieTitle);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFrag(MovieDetailsFragment.newInstance(MovieDetails),"Details");
        mSectionsPagerAdapter.addFrag(TrailersFragment.newInstance(ID),"Trailers");
        mSectionsPagerAdapter.addFrag(ReviewsFragment.newInstance(ID),"Reviews");

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moviesDB.isInDataBase(MovieTitle)==true){
                    moviesDB.deleteMovie(MovieTitle);
                    Snackbar.make(view, "Removed from favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if(moviesDB.isInDataBase(MovieTitle)==false){
                    moviesDB.addMovie(ID, MovieTitle, Overview, VoteAverage, Date, PosterPath);
                    Snackbar.make(view, "Added to favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

    }

    public void viewAdapter(String movieDetails) {
        DetailsArray = movieDetails.split("=");
        ID = DetailsArray[0];
        MovieTitle = DetailsArray[1];
        Overview = DetailsArray[2];
        VoteAverage = DetailsArray[3];
        Date = DetailsArray[4];
        PosterPath = DetailsArray[5];
        Picasso.with(this).load(PosterPath).into(Poster);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }
    }

}
