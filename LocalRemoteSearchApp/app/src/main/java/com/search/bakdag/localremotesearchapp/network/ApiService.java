package com.search.bakdag.localremotesearchapp.network;

import com.search.bakdag.localremotesearchapp.network.model.Contact;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by bakdag on 30.03.2018.
 */

public interface ApiService {
    // Single Observable, burada kişi listesi bir kerede alınacak şekilde kullanılır.
    // getContacts() iki parametre alır source : gmail veya linkedin, query : gerçek arama sorgusu olacaktır.
    @GET("contacts.php")
    Single<List<Contact>> getContacts(@Query("source") String source, @Query("search") String query);

}
