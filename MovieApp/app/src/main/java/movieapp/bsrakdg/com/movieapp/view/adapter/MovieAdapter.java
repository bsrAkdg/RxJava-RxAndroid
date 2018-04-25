package movieapp.bsrakdg.com.movieapp.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import movieapp.bsrakdg.com.movieapp.R;
import movieapp.bsrakdg.com.movieapp.databinding.ItemMovieBinding;
import movieapp.bsrakdg.com.movieapp.model.Movie;
import movieapp.bsrakdg.com.movieapp.viewModel.ItemMovieViewModel;

/**
 * Created by busra on 10.04.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieVH> {

    private List<Movie> movieList;

    public MovieAdapter(){
        this.movieList = Collections.emptyList();
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding itemMovieBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_movie, parent, false);
        return new MovieVH(itemMovieBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {
        holder.bindMovie(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setMovieList(List<Movie> movieList){
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public static class MovieVH extends RecyclerView.ViewHolder{

        ItemMovieBinding itemMovieBinding;

        public MovieVH(ItemMovieBinding itemMovieBinding) {
            super(itemMovieBinding.itemMovie);
            this.itemMovieBinding = itemMovieBinding;
        }

        void bindMovie(Movie movie){
            if (itemMovieBinding.getMovieViewModel() == null){
                itemMovieBinding.setMovieViewModel(new ItemMovieViewModel(movie, itemView.getContext()));
            }else{
                itemMovieBinding.getMovieViewModel().setMovie(movie);
            }
        }
    }
}
