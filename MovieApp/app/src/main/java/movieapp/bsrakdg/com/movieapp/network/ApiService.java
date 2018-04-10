package movieapp.bsrakdg.com.movieapp.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static movieapp.bsrakdg.com.movieapp.utils.Constant.BASE_URL;

/**
 * Created by busra on 10.04.2018.
 */

public class ApiService {

    private ApiClient apiClient;

    public static ApiClient getApiClient(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(ApiClient.class);
    }
}
