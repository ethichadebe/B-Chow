package www.ethichadebe.com.loxion_beanery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import SingleItem.MyShopItem;
import util.HelperMethods;
import util.VolleyMultipartRequest;
import util.VolleySingleton;
import www.ethichadebe.co.za.uploadpicture.UploadImage;

import static util.AppHelper.getFileDataFromDrawable;
import static util.Constants.getIpAddress;
import static util.HelperMethods.DisplayImage;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.checkData;
import static util.HelperMethods.loadData;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.getNewShop;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.isCompleteReg;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.setNewShop;
import static www.ethichadebe.com.loxion_beanery.ShopSettingsActivity.isEdit;

public class RegisterShopActivity extends AppCompatActivity {
    private static final String TAG = "RegisterShopActivity";
    private Dialog myDialog;
    private TextView tvName, tvLocation, tvAddress;
    private MaterialEditText etName, etShortDescription, etFullDescription;
    private Button btnNext;
    private ImageView[] ivImages = new ImageView[2];
    private UploadImage uploadImage;
    private LinearLayout llLocation;
    private LatLng sLocation;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);
        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        myDialog = new Dialog(this);
        etName = findViewById(R.id.etName);
        tvAddress = findViewById(R.id.tvAddress);
        btnNext = findViewById(R.id.btnNext);
        tvName = findViewById(R.id.tvName);
        tvLocation = findViewById(R.id.tvLocation);
        ivImages[0] = findViewById(R.id.civSmall);
        ivImages[1] = findViewById(R.id.civBig);
        etShortDescription = findViewById(R.id.etShortDescription);
        etFullDescription = findViewById(R.id.etFullDescription);
        llLocation = findViewById(R.id.llLocation);

        uploadImage = new UploadImage(this, this, getPackageManager(), myDialog, ivImages,
                "www.ethichadebe.com.loxion_beanery", TAG);
        uploadImage.setWidthHeight(720, 351, 0);
        uploadImage.setWidthHeight(540, 328, 1);

        if (getNewShop() != null) {
            etName.setText(getNewShop().getStrShopName());
            if (!getNewShop().getStrAddress().isEmpty()) {
                tvLocation.setText(getNewShop().getStrAddress());
                tvAddress.setText(getNewShop().getStrAddress());
                sLocation = getNewShop().getLlLocation();
            }
            if (getNewShop().getStrShortDescript() != null) {
                etShortDescription.setText(getNewShop().getStrShortDescript());
            }
            if (getNewShop().getStrFullDescript() != null)
                etFullDescription.setText(getNewShop().getStrFullDescript());
            if (getNewShop().getDraLogoBig() != null) {
                ivImages[1].setImageDrawable(getNewShop().getDraLogoBig());
            }
            if (getNewShop().getDraLogoSmall() != null) {
                ivImages[0].setImageDrawable(getNewShop().getDraLogoSmall());
            }

            if (getNewShop().getStrLogoSmall() != null) {
                DisplayImage(ivImages[0], getNewShop().getStrLogoSmall());
            }

            if (getNewShop().getStrLogoBig() != null) {
                DisplayImage(ivImages[1], getNewShop().getStrLogoBig());
            }
        }//If user pressed back from Operation Hours activity

        if (isEdit) {
            btnNext.setText("Save");
        } //When user comes from shop settings

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    tvName.setText(charSequence);
                } else {
                    tvName.setText("Shop name");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Initialize maps places
        Places.initialize(this, getResources().getString(R.string.google_maps_api_key));

    }

    public void ShowPopup() {
        TextView tvCancel, tvMessage, btnYes, btnNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        btnNo = myDialog.findViewById(R.id.btnNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        if (getNewShop() != null) {
            tvMessage.setText("Would you like to save changes before exiting?");
        } else {
            tvMessage.setText("All entered information will be lost\nAre you sure?");
        }

        btnYes.setOnClickListener(view -> {
            if (getNewShop() != null) {
                PUTShopDetails(true);
            } else {
                myDialog.dismiss();
                startActivity(new Intent(this, MainActivity.class));
                setIntFragment(3);
            }
        });

        btnNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100) && (resultCode == RESULT_OK)) {
            Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(data));

            tvLocation.setText(place.getAddress());
            tvAddress.setText(place.getAddress());
            llLocation.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
            sLocation = place.getLatLng();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(Objects.requireNonNull(data));
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }

        uploadImage.onActivityResult(getCacheDir(), requestCode, resultCode, data, index);
    }

    public void next(View view) {
        if (tvLocation.getText().toString().equals("Add shop location") &&
                Objects.requireNonNull(etName.getText()).toString().isEmpty()) {
            etName.setError("required");
            llLocation.setBackground(getResources().getDrawable(R.drawable.ripple_effect_red));
        } else if (Objects.requireNonNull(etName.getText()).toString().isEmpty()) {
            etName.setError("required");
        } else if (tvLocation.getText().toString().equals("Add shop location")) {
            llLocation.setBackground(getResources().getDrawable(R.drawable.ripple_effect_red));
        } else {
            if (isEdit) {
                PUTShopDetails(false);
            } else {
                if (!isCompleteReg) {
                    setNewShop(new MyShopItem(etName.getText().toString(),
                            Objects.requireNonNull(etShortDescription.getText()).toString(),
                            Objects.requireNonNull(etFullDescription.getText()).toString(),
                            ivImages[0].getDrawable(), ivImages[1].getDrawable(), sLocation,
                            tvLocation.getText().toString()));
                } else {
                    getNewShop().setShopEdit(etName.getText().toString(),
                            Objects.requireNonNull(etShortDescription.getText()).toString(),
                            Objects.requireNonNull(etFullDescription.getText()).toString(), ivImages[0].getDrawable(),
                            ivImages[1].getDrawable(), sLocation, tvLocation.getText().toString());
                }
                Log.d(TAG, "next: Shop Name: " + getNewShop().getStrShopName());
                startActivity(new Intent(RegisterShopActivity.this,
                        OperatingHoursActivity.class));
            }
        }
    }

    public void Small(View view) {
        index = 0;
        uploadImage.start(index);
    }

    public void big(View view) {
        index = 1;
        uploadImage.start(index);
    }

    public void back(View view) {
        back();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void addLocation(View view) {
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                fieldList).build(this);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        uploadImage.onRequestPermissionsResult(requestCode, grantResults, index);
    }

    private void PUTShopDetails(boolean isBack) {
        ShowLoadingPopup(myDialog, true);
        // loading or check internet connection or something...
        // ... then
        String url = getIpAddress() + "/shops/Register/" + getNewShop().getIntID();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.PUT, url,
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);

                    getNewShop().setStrShopName(Objects.requireNonNull(etName.getText()).toString());
                    getNewShop().setStrFullDescript(Objects.requireNonNull(etFullDescription.getText()).toString());
                    getNewShop().setStrShortDescript(Objects.requireNonNull(etShortDescription.getText()).toString());
                    getNewShop().setStrAddress(tvLocation.getText().toString());
                    Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
                    if (isBack) {
                        //If user presses back in edit.
                        finish();
                    }
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            NetworkResponse networkResponse = error.networkResponse;
            String errorMessage = "Unknown error";
            if (networkResponse == null) {
                if (error.getClass().equals(TimeoutError.class)) {
                    errorMessage = "Request timeout";
                } else if (error.getClass().equals(NoConnectionError.class)) {
                    errorMessage = "Failed to connect server";
                }
            } else {
                String result = new String(networkResponse.data);
                try {
                    JSONObject response = new JSONObject(result);
                    String status = response.getString("status");
                    String message = response.getString("message");

                    Log.e("Error Status", status);
                    Log.e("Error Message", message);

                    if (networkResponse.statusCode == 404) {
                        errorMessage = "Resource not found";
                    } else if (networkResponse.statusCode == 401) {
                        errorMessage = message + " Please login again";
                    } else if (networkResponse.statusCode == 400) {
                        errorMessage = message + " Check your inputs";
                    } else if (networkResponse.statusCode == 500) {
                        errorMessage = message + " Something is getting wrong";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sName", Objects.requireNonNull(etName.getText()).toString());
                params.put("sShortDescrption", Objects.requireNonNull(etShortDescription.getText()).toString());
                params.put("sFullDescription", Objects.requireNonNull(etFullDescription.getText()).toString());
                params.put("sLatitude", String.valueOf(sLocation.latitude));
                params.put("sLongitude", String.valueOf(sLocation.longitude));
                params.put("sAddress", tvAddress.getText().toString());
                params.put("uID", String.valueOf(getUser().getuID()));
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("sSmallPicture", new DataPart("small" +
                        getNewShop().getStrShopName().replace("[^a-zA-Z0-9]", "_") + ".jpg",
                        getFileDataFromDrawable(getBaseContext(), ivImages[0].getDrawable()),
                        "image/jpeg"));
                params.put("sBigPicture", new DataPart("big" +
                        getNewShop().getStrShopName().replaceAll("[^a-zA-Z0-9]","_") + ".jpg",
                        getFileDataFromDrawable(getBaseContext(), ivImages[1].getDrawable()),
                        "image/jpeg"));

                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }

    private void back() {
        if (getNewShop() != null) {
            if (!getNewShop().getStrShopName().equals(Objects.requireNonNull(etName.getText()).toString()) ||
                    !getNewShop().getStrFullDescript().equals(Objects.requireNonNull(etFullDescription.getText()).toString()) ||
                    !getNewShop().getStrShortDescript().equals(Objects.requireNonNull(etShortDescription.getText()).toString()) ||
                    !getNewShop().getStrAddress().equals(tvLocation.getText().toString())) {
                ShowPopup();
            } else {
                setIntFragment(3);
                startActivity(new Intent(this, MainActivity.class));
            }
        } else {
            if (!Objects.requireNonNull(etName.getText()).toString().isEmpty() ||
                    !Objects.requireNonNull(etFullDescription.getText()).toString().isEmpty() ||
                    !Objects.requireNonNull(etShortDescription.getText()).toString().isEmpty()) {
                ShowPopup();
            } else {
                setIntFragment(3);
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }
}
