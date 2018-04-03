package com.flights.bakdag.flightticketsapp.network.model;
/**
 * Created by bakdag on 29.03.2018.
 */
import com.google.gson.annotations.SerializedName;

public class Price {
    float price;
    String seats;
    String currency;

    @SerializedName("flight_number")
    String flightNumber;

    String from;
    String to;

    public float getPrice() {
        return price;
    }

    public String getSeats() {
        return seats;
    }

    public String getCurrency() {
        return currency;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}