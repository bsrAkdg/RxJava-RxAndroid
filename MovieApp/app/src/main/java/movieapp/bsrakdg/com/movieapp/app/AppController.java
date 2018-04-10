package movieapp.bsrakdg.com.movieapp.app;

import android.app.Application;
import android.content.Context;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import movieapp.bsrakdg.com.movieapp.network.ApiClient;
import movieapp.bsrakdg.com.movieapp.network.ApiService;

/**
 * Created by busra on 10.04.2018.
 */

public class AppController extends Application {

    private ApiClient apiClient;
    private Scheduler scheduler;

    private static AppController get(Context context) {
        return (AppController) context.getApplicationContext();
    }

    public static AppController create(Context context) {
        return AppController.get(context);
    }

    public ApiClient getApiClient() {
        if (apiClient == null) {
            apiClient = ApiService.getApiClient();
        }
        return apiClient;
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }
        return scheduler;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

}
