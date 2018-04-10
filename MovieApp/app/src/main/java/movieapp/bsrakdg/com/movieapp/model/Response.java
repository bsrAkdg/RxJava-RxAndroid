package movieapp.bsrakdg.com.movieapp.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by busra on 10.04.2018.
 */

public class Response implements Serializable{

    @SerializedName("success") Boolean success;
    @SerializedName("guest_session_id") String guest_session_id;
    @SerializedName("expires_at") String expires_at;

}
