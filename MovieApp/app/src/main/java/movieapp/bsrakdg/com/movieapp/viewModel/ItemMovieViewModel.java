package movieapp.bsrakdg.com.movieapp.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import movieapp.bsrakdg.com.movieapp.model.Movie;
import movieapp.bsrakdg.com.movieapp.utils.Constant;
import movieapp.bsrakdg.com.movieapp.view.activity.MovieDetailActivity;

public class ItemMovieViewModel extends BaseObservable {

    private Movie movie;
    private Context context;

    public ItemMovieViewModel(Movie movie, Context context){
        this.context = context;
        this.movie = movie;
    }

    public String getPosterPath(){
        return movie.poster_path;
    }

    @BindingAdapter("imageUrl") public static void setImageUrl(ImageView imageView, String url){
        Glide.with(imageView.getContext()).load(Constant.PICTURE_PATH + url).into(imageView);
    }

    public Double getId(){
        return movie.id;
    }

    public String getTitle(){
        return movie.title;
    }

    public String getReleaseDate(){
        return movie.release_date;
    }

    public String getVoteAverage(){
        return String.valueOf(movie.vote_average);
    }

    public void onItemClick(View view){
        context.startActivity(MovieDetailActivity.showDetail(view.getContext(), movie));
    }

    public void setMovie(Movie movie){
        this.movie = movie;
        notifyChange();
    }
}
