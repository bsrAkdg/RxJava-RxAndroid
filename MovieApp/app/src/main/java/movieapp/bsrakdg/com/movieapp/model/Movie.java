package movieapp.bsrakdg.com.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by busra on 10.04.2018.
 */

public class Movie implements Serializable {

    @SerializedName("vote_count") public Double vote_count;
    @SerializedName("id") public Double id;
    @SerializedName("video") public Boolean video;
    @SerializedName("vote_average") public Double vote_average;
    @SerializedName("title") public String title;
    @SerializedName("popularity") public Double popularity;
    @SerializedName("poster_path") public String poster_path;
    @SerializedName("original_language") public String original_language;
    @SerializedName("original_title") public String original_title;
    @SerializedName("genre_ids") public List<Integer> genre_ids;
    @SerializedName("backdrop_path") public String backdrop_path;
    @SerializedName("adult") public Boolean adult;
    @SerializedName("overview") public String overview;
    @SerializedName("release_date") public String release_date;

}
