package com.flights.bakdag.flightticketsapp.view;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;

import com.flights.bakdag.flightticketsapp.R;
import com.flights.bakdag.flightticketsapp.network.ApiClient;
import com.flights.bakdag.flightticketsapp.network.ApiService;
import com.flights.bakdag.flightticketsapp.network.model.Price;
import com.flights.bakdag.flightticketsapp.network.model.Ticket;
import com.flights.bakdag.flightticketsapp.view.adapters.TicketsAdapter;
import com.flights.bakdag.flightticketsapp.view.helper.TicketsAdapterListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements TicketsAdapterListener{
    // https://www.androidhive.info/RxJava/flatmap-concatmap-operators-flight-tickets-app/ ' den yapılan proje.

    /* 1. Single Observable : İlk olarak, biletlerin listesini yayınlayan bir Observable create etmemiz gerekir.
     Bu Observable'ın görevi biletler JSON'unu yalnızca bir kez almak ve bu  JSON' u birçok kez yaymaktır.

       2. Multiple Observers :  İlk Observer bilet listesini alır ve verileri
       fiyat ve koltuklar dışındaki bilet detaylarını gösteren RecyclerView'da toplar.
       İkinci Observer, Observable listeyi tek bilet yayınına dönüştürür.
       Tek biletin her birinde, fiyatı ve mevcut koltukları almak için başka bir HTTP çağrısı yapılır.

       3. replay() operatörü ile Observable' a operatör ekleyeceğiz.
       replay() mantığı (HTTP çağrısı) yeniden çalıştırmadan yeni abonelikte veri yayınlamaya başlayacağı anlamına gelir.

     */

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String from = "DEL";
    private static final String to = "HYD";

    //RxJava
    private CompositeDisposable disposable = new CompositeDisposable();
    private Unbinder unbinder;

    //Retrofit-Data
    private ApiService apiService;
    private TicketsAdapter mAdapter;
    private ArrayList<Ticket> ticketsList = new ArrayList<>();

    //view create
    @BindView(R.id.recyclerFlights)
    RecyclerView recyclerFlights;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        apiService = ApiClient.getClient().create(ApiService.class);

        setAdapter();

        /* ConnectableObservable : Normal bir Observable'a benzer, ancak abone olunup connect() yöntemi çağrıldığında öğeleri yayınlanmaya başlar,
           Bu şekilde, gözlemlenen tüm ConnectableObservable öğeler yaymaya başlamadan önce Observable' a abone olunmasını bekleyebilirsiniz.

           replay() : Tüm öğelerini ve bildirimlerini gelecekteki herhangi bir {@link Observer} 'a tekrar gönderecek olan temel bir
           ObservableSource'a tek bir abonelik paylaşan bir {@link ConnectableObservable} döndürür. */
        ConnectableObservable<List<Ticket>> ticketsObservable = getTickets(from, to).replay();

        //tüm biletleri çeken Observable' a Observer subscribe et :
        disposable.add(ticketsObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Ticket>>(){

                            @Override
                            public void onNext(List<Ticket> tickets) {
                                Log.e(TAG, "biletleri çeken observable : " + tickets.size());
                                ticketsList.clear();
                                ticketsList.addAll(tickets);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "observer 1 -> onComplete()");
                            }
                        })
        );

        //her bilet için giyatları çeken observable' a Observer subscribe et :
        disposable.add(
                ticketsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        //List<Ticket> yayınını single Ticket yayınına dönüştürür.
                        .flatMap(new Function<List<Ticket>, ObservableSource<Ticket>>() {
                            @Override
                            public ObservableSource<Ticket> apply(List<Ticket> tickets) throws Exception {
                                Log.e(TAG, "flatMap 1 -> apply");
                                return Observable.fromIterable(tickets);
                            }
                        })
                        //her Ticket için Price getirir
                        .flatMap(new Function<Ticket, ObservableSource<Ticket>>() {
                            @Override
                            public ObservableSource<Ticket> apply(Ticket ticket) throws Exception {
                                Log.e(TAG, "flatMap 2 -> apply");
                                return getPriceObservable(ticket);
                            }
                        })
                        .subscribeWith(new DisposableObserver<Ticket>() {

                            @Override
                            public void onNext(Ticket ticket) {
                                int position = ticketsList.indexOf(ticket);
                                if (position == -1) {
                                    Log.e(TAG,"onNext : Ticket listede bulunamadı");
                                    return;
                                }
                                ticketsList.set(position, ticket);
                                mAdapter.notifyItemChanged(position);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "observer 2-> onComplete()");
                            }
                        }));

        // Calling connect to start emission
        ticketsObservable.connect();


    }

    //Tüm biletleri çeken single bir Observable :
    private Observable<List<Ticket>> getTickets(String from, String to){
        return apiService.searchTickets(from, to)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //her ticket için tek tek bilet fiyatını servisten çeker fiyat bilgisini set eder.
    //Bu Observable geri dönüş olarak Price döndürür ama map() metodu ile türünü değiştiririz.
    private Observable<Ticket> getPriceObservable(final Ticket ticket){
        return apiService.getPrice(ticket.getFlightNumber(), ticket.getFrom(), ticket.getTo())
                         .toObservable()
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .map(new Function<Price, Ticket>() {
                             @Override
                             public Ticket apply(Price price) throws Exception {
                                 Log.e(TAG, "Bilet fiyatı : " + ticket.getFlightNumber() + " -> " + price.getPrice());
                                 ticket.setPrice(price);
                                 return ticket;
                             }
                         });
    }

    void setAdapter(){
        mAdapter = new TicketsAdapter(this, ticketsList, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerFlights.setLayoutManager(mLayoutManager);
        recyclerFlights.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recyclerFlights.setItemAnimator(new DefaultItemAnimator());
        recyclerFlights.setAdapter(mAdapter);

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onTicketSelected(Ticket contact) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
