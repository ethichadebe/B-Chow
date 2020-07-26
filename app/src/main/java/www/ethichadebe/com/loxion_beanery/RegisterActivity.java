package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import util.User;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.allFieldsEntered;
import static util.HelperMethods.isEmail;
import static util.HelperMethods.saveData;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private RequestQueue requestQueue;
    private RelativeLayout rellay1;
    private Dialog myDialog;
    private static User newUser;
    private LatLng sLocation;

    public static User getNewUser() {
        return newUser;
    }

    /*  0 Name
        1 Surname
        2 Number
        3 Email
        4 Password
        5 CPassword
        6 Address*/
    private MaterialEditText[] mTextBoxes = new MaterialEditText[7];
    private CheckBox mCBMale, mCBFemale, mCBOther;

    private String UserSex;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserSex = "";
        rellay1 = findViewById(R.id.rellay1);
        myDialog = new Dialog(this);
        handler.postDelayed(runnable, 500);

        //Initialize maps places
        Places.initialize(this, getResources().getString(R.string.google_maps_api_key));

        //Textboxes
        mTextBoxes[0] = findViewById(R.id.txtName);
        mTextBoxes[1] = findViewById(R.id.txtSurname);
        mTextBoxes[2] = findViewById(R.id.txtPhone);
        mTextBoxes[3] = findViewById(R.id.txtEmail);
        mTextBoxes[4] = findViewById(R.id.txtPass);
        mTextBoxes[5] = findViewById(R.id.txtCPass);
        mTextBoxes[6] = findViewById(R.id.tvAddress);

        saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), null, false);

        //CheckBoxes
        mCBMale = findViewById(R.id.cbMale);
        mCBFemale = findViewById(R.id.cbFemale);
        mCBOther = findViewById(R.id.cbOther);


        //Change location
        mTextBoxes[6].setOnClickListener(view -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                    fieldList).build(this);
            startActivityForResult(intent, 100);
        });

        //Handling Checkbox click events
        mCBMale.setOnClickListener(view -> {
            UserSex = "male";
            sexIsChecked();
            if (mCBMale.isChecked()) {
                mCBFemale.setChecked(false);
                mCBOther.setChecked(false);
            } else {
                mCBMale.setChecked(true);
            }
        });
        mCBOther.setOnClickListener(view -> {
            UserSex = "other";
            sexIsChecked();
            if (mCBOther.isChecked()) {
                mCBMale.setChecked(false);
                mCBFemale.setChecked(false);
            } else {
                mCBOther.setChecked(true);
            }
        });
        mCBFemale.setOnClickListener(view -> {
            UserSex = "female";
            sexIsChecked();
            if (mCBFemale.isChecked()) {
                mCBMale.setChecked(false);
                mCBOther.setChecked(false);
            } else {
                mCBFemale.setChecked(true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    private void POSTRegister() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getIpAddress() + "/users/CheckStuff",
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject JSONResponse = new JSONObject(response);

                        switch (JSONResponse.getString("data")) {
                            case "both":
                                mTextBoxes[2].setError("Already exists");
                                mTextBoxes[3].setError("Already exists");
                                break;
                            case "number":
                                mTextBoxes[2].setError("Already exists");
                                break;
                            case "email":
                                mTextBoxes[3].setError("Already exists");
                                break;
                            case "Registered":
                                newUser = new User(1, Objects.requireNonNull(mTextBoxes[0].getText()).toString(),
                                        Objects.requireNonNull(mTextBoxes[1].getText()).toString(), Objects.requireNonNull(mTextBoxes[6].getText()).toString(),
                                        sLocation, UserSex, Objects.requireNonNull(mTextBoxes[3].getText()).toString(),
                                        Objects.requireNonNull(mTextBoxes[2].getText()).toString(), 0,
                                        Objects.requireNonNull(mTextBoxes[4].getText()).toString());
                                startActivity(new Intent(this, UserTypeActivity.class));
                                break;
                            default:
                                Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uEmail", Objects.requireNonNull(mTextBoxes[3].getText()).toString());
                params.put("uNumber", Objects.requireNonNull(mTextBoxes[2].getText()).toString());
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    private boolean sexIsChecked() {
        if (UserSex.isEmpty()) {
            mCBMale.setTextColor(Color.argb(255, 255, 23, 23));
            mCBFemale.setTextColor(Color.argb(255, 255, 23, 23));
            mCBOther.setTextColor(Color.argb(255, 255, 23, 23));
            return false;
        }
        mCBMale.setTextColor(getResources().getColor(R.color.Grey));
        mCBFemale.setTextColor(getResources().getColor(R.color.Grey));
        mCBOther.setTextColor(getResources().getColor(R.color.Grey));
        return true;
    }

    private boolean passwordMatches() {
        if (!Objects.requireNonNull(mTextBoxes[4].getText()).toString().equals(Objects.requireNonNull(mTextBoxes[5].getText()).toString())) {
            mTextBoxes[4].setError("Password does not match");
            mTextBoxes[5].setError("Password does not match");
            return false;
        }
        return true;
    }

    public void register(View view) {
        if (isEmail(mTextBoxes[3]) && allFieldsEntered(mTextBoxes) && sexIsChecked() && passwordMatches()) {
            POSTRegister();
        }
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100) && (resultCode == RESULT_OK)) {
            Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(data));

            mTextBoxes[6].setText(place.getAddress());
            sLocation = place.getLatLng();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(Objects.requireNonNull(data));
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}