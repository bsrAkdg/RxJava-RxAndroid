package movieapp.bsrakdg.com.movieapp.network;

import io.reactivex.Observable;
import movieapp.bsrakdg.com.movieapp.model.PopularMovie;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by busra on 10.04.2018.
 */

public interface ApiClient {

    // GET https://api.themoviedb.org/3/authentication/guest_session/new?api_key=bf354e3c4e618fe75bf87ab7397788cb SESSİONID=727cd8b562d5b6fb2ae2f3ddd517830d
    @GET
    Observable<PopularMovie> fetchMovies(@Url String url);

}
