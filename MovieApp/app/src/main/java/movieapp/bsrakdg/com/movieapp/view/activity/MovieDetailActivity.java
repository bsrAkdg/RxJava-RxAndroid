package movieapp.bsrakdg.com.movieapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import movieapp.bsrakdg.com.movieapp.R;
import movieapp.bsrakdg.com.movieapp.databinding.ActivityMovieDetailBinding;
import movieapp.bsrakdg.com.movieapp.model.Movie;
import movieapp.bsrakdg.com.movieapp.viewModel.MovieDetailViewModel;
import movieapp.bsrakdg.com.movieapp.viewModel.MovieViewModel;

/**
 * Created by busra on 10.04.2018.
 */

public class MovieDetailActivity extends AppCompatActivity {

    MovieDetailViewModel movieDetailViewModel;
    ActivityMovieDetailBinding activityMovieDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        displayHomeAsUpEnabled();

    }

    private void initDataBinding(){
        activityMovieDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        setSupportActionBar(activityMovieDetailBinding.toolbar);
        Movie movie = (Movie) getIntent().getSerializableExtra("Movie");
        movieDetailViewModel = new MovieDetailViewModel(movie);
        activityMovieDetailBinding.setMovieDetailViewModel(movieDetailViewModel);
        setTitle(movie.title);
    }

    public static Intent showDetail(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra("Movie", movie);
        return intent;
    }

    private void displayHomeAsUpEnabled(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
