package movieapp.bsrakdg.com.movieapp.viewModel;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import movieapp.bsrakdg.com.movieapp.R;
import movieapp.bsrakdg.com.movieapp.app.AppController;
import movieapp.bsrakdg.com.movieapp.model.Movie;
import movieapp.bsrakdg.com.movieapp.model.PopularMovie;
import movieapp.bsrakdg.com.movieapp.network.ApiClient;
import movieapp.bsrakdg.com.movieapp.network.ApiService;
import movieapp.bsrakdg.com.movieapp.utils.Constant;

/**
 * Created by busra on 10.04.2018.
 */

public class MovieViewModel extends Observable {

    private Context context;
    private List<Movie> movieList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ObservableInt progressBar;
    public ObservableInt movieRecycler;
    public ObservableInt userLabel;
    public ObservableField<String> messageLabel;

    public MovieViewModel(@Nullable Context context){
        this.context = context;
        this.movieList = new ArrayList<Movie>();
        //bu observable' lar değişiklik olduğunda UI' yı güncelleyecek.
        progressBar = new ObservableInt(View.GONE);
        movieRecycler = new ObservableInt(View.GONE);
        userLabel = new ObservableInt(View.VISIBLE);
        messageLabel = new ObservableField<>(context.getString(R.string.default_message_loading_movies));
    }

    public void onClickFloatToLoad(View view){
        initializeViews();
        fetchMovieList();
    }

    public void initializeViews(){
        userLabel.set(View.GONE);
        movieRecycler.set(View.GONE);
        progressBar.set(View.VISIBLE);
    }

    private void fetchMovieList(){

        //retrofit singleton
        AppController appController = AppController.create(context);
        ApiClient apiClient = appController.getApiClient();

        //rx
        Disposable disposable = apiClient.fetchMovies(Constant.FETCH_MOVIE)
                                .subscribeOn(appController.subscribeScheduler())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<PopularMovie>() {
                                    @Override
                                    public void accept(PopularMovie popularMovie) throws Exception {
                                        updateList(popularMovie.getResults());
                                        progressBar.set(View.GONE);
                                        userLabel.set(View.GONE);
                                        movieRecycler.set(View.VISIBLE);
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        messageLabel.set(context.getString(R.string.error_message_loading_movies));
                                        progressBar.set(View.GONE);
                                        userLabel.set(View.VISIBLE);
                                        movieRecycler.set(View.GONE);                                    }
                                });

        compositeDisposable.add(disposable);
    }

    private void updateList(List<Movie> movies){
        movieList.addAll(movies);
        setChanged();
        notifyObservers();
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    private void unSubscribeFromObservable(){
        if (compositeDisposable != null && !compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

    public void reset(){
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }
}
