package movieapp.bsrakdg.com.movieapp.view.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Observable;
import java.util.Observer;

import movieapp.bsrakdg.com.movieapp.R;
import movieapp.bsrakdg.com.movieapp.databinding.ActivityMovieBinding;
import movieapp.bsrakdg.com.movieapp.view.adapter.MovieAdapter;
import movieapp.bsrakdg.com.movieapp.viewModel.MovieViewModel;

public class MoviesActivity extends AppCompatActivity implements Observer {

    private ActivityMovieBinding activityMovieBinding;
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        setSupportActionBar(activityMovieBinding.toolbar);
        setMovieAdapter(activityMovieBinding.listMovie);
        setObserver(movieViewModel);
    }

    private void initDataBinding(){
        activityMovieBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);
        movieViewModel = new MovieViewModel(this);
        activityMovieBinding.setMovieViewModel(movieViewModel);
    }

    private void setMovieAdapter(RecyclerView recyclerView){
        MovieAdapter movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setObserver(Observable observable){
       observable.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MovieViewModel){
            MovieAdapter movieAdapter = (MovieAdapter) activityMovieBinding.listMovie.getAdapter();
            MovieViewModel movieViewModel = (MovieViewModel) o;
            movieAdapter.setMovieList(movieViewModel.getMovieList());
        }
    }
}
