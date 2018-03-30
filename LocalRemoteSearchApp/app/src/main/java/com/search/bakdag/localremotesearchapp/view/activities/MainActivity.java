package com.search.bakdag.localremotesearchapp.view.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.search.bakdag.localremotesearchapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    /*Local Search : kişiler başlangıçta network çağrısı yaparak getirilecektir.
    Arama, RecyclerView'ın getFilter() yöntemini kullanarak bir array listesinde gerçekleştirilecektir.
    Bu durumda, HTTP isteklerinin sayısı sadece bir tane olacaktır.

    Remote Search : kullanıcı arama sorgusuna her girdiğinde bir HTTP çağrısı yapılır.
    Arama sunucu üzerinde gerçekleştirilecek ve sonuçlar geri verilecektir.*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_local_search)
    public void openLocalSearch() {
        // launching local search activity
        startActivity(new Intent(MainActivity.this, LocalSearchActivity.class));
    }

    @OnClick(R.id.btn_remote_search)
    public void openRemoteSearch() {
        // launch remote search activity
        startActivity(new Intent(MainActivity.this, RemoteSearchActivity.class));
    }

}