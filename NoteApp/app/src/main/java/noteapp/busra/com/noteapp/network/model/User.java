package noteapp.busra.com.noteapp.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bakdag on 2.04.2018.
 */

public class User extends BaseResponse {

    @SerializedName("api_key")
    String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
