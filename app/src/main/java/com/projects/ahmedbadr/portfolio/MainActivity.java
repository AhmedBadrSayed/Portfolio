package com.projects.ahmedbadr.portfolio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.projects.ahmedbadr.portfolio.Activities.Movies;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.popular_movies)
    Button popularMovies;
    @BindView(R.id.stock_hawk)
    Button stockHawk;
    @BindView(R.id.build_it)
    Button buildIt;
    @BindView(R.id.material)
    Button Material;
    @BindView(R.id.ubiquitous)
    Button Ubiquitous;
    @BindView(R.id.capstone)
    Button Capstone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        popularMovies.setOnClickListener(this);
        stockHawk.setOnClickListener(this);
        buildIt.setOnClickListener(this);
        Material.setOnClickListener(this);
        Ubiquitous.setOnClickListener(this);
        Capstone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.popular_movies:
                startActivity(new Intent(this,Movies.class));
                break;
            case R.id.stock_hawk:
                Toast.makeText(getApplication(),"This will launch my stock hawk project",Toast.LENGTH_SHORT).show();
                break;
            case R.id.build_it:
                Toast.makeText(getApplication(),"This will launch my build it bigger project",Toast.LENGTH_SHORT).show();
                break;
            case R.id.material:
                Toast.makeText(getApplication(),"This will launch my material project",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ubiquitous:
                Toast.makeText(getApplication(),"This will launch my ubiquitous project",Toast.LENGTH_SHORT).show();
                break;
            case R.id.capstone:
                Toast.makeText(getApplication(),"This will launch my capstone project",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
