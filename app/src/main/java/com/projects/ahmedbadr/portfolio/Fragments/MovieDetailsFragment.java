package com.projects.ahmedbadr.portfolio.Fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.ahmedbadr.portfolio.Adapters.ExpandedListAdapter;
import com.projects.ahmedbadr.portfolio.DataObjects.MovieObject;
import com.projects.ahmedbadr.portfolio.DataStore.MoviesDB;
import com.projects.ahmedbadr.portfolio.R;
import com.projects.ahmedbadr.portfolio.Service.APIModel;
import com.projects.ahmedbadr.portfolio.Service.ServiceBuilder;
import com.projects.ahmedbadr.portfolio.Service.ServiceInterfaces;
import com.projects.ahmedbadr.portfolio.Utilities.Constants;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    @BindView(R.id.movie_description)
    TextView overview;
    @BindView(R.id.movie_vote)
    TextView vote;
    @BindView(R.id.movie_date)
    TextView date;
    ExpandableListView expandableListView;
    private String MovieDetails="", PosterPath = "", MovieTitle = "", Overview = "", VoteAverage = "", Date = "", ID = "";
    private String[] DetailsArray;
    private ExpandedListAdapter expandedListAdapter;
    private List<String> DataHeader;
    private HashMap<String, List<String>> DataChild;
    private ArrayList<String> TrailersArray, ReviewsArray, TrailerI, Authors;
    private static final String ARG_PARAM = null;
    private static final String ARG_PARAM_TABS = null;
    private MoviesDB moviesDB;
    private static Bundle args,bundle;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    public static MovieDetailsFragment newInstance(String movieDetails) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        args = new Bundle();
        args.putString(ARG_PARAM_TABS, movieDetails);
        fragment.setArguments(args);
        return fragment;
    }

    public static MovieDetailsFragment getInstace(String movieDetails){
        bundle = new Bundle();
        bundle.putString(ARG_PARAM, movieDetails);
        MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this,rootview);
        moviesDB = new MoviesDB(getActivity());
        TrailersArray = new ArrayList<>();
        ReviewsArray = new ArrayList<>();
        DataHeader = new ArrayList<>();
        DataChild = new HashMap<>();
        TrailerI = new ArrayList<>();
        Authors = new ArrayList<>();
        Intent intent = getActivity().getIntent();
        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            MovieDetails = intent.getStringExtra(Intent.EXTRA_TEXT);
            viewAdapter(MovieDetails);
        }
        if(getArguments() == bundle){
            viewAdapter(getArguments().getString(ARG_PARAM));
        }else viewAdapter(getArguments().getString(ARG_PARAM_TABS));

        //check for tablet layout
        if (rootview.findViewById(R.id.fav_tablet)!= null && rootview.findViewById(R.id.poster_tablet)!=null){
            PerformTrailersCall(ID);
            PerformReviewsCall(ID);
            final ImageButton favorite = (ImageButton) rootview.findViewById(R.id.fav_tablet);
            ImageView poster = (ImageView) rootview.findViewById(R.id.poster_tablet);
            TextView tittle = (TextView) rootview.findViewById(R.id.tittle);
            if(moviesDB.isInDataBase(MovieTitle)==true){
                favorite.setImageResource(R.drawable.fav);
            }else favorite.setImageResource(R.drawable.off);
            expandableListView = (ExpandableListView) rootview.findViewById(R.id.trailers_reviews);
            DataHeader.add("Trailers");
            DataHeader.add("Reviews");
            expandedListAdapter = new ExpandedListAdapter(getActivity(), DataHeader, DataChild);

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    if (DataHeader.get(groupPosition).equals("Trailers")) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(TrailersArray.get(childPosition)));
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(TrailersArray.get(childPosition)));
                            startActivity(intent);
                        }
                    } else {
                        new LovelyInfoDialog(getActivity())
                                .setTopColorRes(R.color.Portfolio_dark_orange)
                                .setIcon(R.drawable.review)
                                .setTitle("By "+Authors.get(childPosition))
                                .setMessage(ReviewsArray.get(childPosition))
                                .show();
                    }
                    return false;
                }
            });

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(moviesDB.isInDataBase(MovieTitle)==true){
                        moviesDB.deleteMovie(MovieTitle);
                        favorite.setImageResource(R.drawable.off);
                        Snackbar.make(view, "Removed from favorites", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else if(moviesDB.isInDataBase(MovieTitle)==false){
                        moviesDB.addMovie(ID, MovieTitle, Overview, VoteAverage, Date, PosterPath);
                        favorite.setImageResource(R.drawable.fav);
                        Snackbar.make(view, "Added to favorites", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });

            tittle.setText(MovieTitle);
            Picasso.with(getActivity()).load(PosterPath).into(poster);
            poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        return rootview;
    }

    void PerformTrailersCall(String movieID){
        ServiceBuilder builder = new ServiceBuilder();
        ServiceInterfaces.Trailers trailers = builder.BuildTrailers();
        Call<APIModel> apiModelCall = trailers.getTrailers(movieID, Constants.API_KEY);
        apiModelCall.enqueue(new Callback<APIModel>() {
            @Override
            public void onResponse(Call<APIModel> call, Response<APIModel> response) {
                for (int i=0 ; i<response.body().MoviesList.size() ; i++){
                    TrailersArray.add(i, Constants.YouTube_URL + response.body().MoviesList.get(i).key);
                }

                TrailerI = new ArrayList<>(TrailersArray.size());
                for(int i=0 ; i<TrailersArray.size() ; i++){
                    TrailerI.add(i, "Trailer " + (i + 1));
                }
                DataChild.put(DataHeader.get(0), TrailerI);
                expandableListView.setAdapter(expandedListAdapter);
            }

            @Override
            public void onFailure(Call<APIModel> call, Throwable t) {
                Log.v("Retrieve Error", t.toString());
            }
        });
    }

    void PerformReviewsCall(String movieID){
        ServiceBuilder builder = new ServiceBuilder();
        ServiceInterfaces.Reviews reviews = builder.BuildReviews();
        Call<APIModel> apiModelCall = reviews.getReviews(movieID, Constants.API_KEY);
        apiModelCall.enqueue(new Callback<APIModel>() {
            @Override
            public void onResponse(Call<APIModel> call, Response<APIModel> response) {
                for (int i=0 ; i<response.body().MoviesList.size() ; i++){
                    ReviewsArray.add(i, response.body().MoviesList.get(i).content);
                    Authors.add(i, response.body().MoviesList.get(i).author);
                }

                DataChild.put(DataHeader.get(1), Authors);
                expandableListView.setAdapter(expandedListAdapter);
            }

            @Override
            public void onFailure(Call<APIModel> call, Throwable t) {
                Log.v("Retrieve Error", t.toString());
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
        overview.setText(Overview);
        vote.setText(VoteAverage+"/10");
        date.setText(Date);
    }
}
