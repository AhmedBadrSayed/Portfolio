package com.projects.ahmedbadr.portfolio.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.ahmedbadr.portfolio.Adapters.TrailersRecyclerAdapter;
import com.projects.ahmedbadr.portfolio.R;
import com.projects.ahmedbadr.portfolio.Service.APIModel;
import com.projects.ahmedbadr.portfolio.Service.ServiceBuilder;
import com.projects.ahmedbadr.portfolio.Service.ServiceInterfaces;
import com.projects.ahmedbadr.portfolio.Utilities.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ahmed Badr on 7/12/2016.
 */
public class TrailersFragment extends Fragment {

    private static final String ARG_PARAM_TABS = null;
    private RecyclerView mRecyclerView;
    private TrailersRecyclerAdapter myRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> TrailerI,TrailersArray;
    private static Bundle args;

    public TrailersFragment() {
    }

    public static TrailersFragment newInstance(String ID) {
        TrailersFragment fragment = new TrailersFragment();
        args = new Bundle();
        args.putString(ARG_PARAM_TABS, ID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trailers, container, false);

        TrailersArray = new ArrayList<>();
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.trailers_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (getArguments() != null){
            PerformTrailersCall(getArguments().getString(ARG_PARAM_TABS));
        }

        myRecyclerViewAdapter = new TrailersRecyclerAdapter(TrailerI);
        myRecyclerViewAdapter.setOnItemClickListener(new TrailersRecyclerAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(TrailersArray.get(position)));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(TrailersArray.get(position)));
                    startActivity(intent);
                }
            }
        });

        return rootView;
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

                myRecyclerViewAdapter = new TrailersRecyclerAdapter(TrailerI);
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
