package com.flights.bakdag.flightticketsapp.network;
/**
 * Created by bakdag on 29.03.2018.
 */
import com.flights.bakdag.flightticketsapp.network.model.Price;
import com.flights.bakdag.flightticketsapp.network.model.Ticket;
import java.util.List;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    //biletlerin listesini getirir ve Single Observable kullanılır.
    // kalkış ile varış yeri arasındaki biletleri aramak from-to parametrelerini alır..
    @GET("airline-tickets.php")
    Single<List<Ticket>> searchTickets(@Query("from") String from, @Query("to") String to);

    //Uçus numarasını, kalkış ve varış noktasını alarak her uçuşun fiyatını ve mevcut koltuklarını getirir.
    @GET("airline-tickets-price.php")
    Single<Price> getPrice(@Query("flight_number") String flightNumber, @Query("from") String from, @Query("to") String to);

}
