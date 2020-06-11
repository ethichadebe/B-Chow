package www.ethichadebe.com.loxion_beanery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import SingleItem.MyShopItem;
import util.HelperMethods;
import util.VolleyMultipartRequest;
import util.VolleySingleton;

import static util.AppHelper.getFileDataFromDrawable;
import static util.Constants.getIpAddress;
import static util.HelperMethods.CAMERA_PERMISSION;
import static util.HelperMethods.DisplayImage;
import static util.HelperMethods.STORAGE_PERMISSION;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.combineString;
import static util.HelperMethods.createFile;
import static util.HelperMethods.requestPermission;
import static util.HelperMethods.startCrop;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.getNewShop;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.setNewShop;
import static www.ethichadebe.com.loxion_beanery.ShopSettingsActivity.isEdit;

public class RegisterShopActivity extends AppCompatActivity {
    private static final String TAG = "RegisterShopActivity";
    private Dialog myDialog;
    private TextView tvName, tvLocation, tvAddress;
    private MaterialEditText etName, etShortDescription, etFullDescription;
    private Boolean isBig;
    private Button btnNext;
    private ImageView civSmall, civBig;
    private LinearLayout llLocation;
    private LatLng sLocation;
    private String pathToFile, strShopID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } //Check if user is logged in

        myDialog = new Dialog(this);
        etName = findViewById(R.id.etName);
        tvAddress = findViewById(R.id.tvAddress);
        btnNext = findViewById(R.id.btnNext);
        tvName = findViewById(R.id.tvName);
        tvLocation = findViewById(R.id.tvLocation);
        civSmall = findViewById(R.id.civSmall);
        civBig = findViewById(R.id.civBig);
        etShortDescription = findViewById(R.id.etShortDescription);
        etFullDescription = findViewById(R.id.etFullDescription);
        llLocation = findViewById(R.id.llLocation);

        if (getNewShop() != null) {
            strShopID = String.valueOf(getNewShop().getIntID());
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
                civBig.setImageDrawable(getNewShop().getDraLogoBig());
            }
            if (getNewShop().getDraLogoSmall() != null) {
                civSmall.setImageDrawable(getNewShop().getDraLogoSmall());
            }

            if (getNewShop().getStrLogoSmall() != null) {
                DisplayImage(civSmall, getNewShop().getStrLogoSmall());
            }

            if (getNewShop().getStrLogoBig() != null) {
                DisplayImage(civBig, getNewShop().getStrLogoBig());
            }
        }//If user pressed back from Operation Hours activity

        if (isEdit) {
            btnNext.setText("Save");
        } //When user comes from shop settings

        /*if (!Objects.requireNonNull(etName.getText()).toString().isEmpty()) {
            tvName.setText(etName.getText().toString());
        }*/

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
                PUTShopDetails();
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
        Uri uri;
        if ((requestCode == 100) && (resultCode == RESULT_OK)) {
            Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(data));

            tvLocation.setText(place.getAddress());
            tvAddress.setText(place.getAddress());
            llLocation.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
            sLocation = place.getLatLng();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(Objects.requireNonNull(data));
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        } else if ((requestCode == STORAGE_PERMISSION) && (resultCode == RESULT_OK)) {
            uri = Objects.requireNonNull(data).getData();
            if (uri != null) {
                if (isBig) {
                    startCrop(this, getCacheDir(), uri, 720, 444);
                } else {
                    startCrop(this, getCacheDir(), uri, 720, 349);
                }
            }
        } else if ((requestCode == CAMERA_PERMISSION) && (resultCode == RESULT_OK)) {
            if (BitmapFactory.decodeFile(pathToFile) != null) {
                if (isBig) {
                    startCrop(this, getCacheDir(), Uri.fromFile(new File(pathToFile)), 720, 444);
                } else {
                    startCrop(this, getCacheDir(), Uri.fromFile(new File(pathToFile)), 720, 349);
                }
            }
        } else if ((requestCode == UCrop.REQUEST_CROP) && (resultCode == RESULT_OK)) {
            uri = UCrop.getOutput(Objects.requireNonNull(data));
            if (uri != null) {
                if (isBig) {
                    civBig.setImageDrawable(null);
                    civBig.setImageURI(uri);
                } else {
                    civSmall.setImageDrawable(null);
                    civSmall.setImageURI(uri);
                }
            }
        }
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
            setNewShop(new MyShopItem(etName.getText().toString(),
                    Objects.requireNonNull(etShortDescription.getText()).toString(),
                    Objects.requireNonNull(etFullDescription.getText()).toString(),
                    civSmall.getDrawable(), civBig.getDrawable(), sLocation,
                    tvLocation.getText().toString()));

            if (isEdit) {
                PUTShopDetails();
            } else {
                startActivity(new Intent(RegisterShopActivity.this,
                        OperatingHoursActivity.class));
            }
        }
    }

    public void Small(View view) {
        isBig = false;
        if (civSmall.getDrawable() != null) {
            ShowDPEditPopup(true, true);
        } else {
            ShowDPEditPopup(false, true);
        }
    }

    public void big(View view) {
        isBig = true;
        if (civBig.getDrawable() != null) {
            ShowDPEditPopup(true, false);
        } else {
            ShowDPEditPopup(false, false);
        }
    }

    public void back(View view) {
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

    @Override
    public void onBackPressed() {
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

    public void addLocation(View view) {
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                fieldList).build(this);
        startActivityForResult(intent, 100);
    }

    private void takePicture() {
        Log.d(TAG, "takePicture: Taking picture");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check if there's an app available to take picture
        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "takePicture: getPackageManager()) != null");
            File photo = null;
            photo = createFile(TAG);
            //Save picture into the photo var
            if (photo != null) {
                pathToFile = Objects.requireNonNull(photo).getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(this,
                        "www.ethichadebe.com.loxion_beanery.fileprovider", photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                Log.d(TAG, "takePicture: Start picture taking activity");
                startActivityForResult(intent, CAMERA_PERMISSION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if ((requestCode == STORAGE_PERMISSION) && ((grantResults.length) > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    STORAGE_PERMISSION);
        } else if ((requestCode == CAMERA_PERMISSION) && ((grantResults.length) > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePicture();
        }
    }

    private void PUTShopDetails() {
        ShowLoadingPopup(myDialog, true);
        // loading or check internet connection or something...
        // ... then
        String url = getIpAddress() + "/shops/Register/" + strShopID;
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.PUT, url,
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();

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
                params.put("sSmallPicture", new DataPart("_" + getNewShop().getStrShopName().replace(" ", "_") + ".jpg",
                        getFileDataFromDrawable(getBaseContext(), civSmall.getDrawable()),
                        "image/jpeg"));
                params.put("sBigPicture", new DataPart("_" + getNewShop().getStrShopName().replace(" ", "_") + ".jpg",
                        getFileDataFromDrawable(getBaseContext(), civBig.getDrawable()),
                        "image/jpeg"));

                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }

    public void ShowDPEditPopup(boolean theresPic, boolean isBig) {
        TextView tvCancel, tvMessage, btnYes, btnNo, tvRemove;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        btnNo = myDialog.findViewById(R.id.btnNo);
        tvRemove = myDialog.findViewById(R.id.tvRemove);

        if (theresPic) {
            tvRemove.setVisibility(View.VISIBLE);
        } else {
            tvRemove.setVisibility(View.GONE);
        }
        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvRemove.setOnClickListener(view -> {
            if (isBig) {
                civSmall.setImageDrawable(null);
            } else {
                civBig.setImageDrawable(null);
            }
            myDialog.dismiss();
        });
        btnNo.setText("Open Gallery");
        btnYes.setText("Open Camera");
        tvMessage.setText("Update shop cover picture");

        btnYes.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "ShowDPEditPopup: Take picture");
                takePicture();
                myDialog.dismiss();
            } else {
                myDialog.dismiss();
                requestPermission(this, this, CAMERA_PERMISSION);
            }

        });

        btnNo.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                myDialog.dismiss();
                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),
                        STORAGE_PERMISSION);
            } else {
                myDialog.dismiss();
                requestPermission(this, this, STORAGE_PERMISSION);
            }
        });

        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }
}
