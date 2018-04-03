package noteapp.busra.com.noteapp.network;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import noteapp.busra.com.noteapp.network.model.Note;
import noteapp.busra.com.noteapp.network.model.User;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by bakdag on 2.04.2018.
 */

public interface ApiService {

    // Single Observable, register, notes/new ve notes/all için single response verecektir.
    // Compotable Observable, call status dışında herhangi bir response vermeyecek update(put) ve delete için kullanılacak.

    // Yeni kullanıcı kaydetme
    @FormUrlEncoded
    @POST("notes/user/register")
    Single<User> register(@Field("device_id") String deviceId);

    // Yeni not oluşturur (max 15)
    @FormUrlEncoded
    @POST("notes/new")
    Single<Note> createNote(@Field("note") String note);

    // Tüm notları getirir
    @GET("notes/all")
    Single<List<Note>> fetchAllNotes();

    // Tek bir notu günceller
    @FormUrlEncoded
    @PUT("notes/{id}")
    Completable updateNote(@Path("id") int noteId, @Field("note") String note);

    // Notu siler
    @DELETE("notes/{id}")
    Completable deleteNote(@Path("id") int noteId);
}
