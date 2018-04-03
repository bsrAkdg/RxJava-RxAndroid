package com.search.bakdag.localremotesearchapp.view.helper;

import com.search.bakdag.localremotesearchapp.network.model.Contact;

/**
 * Created by bakdag on 30.03.2018.
 */
public interface ContactsAdapterListener {
    void onContactSelected(Contact contact);
}