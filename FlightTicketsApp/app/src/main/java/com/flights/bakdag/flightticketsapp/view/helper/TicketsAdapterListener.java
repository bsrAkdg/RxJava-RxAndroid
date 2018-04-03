package com.flights.bakdag.flightticketsapp.view.helper;

import com.flights.bakdag.flightticketsapp.network.model.Ticket;

/**
 * Created by bakdag on 30.03.2018.
 */
public interface TicketsAdapterListener {
    void onTicketSelected(Ticket contact);
}