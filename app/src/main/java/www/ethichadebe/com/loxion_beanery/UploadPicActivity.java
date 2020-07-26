package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.AppHelper;
import util.VolleyMultipartRequest;
import util.VolleySingleton;
import www.ethichadebe.co.za.uploadpicture.UploadImage;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.checkData;
import static util.HelperMethods.getError;
import static util.HelperMethods.loadData;
import static util.HelperMethods.saveData;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;

public class UploadPicActivity extends AppCompatActivity {
    private static final String TAG = "UploadPicActivity";
    private Dialog myDialog;
    private TextView tvUpload;
    private ImageView civProfilePicture;
    private UploadImage uploadImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pic);
        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        tvUpload = findViewById(R.id.tvUpload);

        tvUpload.setOnClickListener(v -> saveProfileAccount());
        myDialog = new Dialog(this);
        civProfilePicture = findViewById(R.id.civProfilePicture);

        uploadImage = new UploadImage(this, this, getPackageManager(), myDialog, civProfilePicture,
                "www.ethichadebe.com.loxion_beanery", TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uploadImage.onActivityResult(getCacheDir(), requestCode, resultCode, data);
    }

    //Editing profile
    private void saveProfileAccount() {
        ShowLoadingPopup(myDialog, true);
        // loading or check internet connection or something...
        // ... then
        String url = getIpAddress() + "/users/EditProfilePicture";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.PUT, url,
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    String resultResponse = new String(response.data);
                    try {
                        JSONObject JSONData = new JSONObject(resultResponse);
                        if (JSONData.getString("data").equals("saved")) {
                            startActivity(new Intent(this, LoginActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(this, "Error: " + getError(error), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uID", String.valueOf(getIntent().getIntExtra("U_ID",-1)));
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("ProfilePicture", new DataPart("User" + getUser().getuID() + ".jpg",
                        AppHelper.getFileDataFromDrawable(getBaseContext(), civProfilePicture.getDrawable()),
                        "image/jpeg"));

                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        uploadImage.onRequestPermissionsResult(requestCode, grantResults);
    }

    public void Skip(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    //Changing profile picture
    public void ProfilePicture(View view) {
        uploadImage.start();
    }
}
