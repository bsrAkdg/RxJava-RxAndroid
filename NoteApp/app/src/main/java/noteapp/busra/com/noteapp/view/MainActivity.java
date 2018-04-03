package noteapp.busra.com.noteapp.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import noteapp.busra.com.noteapp.R;
import noteapp.busra.com.noteapp.network.ApiClient;
import noteapp.busra.com.noteapp.network.ApiService;
import noteapp.busra.com.noteapp.network.model.Note;
import noteapp.busra.com.noteapp.network.model.User;
import noteapp.busra.com.noteapp.utils.MyDividerItemDecoration;
import noteapp.busra.com.noteapp.utils.PrefUtils;
import noteapp.busra.com.noteapp.utils.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity {
    // https://www.androidhive.info/RxJava/android-rxjava-networking-with-retrofit-gson-notes-app/
    // Gereklilikler : RxJava, Retrofit, Recyclerview(ViewHolder ve DividerItemDecoration), ButterKnife
/**
    registerUser() - Cihazı kaydeder. Her cihaz randomUUID() tarafından benzersiz şekilde tanımlanır, böylece notlar bir cihaza bağlı olacaktır.

    fetchAllNotes() - Cihazda oluşturulan tüm notları getirir ve bunları RecyclerView'da görüntüler.
 API, notları rasgele sırada döndürür, böylece map() operatörü notları not id'sine göre sıralamak için kullanılır.

    createNote() - Yeni not oluşturur ve bunu RecyclerView'a ekler. Yeni oluşturulan not 0 konumuna eklenir ve listeyi yenilemek için notifyItemInserted(0) çağrılır.

    updateNote() - Varolan notu günceller ve notifyItemChanged() öğesini çağırarak adaptöre bildirir.

    deleteNote() - Varolan notu siler ve notifyItemRemoved() yöntemini çağırarak adaptöre bildirir.

    showNoteDialog() - Bir not oluşturmak veya güncellemek için bir dialog  açar. Bu diyalog FAB'a dokunarak açılacaktır.

    showActionsDialog() - Düzenle veya Sil seçenekleriyle bir Diyalog açar. Bu diyalog Not satırına uzun basılarak açılacaktır.

    CompositeDisposable - onDestroy() yöntemindeki tüm abonelikleri eler.
**/
    private static final String TAG = MainActivity.class.getSimpleName();
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.txt_empty_notes_view)
    TextView noNotesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.activity_title_home));
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });

        // white background notification bar
        whiteNotificationBar(fab);

        apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);

        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        /**
         * RecyclerView item'a uzun basılınca alert dialog açılır
         * Varolan notu güncellemesi ya da silmesi istenir.
        **/
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        if (TextUtils.isEmpty(PrefUtils.getApiKey(this))) {
            registerUser();
        } else {
            fetchAllNotes();
        }
    }
    private void deleteNote(final int noteId, final int position) {

        disposable.add(apiService.deleteNote(noteId)
                                 .subscribeOn(Schedulers.io())
                                 .observeOn(AndroidSchedulers.mainThread())
                                 .subscribeWith(new DisposableCompletableObserver() {
                                     @Override
                                     public void onComplete() {
                                         Log.d(TAG, "Not Silindi!" );

                                         // o position' daki notu sil
                                         notesList.remove(position);
                                         mAdapter.notifyItemRemoved(position);

                                         Toast.makeText(getApplicationContext(), "Not silindi", Toast.LENGTH_LONG).show();

                                         toggleEmptyNotes();
                                     }

                                     @Override
                                     public void onError(Throwable e) {
                                         Log.e(TAG, "Hata : " + e.getMessage());
                                         showError(e);
                                     }
                                 })
        );
    }

    private void updateNote(int noteId, final String note, final int position) {
        disposable.add(apiService.updateNote(noteId, note)
                                 .subscribeOn(Schedulers.io())
                                 .observeOn(AndroidSchedulers.mainThread())
                                 .subscribeWith(new DisposableCompletableObserver() {
                                     @Override
                                     public void onComplete() {
                                         Log.d(TAG, "Not güncellendi!" );

                                         // o position' daki notu güncelle
                                         Note oldNote = notesList.get(position);
                                         oldNote.setNote(note);
                                         notesList.set(position, oldNote);
                                         mAdapter.notifyItemChanged(position);
                                     }

                                     @Override
                                     public void onError(Throwable e) {
                                         Log.e(TAG, "Hata : " + e.getMessage());
                                         showError(e);
                                     }
                                 })


        );
    }

    private void createNote(String note) {
        disposable.add(apiService.createNote(note)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new DisposableSingleObserver<Note>() {
                                        @Override
                                        public void onSuccess(Note note) {
                                            if (!TextUtils.isEmpty(note.getError())) {
                                                Toast.makeText(getApplicationContext(), note.getError(), Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            Log.d(TAG, "Yeni not oluşturuldu : " + note.getId() + ", " + note.getNote() + ", " + note.getTimestamp());

                                            // Adatörü güncelle; oluşturulan son not ilk sırada yer alır:
                                            notesList.add(0, note);
                                            mAdapter.notifyItemInserted(0);

                                            toggleEmptyNotes();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e(TAG, "Hata : " + e.getMessage());
                                            showError(e);
                                        }
                                    })
        );
    }

    private void fetchAllNotes() {
        //sırasız
        disposable.add((Disposable) apiService.fetchAllNotes()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new Function<List<Note>, List<Note>>() {
                                         @Override
                                         public List<Note> apply(List<Note> notes) throws Exception {
                                             Collections.sort(notes, new Comparator<Note>() {
                                                 @Override
                                                 public int compare(Note o1, Note o2) {
                                                     Log.d(TAG, "Note id : " + o1.getId() + " Note id : " + o2.getId());
                                                     return o2.getId() - o1.getId();
                                                 }
                                             });
                                             return notes;
                                         }
                                })
                                .subscribeWith(new DisposableSingleObserver<List<Note>>() {
                                    @Override
                                    public void onSuccess(List<Note> notes) {
                                        //notlar sıralanmış ve çekilmiş demektir
                                        notesList.clear();
                                        notesList.addAll(notes);
                                        mAdapter.notifyDataSetChanged();

                                        toggleEmptyNotes();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(TAG, "Hata : " + e.getMessage());
                                        showError(e);
                                    }
                                })
        );

    }

    private void registerUser() {

        String deviceId = UUID.randomUUID().toString();

        disposable.add(apiService.register(deviceId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableSingleObserver<User>() {
                                    @Override
                                    public void onSuccess(User user) {
                                        //Cihaz başarıyla sunucuya kaydedildi şimdi telefona kaydet.
                                        PrefUtils.storeApiKey(getApplicationContext(), user.getApiKey());
                                        Toast.makeText(getApplicationContext(), "Cihaz başarıyla kaydedildi : "
                                                + PrefUtils.getApiKey(getApplicationContext()), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(TAG, "Hata : " + e.getMessage());
                                    }
                                }));
    }


    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
        /**
         * Bir notu shouldUpdate=true olduğunda enter/edit seçenekleriyle dialog gösterir,
         * eski notu otomatik olarak görüntüler ve butonu UPDATE olarak değiştirir.
        **/
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && note != null) {
            inputNote.setText(note.getNote());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    updateNote(note.getId(), inputNote.getText().toString(), position);
                } else {
                    // create new note
                    createNote(inputNote.getText().toString());
                }
            }
        });
    }

    private void whiteNotificationBar(FloatingActionButton fab) {

    }

    private void showActionsDialog(final int position) {
        // Edit-Delete Dialog Edit - 0 Delete - 1
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, notesList.get(position), position);
                } else {
                    deleteNote(notesList.get(position).getId(), position);
                }
            }
        });
        builder.show();
    }

    private void toggleEmptyNotes() {
        if (notesList.size() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    private void showError(Throwable e) {
        String message = "";
        try {
            if (e instanceof IOException) {
                message = "İnternet bağlantısı yok'";
            } else if (e instanceof HttpException) {
                HttpException error = (HttpException) e;
                String errorBody = error.response().errorBody().string();
                JSONObject jObj = new JSONObject(errorBody);

                message = jObj.getString("error");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (TextUtils.isEmpty(message)) {
            message = "Bilinmeyen bir hata oluştu";
        }
        //hatayı gösterir
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}


