package movieapp.bsrakdg.com.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by busra on 10.04.2018.
 */

public class PopularMovie implements Serializable {

    @SerializedName("page") Integer page;
    @SerializedName("total_results") Double total_results;
    @SerializedName("total_pages") Double total_pages;
    @SerializedName("results") List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

}
