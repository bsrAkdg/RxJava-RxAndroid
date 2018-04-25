package movieapp.bsrakdg.com.movieapp.viewModel;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Observable;

import movieapp.bsrakdg.com.movieapp.model.Movie;
import movieapp.bsrakdg.com.movieapp.utils.Constant;

public class MovieDetailViewModel extends Observable{

    private Movie movie;

    public MovieDetailViewModel(Movie movie){
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

    public String getOverview(){
        return movie.overview;
    }

}
