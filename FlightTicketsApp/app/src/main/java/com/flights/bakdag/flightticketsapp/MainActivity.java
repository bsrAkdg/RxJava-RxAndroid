package com.flights.bakdag.flightticketsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /* 1. Single Observable : İlk olarak, biletlerin listesini yayınlayan bir Observable create etmemiz gerekir.
     Bu Observable'ın görevi biletler JSON'unu yalnızca bir kez almak ve bu  JSON' u birçok kez yaymaktır.

       2. Multiple Observers :  İlk Observer bilet listesini alır ve verileri
       fiyat ve koltuklar dışındaki bilet detaylarını gösteren RecyclerView'da toplar.
       İkinci Observer, Observable listeyi tek bilet yayınına dönüştürür.
       Tek biletin her birinde, fiyatı ve mevcut koltukları almak için başka bir HTTP çağrısı yapılır.

       3. replay() operatörü ile Observable' a operatör ekleyeceğiz.
       replay() mantığı (HTTP çağrısı) yeniden çalıştırmadan yeni abonelikte veri yayınlamaya başlayacağı anlamına gelir.

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
