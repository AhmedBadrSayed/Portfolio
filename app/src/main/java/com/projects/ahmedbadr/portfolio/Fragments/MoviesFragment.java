package com.projects.ahmedbadr.portfolio.Fragments;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.ahmedbadr.portfolio.Activities.MovieDetails;
import com.projects.ahmedbadr.portfolio.Adapters.PostersRecyclerViewAdapter;
import com.projects.ahmedbadr.portfolio.DataObjects.MovieObject;
import com.projects.ahmedbadr.portfolio.DataStore.MoviesDB;
import com.projects.ahmedbadr.portfolio.R;
import com.projects.ahmedbadr.portfolio.Service.APIModel;
import com.projects.ahmedbadr.portfolio.Service.ServiceBuilder;
import com.projects.ahmedbadr.portfolio.Service.ServiceInterfaces;
import com.projects.ahmedbadr.portfolio.Utilities.Constants;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements OnMenuItemClickListener {

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.posters_recycler_view)
    RecyclerView RecyclerView;
    private PostersRecyclerViewAdapter recyclerViewAdapter;
    private android.support.v7.widget.RecyclerView.LayoutManager gridLayoutManager;
    private List<MovieObject> movieObjectList, favoriteMovies;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    private List<APIModel> AllMovies;
    private ArrayList<String> MoviesDetailsArray;
    private MoviesDB moviesDB;
    private static String lastChoice;
    private String mostPopular, topRated;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView =  inflater.inflate(R.layout.fragment_movies, container, false);

        ButterKnife.bind(this,RootView);
        initMenuFragment();
        moviesDB = new MoviesDB(getActivity());
        fragmentManager = getActivity().getSupportFragmentManager();
        MoviesDetailsArray = new ArrayList<>();
        favoriteMovies = new ArrayList<>();
        movieObjectList = new ArrayList<>();
        AllMovies = new ArrayList<>();
        mostPopular = getString(R.string.pref_sort_value_mostpopular);
        topRated = getString(R.string.pref_sort_value_toprated);

        RecyclerView.setHasFixedSize(true);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager = new GridLayoutManager(getContext(),2);
            RecyclerView.setLayoutManager(gridLayoutManager);
        }
        else{
            gridLayoutManager = new GridLayoutManager(getContext(),4);
            RecyclerView.setLayoutManager(gridLayoutManager);
        }

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setEnabled( true );
                swipeContainer.setRefreshing( true );
                if(lastChoice == "Favorites"){
                    retrieveAll();
                }else PerformMoviesCall(lastChoice);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerViewAdapter = new PostersRecyclerViewAdapter(favoriteMovies,getActivity());
        recyclerViewAdapter.setOnItemClickListener(new PostersRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                String Movie_Details = MoviesDetailsArray.get(position);
                Intent intent = new Intent(getActivity(), MovieDetails.class);
                intent.putExtra(Intent.EXTRA_TEXT, Movie_Details);
                startActivity(intent);
            }
        });
        if(lastChoice == "Favorites"){
            retrieveAll();
        }else PerformMoviesCall(lastChoice);

        return RootView;
    }

    public void PerformMoviesCall(String SortType){

        //progressDialog = ProgressDialog.show(getActivity(),"", "Loading. Please wait...", true);
        ServiceBuilder builder = new ServiceBuilder();
        ServiceInterfaces.Movies movies = builder.BuildMovies();
        Call<APIModel> apiModelCall = movies.getMovies(SortType, Constants.API_KEY);
        apiModelCall.enqueue(new Callback<APIModel>() {
            @Override
            public void onResponse(Call<APIModel> call, Response<APIModel> response) {
                movieObjectList.clear();
                swipeContainer.setEnabled( false );
                swipeContainer.setRefreshing( false );
                try {
                    AllMovies = response.body().MoviesList;
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                for(int i = 0; i < AllMovies.size(); i++) {
                    //PostersPathsArray.add(i,Constants.BASIC_URL+AllMovies.get(i).poster_path);
                    movieObjectList.add(i,new MovieObject(AllMovies.get(i).original_title,
                            Constants.BASIC_URL+AllMovies.get(i).poster_path,AllMovies.get(i).release_date));

                    MoviesDetailsArray.add(i,AllMovies.get(i).id+"="+AllMovies.get(i).original_title+"="+
                            AllMovies.get(i).overview+ "="+AllMovies.get(i).vote_average+"="+
                            AllMovies.get(i).release_date+"="+ Constants.BASIC_URL+AllMovies.get(i).poster_path);
                }

                recyclerViewAdapter = new PostersRecyclerViewAdapter(movieObjectList,getActivity());
                RecyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<APIModel> call, Throwable t) {
                swipeContainer.setEnabled( false );
                swipeContainer.setRefreshing( false );
                Log.v("Retrieve Error", t.toString());
                try {
                    Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void retrieveAll() {

        try {
            favoriteMovies.clear();
            swipeContainer.setEnabled( false );
            swipeContainer.setRefreshing( false );
            ArrayList<ArrayList<Object>> row;
            row = moviesDB.getAllMovies();
            for(int i=0 ; i<row.size() ; i++) {

                favoriteMovies.add(i,new MovieObject((String) row.get(i).get(1), (String) row.get(i).get(5),
                        (String) row.get(i).get(4)));

                MoviesDetailsArray.add(i, row.get(i).get(0) + "=" + row.get(i).get(1) + "=" + row.get(i).get(2)
                        + "=" + row.get(i).get(3) + "=" + row.get(i).get(4) + "=" + row.get(i).get(5));
            }
        } catch (Exception e) {
            Log.e("Retrieve Error", e.toString());
            e.printStackTrace();
        }

        recyclerViewAdapter = new PostersRecyclerViewAdapter(favoriteMovies,getActivity());
        RecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject mostPopular = new MenuObject("Most Popular");
        mostPopular.setResource(R.drawable.ic_theaters_black_24dp);
        mostPopular.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject topRated = new MenuObject("Top Rated");
        topRated.setResource(R.drawable.ic_star_black_24dp);
        topRated.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject Favorites = new MenuObject("Favorites");
        Favorites.setResource(R.drawable.ic_favorite_black_24dp);
        Favorites.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject Close = new MenuObject("Close");
        Close.setResource(R.drawable.ic_cancel_black_24dp);
        Close.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        menuObjects.add(mostPopular);
        menuObjects.add(topRated);
        menuObjects.add(Favorites);
        menuObjects.add(Close);

        return menuObjects;
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {

        switch (position){
            case 0:
                PerformMoviesCall(mostPopular);
                lastChoice = getString(R.string.pref_sort_value_mostpopular);
                break;
            case 1:
                PerformMoviesCall(topRated);
                lastChoice = getString(R.string.top_rated);
                break;
            case 2:
                retrieveAll();
                lastChoice = "Favorites";
                break;
        }
    }

}
