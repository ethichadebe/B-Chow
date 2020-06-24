package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.User;

import static util.Constants.getIpAddress;
import static util.HelperMethods.COARSE_LOCATION;
import static util.HelperMethods.FINE_LOCATION;
import static util.HelperMethods.LOCATION_REQUEST_CODE;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.ismLocationGranted;
import static util.HelperMethods.loadData;
import static util.HelperMethods.saveData;
import static util.HelperMethods.setmLocationGranted;
import static www.ethichadebe.com.loxion_beanery.ProfileFragment.isLogout;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CHECK_SETTINGS = 200;
    private static LatLng userLocation;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    static LatLng getUserLocation() {
        return userLocation;
    }

    private static final String TAG = "LoginActivity";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private RelativeLayout rellay1;
    private TextView tvError;
    private CheckBox cbRemember;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
        }
    };
    private static User user;
    private Dialog myDialog;
    private MaterialEditText mTextPassword, mTextUsername;
    private BottomSheetBehavior bsbBottomSheetBehavior;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        rellay1 = findViewById(R.id.rellay1);
        mTextUsername = findViewById(R.id.txtUsername);
        mTextPassword = findViewById(R.id.txtPassword);
        tvError = findViewById(R.id.tvError);
        cbRemember = findViewById(R.id.cbRemember);
        View bsbBottomSheet = findViewById(R.id.bottom_sheet);
        bsbBottomSheetBehavior = BottomSheetBehavior.from(bsbBottomSheet);
        bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        myDialog = new Dialog(this);
        //Get user location
        if (isServicesOk()) {
            getLocationPermissions();
        }

        if (isLogout) {
            saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), "", "");
        }

        //Check if shared prefs are empty
        loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), mTextUsername, mTextPassword);
        if (!Objects.requireNonNull(mTextUsername.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            PostLogin(false);
        } else {
            handler.postDelayed(runnable, 3000);
        }

        Log.d(TAG, "onCreate: getting token");
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String mToken = instanceIdResult.getToken();
            Log.d(TAG, "onCreate: " + mToken);
        });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    private void PostLogin(boolean isPopup) {
        Log.d(TAG, "PostLogin: call login method");
        if (isPopup && (rellay1.getVisibility() == View.VISIBLE)) {
            ShowLoadingPopup(myDialog, true);
        } else {
            YoYo.with(Techniques.FadeIn)
                    .duration(2000)
                    .repeat(2000)
                    .playOn(findViewById(R.id.vLoading));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getIpAddress() + "/users/Login",
                response -> {
                    Log.d(TAG, "PostLogin: Response from server " + response);
                    ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONResponse = new JSONObject(response);

                        if (JSONResponse.getString("data").equals("false")) {
                            Log.d(TAG, "PostLogin: User not found");
                            ShowConfirmationPopup();
                        } else {
                            Log.d(TAG, "PostLogin: User found");
                            JSONObject ObjData = new JSONObject(response);
                            JSONArray ArrData = ObjData.getJSONArray("data");
                            JSONObject userData = ArrData.getJSONObject(0);
                            //Toast.makeText(LoginActivity.this, userData.toString(), Toast.LENGTH_LONG).show();

                            user = new User(userData.getInt("uID"), userData.getString("uName"),
                                    userData.getString("uSurname"), userData.getString("uDOB"),
                                    userData.getString("uSex"), userData.getString("uEmail"),
                                    userData.getString("uNumber"),
                                    userData.getString("uPicture"),
                                    userData.getInt("uType"));
                            if (cbRemember.isChecked()) {//Check if remember me is checked
                                isLogout = false;
                                saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE),
                                        Objects.requireNonNull(mTextUsername.getText()).toString(),
                                        Objects.requireNonNull(mTextPassword.getText()).toString());
                            }
                            Log.d(TAG, "PostLogin: Getting device location");
                            getDeviceLocation();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }, error -> {
            Log.d(TAG, "PostLogin: error " + error);
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                tvError.setText("Connection timeout, Please try again");
            } else if (error.toString().equals("com.android.volley.ServerError")) {
                tvError.setText("Problem from our side, Please try again later");
            } else if (error.toString().contains("UnknownHostException")) {
                tvError.setText("Make sure you're connected");
            } else {
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
            ShowLoadingPopup(myDialog, false);
            bsbBottomSheetBehavior.setHideable(false);
            bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uNumber", Objects.requireNonNull(mTextUsername.getText()).toString());
                params.put("uPassword", Objects.requireNonNull(mTextPassword.getText()).toString());
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    public void ShowConfirmationPopup() {
        TextView tvCancel, tvMessage, btnYes, btnNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        btnNo = myDialog.findViewById(R.id.btnNo);

        tvCancel.setVisibility(View.GONE);

        tvMessage.setText("Seems like the user does not exist");

        btnYes.setText("Create account");
        btnYes.setOnClickListener(view -> {
            myDialog.dismiss();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        btnNo.setText("Retry");
        btnNo.setOnClickListener(view -> {
            saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), "", "");
            startActivity(new Intent(this, LoginActivity.class));
            myDialog.dismiss();
        });
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    public static User getUser() {
        return user;
    }

    public void Retry(View view) {
        PostLogin(false);
        bsbBottomSheetBehavior.setHideable(true);
        bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void login(View view) {
        if (Objects.requireNonNull(mTextUsername.getText()).toString().isEmpty() &&
                Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            mTextUsername.setError("Required");
            mTextPassword.setError("Required");
        } else if (mTextUsername.getText().toString().isEmpty() &&
                !Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            mTextUsername.setError("Required");
        } else if (!mTextUsername.getText().toString().isEmpty() &&
                Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            mTextPassword.setError("Required");
            mTextPassword.setHelperText("Field required");
        } else {
            PostLogin(true);
        }
    }

    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    private void getDeviceLocation() {
        Log.d(TAG, "PostLogin getDeviceLocation: getting current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(this));

        try {
            if (ismLocationGranted()) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "PostLogin onComplete: location found");
                        if (task.getResult() == null) {
                            Log.d(TAG, "PostLogin turnOnLocation: location is null");
                            turnOnLocation();
                        } else {
                            Location currentLocation = (Location) task.getResult();
                            userLocation = new LatLng(Objects.requireNonNull(currentLocation).getLatitude(),
                                    currentLocation.getLongitude());

                            startActivity(new Intent(this, MainActivity.class));
                        }

                    } else {
                        Log.d(TAG, "PostLogin onComplete: Unable to get location");
                        Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security exception " + e.getMessage());
        }
    }

    private void turnOnLocation() {
        Log.d(TAG, "PostLogin turnOnLocation: trying to turn on location");
        LocationRequest request = new LocationRequest().setFastestInterval(1500).setInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(request);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {
            try {
                Log.d(TAG, "PostLogin turnOnLocation: gets here");
                task.getResult(ApiException.class);

                //Successful
                Log.d(TAG, "PostLogin turnOnLocation: successful");
                PostLogin(false);
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.d(TAG, "PostLogin turnOnLocation: Turning on location");
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        } catch (ClassCastException ex) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        setmLocationGranted(false);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: Permissions failed");
                        return;
                    }
                }
                Log.d(TAG, "onRequestPermissionsResult: Permissions granted");
                setmLocationGranted(true);
            }
        }
    }

    private void getLocationPermissions() {
        Log.d(TAG, "getLocationPermissions: Getting location permissions");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this,
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getLocationPermissions: Location granted");
                setmLocationGranted(true);
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_REQUEST_CODE);
        }

    }

    private boolean isServicesOk() {
        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOk: google services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOk: error occurred but can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available,
                    ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You cant make map requests", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CHECK_SETTINGS) && (resultCode == RESULT_OK)) {
            PostLogin(false);
        }
    }
}