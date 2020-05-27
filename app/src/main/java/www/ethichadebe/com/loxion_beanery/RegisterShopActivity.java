package www.ethichadebe.com.loxion_beanery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import SingleItem.MyShopItem;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.StringToBitMap;
import static util.HelperMethods.getStringImage;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.setNewShop;
import static www.ethichadebe.com.loxion_beanery.ShopSettingsActivity.isEdit;

public class RegisterShopActivity extends AppCompatActivity {
    private static final String TAG = "RegisterShopActivity";
    private RequestQueue requestQueue;
    private Dialog myDialog;
    private TextView tvName, tvLocation,tvAddress;
    private MaterialEditText etName, etShortDescription, etFullDescription;
    private Boolean isBig, goBack;
    private Button btnNext;
    private ImageView civSmall, civBig;
    private Bitmap bmSmall, bmBig;
    private LinearLayout llLocation;
    private LatLng sLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } //Check if user is logged in

        goBack = false;
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
            if (!getNewShop().getStrLogoBig().equals("no image")) {
                civBig.setImageBitmap(StringToBitMap(getNewShop().getStrLogoBig()));
            }

            if (!getNewShop().getStrLogoSmall().equals("no image")) {
                civSmall.setImageBitmap(StringToBitMap(getNewShop().getStrLogoSmall()));
            }
        }//If user pressed back from Operation Hours activity

        if (isEdit) {
            btnNext.setText("Save");
        }

        if (!Objects.requireNonNull(etName.getText()).toString().isEmpty()) {
            tvName.setText(etName.getText().toString());
        }

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
        Places.initialize(this, getResources().getString(R.string.google_maps_api_key));
    }

    public void ShowPopup() {
        TextView tvCancel, tvMessage;
        Button btnYes, btnNo;
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
                goBack = true;
                PUTShop();
            } else {
                myDialog.dismiss();
                startActivity(new Intent(this, MyShopsActivity.class));
            }
        });

        btnNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void prepareChooser() {
        if (!isBig) {
            Croperino.prepareChooser(this, "Choose Front facing Image", ContextCompat.getColor(this,
                    R.color.colorPrimary));
        } else {
            Croperino.prepareChooser(this, "Choose Shop home image", ContextCompat.getColor(this,
                    R.color.colorPrimary));
        }
    }

    private void prepareCamera() {
        Croperino.prepareCamera(this);
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

        } else {
            switch (requestCode) {
                case CroperinoConfig.REQUEST_TAKE_PHOTO:
                    if (resultCode == Activity.RESULT_OK) {
                        Croperino.runCropImage(CroperinoFileUtil.getTempFile(), this, true, 29,
                                10, R.color.gray, R.color.gray_variant);
                    }
                    break;
                case CroperinoConfig.REQUEST_PICK_FILE:
                    if (resultCode == Activity.RESULT_OK) {
                        CroperinoFileUtil.newGalleryFile(data, this);
                        Croperino.runCropImage(CroperinoFileUtil.getTempFile(), this, true, 28,
                                10, R.color.gray, R.color.gray_variant);
                    }
                    break;
                case CroperinoConfig.REQUEST_CROP_PHOTO:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri i = Uri.fromFile(CroperinoFileUtil.getTempFile());
                        setImage(i);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CroperinoFileUtil.REQUEST_CAMERA) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.CAMERA)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        prepareCamera();
                    }
                }
            }
        } else if (requestCode == CroperinoFileUtil.REQUEST_EXTERNAL_STORAGE) {
            boolean wasReadGranted = false;
            boolean wasWriteGranted = false;

            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        wasReadGranted = true;
                    }
                }
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        wasWriteGranted = true;
                    }
                }
            }

            if (wasReadGranted && wasWriteGranted) {
                prepareChooser();
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
            if (getNewShop() == null) {
                setNewShop(new MyShopItem(etName.getText().toString(),
                        Objects.requireNonNull(etShortDescription.getText()).toString(),
                        Objects.requireNonNull(etFullDescription.getText()).toString(), getStringImage(bmSmall),
                        getStringImage(bmBig), sLocation, tvLocation.getText().toString()));
            }

            if (isEdit) {
                PUTShop();
            } else {
                startActivity(new Intent(RegisterShopActivity.this, OperatingHoursActivity.class));
            }
        }
    }

    public void Small(View view) {
        isBig = false;
        if (CroperinoFileUtil.verifyStoragePermissions(this)) {
            prepareChooser();
        }
    }

    public void big(View view) {
        isBig = true;
        if (CroperinoFileUtil.verifyStoragePermissions(this)) {
            prepareChooser();
        }
    }

    private void setImage(Uri uri) {
        if (isBig) {
            try {
                bmBig = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            civBig.setImageURI(uri);
        } else {
            try {
                bmSmall = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            civSmall.setImageURI(uri);
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
                finish();
            }
        } else {
            if (!Objects.requireNonNull(etName.getText()).toString().isEmpty() ||
                    !Objects.requireNonNull(etFullDescription.getText()).toString().isEmpty() ||
                    !Objects.requireNonNull(etShortDescription.getText()).toString().isEmpty()) {
                ShowPopup();
            } else {
                startActivity(new Intent(this, MyShopsActivity.class));
            }
        }
    }

    private void PUTShop() {
        getNewShop().setStrShopName(Objects.requireNonNull(etName.getText()).toString());
        getNewShop().setStrShortDescript(Objects.requireNonNull(etShortDescription.getText()).toString());
        getNewShop().setStrFullDescript(Objects.requireNonNull(etFullDescription.getText()).toString());

        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/shops/Register/" + getNewShop().getIntID(),
                response -> {
                    Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    if (goBack) {
                        finish();
                    }
                }, error -> {
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            HelperMethods.ShowLoadingPopup(myDialog, false);
            //myDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("sName", etName.getText().toString());
                params.put("sShortDescrption", etShortDescription.getText().toString());
                params.put("sFullDescription", etFullDescription.getText().toString());
                params.put("sSmallPicture", getStringImage(bmSmall));
                params.put("sBigPicture", getStringImage(bmBig));
                params.put("sLatitude", String.valueOf(sLocation.latitude));
                params.put("sLongitude", String.valueOf(sLocation.longitude));
                params.put("sAddress", tvLocation.getText().toString());
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
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
                finish();
            }
        } else {
            if (!Objects.requireNonNull(etName.getText()).toString().isEmpty() ||
                    !Objects.requireNonNull(etFullDescription.getText()).toString().isEmpty() ||
                    !Objects.requireNonNull(etShortDescription.getText()).toString().isEmpty()) {
                ShowPopup();
            } else {
                startActivity(new Intent(this, MyShopsActivity.class));
            }

        }
    }

    public void addLocation(View view) {
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                fieldList).build(this);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
