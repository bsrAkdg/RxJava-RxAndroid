package movieapp.bsrakdg.com.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by busra on 10.04.2018.
 */

public class Movie implements Serializable {

    @SerializedName("vote_count") Double vote_count;
    @SerializedName("id") Double id;
    @SerializedName("video") Boolean video;
    @SerializedName("vote_average") Double vote_average;
    @SerializedName("title") String title;
    @SerializedName("popularity") Double popularity;
    @SerializedName("poster_path") String poster_path;
    @SerializedName("original_language") String original_language;
    @SerializedName("original_title") String original_title;
    @SerializedName("genre_ids") List<Integer> genre_ids;
    @SerializedName("backdrop_path") String backdrop_path;
    @SerializedName("adult") Boolean adult;
    @SerializedName("overview") String overview;
    @SerializedName("release_date") String release_date;

}
