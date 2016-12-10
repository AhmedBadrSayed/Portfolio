package com.projects.ahmedbadr.portfolio.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.ahmedbadr.portfolio.Adapters.ReviewsRecyclerAdapter;
import com.projects.ahmedbadr.portfolio.R;
import com.projects.ahmedbadr.portfolio.Service.APIModel;
import com.projects.ahmedbadr.portfolio.Service.ServiceBuilder;
import com.projects.ahmedbadr.portfolio.Service.ServiceInterfaces;
import com.projects.ahmedbadr.portfolio.Utilities.Constants;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ahmed Badr on 7/12/2016.
 */
public class ReviewsFragment extends Fragment {

    private static final String ARG_PARAM_TABS = null;
    private ArrayList<String>  ReviewsArray, Authors;
    private RecyclerView mRecyclerView;
    private ReviewsRecyclerAdapter myRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Bundle args;

    public ReviewsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ReviewsFragment newInstance(String ID) {
        ReviewsFragment fragment = new ReviewsFragment();
        args = new Bundle();
        args.putString(ARG_PARAM_TABS,ID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        ReviewsArray = new ArrayList<>();
        Authors = new ArrayList<>();
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.reviews_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (getArguments() != null){
            PerformReviewsCall(getArguments().getString(ARG_PARAM_TABS));
        }

        myRecyclerViewAdapter = new ReviewsRecyclerAdapter(Authors);
        myRecyclerViewAdapter.setOnItemClickListener(new ReviewsRecyclerAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                new LovelyInfoDialog(getContext())
                        .setTopColorRes(R.color.Portfolio_dark_orange)
                        .setIcon(R.drawable.review)
                        .setTitle("By "+ Authors.get(position))
                        .setMessage(ReviewsArray.get(position))
                        .show();
            }
        });

        return rootView;
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

                myRecyclerViewAdapter = new ReviewsRecyclerAdapter(Authors);
                mRecyclerView.setAdapter(myRecyclerViewAdapter);
                myRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<APIModel> call, Throwable t) {
                Log.v("Retrieve Error", t.toString());
            }
        });
    }
}
