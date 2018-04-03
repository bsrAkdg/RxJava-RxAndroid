package noteapp.busra.com.noteapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bakdag on 2.04.2018.
 */

public class PrefUtils {
    //Bu sınıf, her HTTP çağrısında Authorization başlığı alanı olarak gönderilmesi gereken API Key'i depolar ve alır.
    public PrefUtils() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
    }

    public static void storeApiKey(Context context, String apiKey) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("API_KEY", apiKey);
        editor.commit();
    }

    public static String getApiKey(Context context) {
        return getSharedPreferences(context).getString("API_KEY", null);
    }
}
