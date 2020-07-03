package www.ethichadebe.com.loxion_beanery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import util.AppHelper;
import util.VolleyMultipartRequest;
import util.VolleySingleton;
import www.ethichadebe.co.za.uploadpicture.UploadImage;

import static util.Constants.getIpAddress;
import static util.HelperMethods.DisplayImage;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.allFieldsEntered;
import static util.HelperMethods.checkData;
import static util.HelperMethods.loadData;
import static util.HelperMethods.saveData;
import static util.HelperMethods.sharedPrefsIsEmpty;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;

public class EditUserProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditUserProfileActivity";
    private MaterialEditText[] mTextBoxes = new MaterialEditText[3];
    private CheckBox mCBMale, mCBFemale, mCBOther;
    private TextView tvNumber, tvEmail;
    private static String UserSex;
    private Dialog myDialog;
    private boolean isBack = true;
    private ImageView[] ivImages = new ImageView[1];
    private UploadImage uploadImage;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private LatLng sLocation;

    /*0 Name
    1 Surname
    2 DOB*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        myDialog = new Dialog(this);
        //TextView
        tvNumber = findViewById(R.id.tvNumber);
        tvEmail = findViewById(R.id.tvEmail);

        //Text boxes
        mTextBoxes[0] = findViewById(R.id.txtName);
        mTextBoxes[1] = findViewById(R.id.txtSurname);
        mTextBoxes[2] = findViewById(R.id.tvAddress);

        //CheckBoxes
        mCBMale = findViewById(R.id.cbMale);
        mCBFemale = findViewById(R.id.cbFemale);
        mCBOther = findViewById(R.id.cbOther);

        ivImages[0] = findViewById(R.id.civProfilePicture);

        mTextBoxes[0].setText(getUser().getuName());
        mTextBoxes[1].setText(getUser().getuSurname());
        mTextBoxes[2].setText(getUser().getuAddress());
        tvEmail.setText(getUser().getuEmail());
        tvNumber.setText(getUser().getuNumber());
        DisplayImage(ivImages[0], getUser().getuPicture());

        uploadImage = new UploadImage(this, this, getPackageManager(), myDialog, ivImages,
                "www.ethichadebe.com.loxion_beanery", TAG);
        switch (getUser().getuSex()) {
            case "male":
                mCBMale.setChecked(true);
                break;
            case "female":
                mCBFemale.setChecked(true);
                break;
            case "other":
                mCBOther.setChecked(true);
                break;
        }

        //Initialize maps places
        Places.initialize(this, getResources().getString(R.string.google_maps_api_key));

        //Date p[icker
        mTextBoxes[2].setOnClickListener(view -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                    fieldList).build(this);
            startActivityForResult(intent, 100);
        });

        setCheck(getUser().getuSex());

        //Handling Checkbox click events
        mCBMale.setOnClickListener(view -> {
            if (mCBMale.isChecked()) {
                mCBFemale.setChecked(false);
                mCBOther.setChecked(false);
                UserSex = "male";
            } else {
                mCBMale.setChecked(true);
            }
        });
        mCBOther.setOnClickListener(view -> {
            if (mCBOther.isChecked()) {
                mCBMale.setChecked(false);
                mCBFemale.setChecked(false);
                UserSex = "other";
            } else {
                mCBOther.setChecked(true);
            }
        });
        mCBFemale.setOnClickListener(view -> {
            if (mCBFemale.isChecked()) {
                mCBMale.setChecked(false);
                mCBOther.setChecked(false);
                UserSex = "female";
            } else {
                mCBFemale.setChecked(true);
            }
        });

    }

    //Editing profile
    private void saveProfileAccount() {
        ShowLoadingPopup(myDialog, true);
        // loading or check internet connection or something...
        // ... then
        String url = getIpAddress() + "/users/EditProfile";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.PUT, url,
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    String resultResponse = new String(response.data);
                    try {
                        JSONObject JSONData = new JSONObject(resultResponse);
                        if (JSONData.getString("data").equals("saved")) {
                            JSONArray jsonArray = new JSONArray(JSONData.getString("response"));
                            JSONObject JSONResponse = jsonArray.getJSONObject(0);

                            Toast.makeText(EditUserProfileActivity.this, "Saved",
                                    Toast.LENGTH_LONG).show();
                            getUser().setuAddress(JSONResponse.getString("uAddress"));
                            getUser().setuLocation(new LatLng(JSONResponse.getDouble("uLatitude"), JSONResponse.getDouble("uLongitude")));
                            getUser().setuEmail(JSONResponse.getString("uEmail"));
                            getUser().setuID(JSONResponse.getInt("uID"));
                            getUser().setuName(JSONResponse.getString("uName"));
                            getUser().setuNumber(JSONResponse.getString("uNumber"));
                            getUser().setuSex(JSONResponse.getString("uSex"));
                            getUser().setuSurname(JSONResponse.getString("uSurname"));
                            getUser().setuPicture(JSONResponse.getString("uPicture"));
                            if (isBack) {
                                setIntFragment(2);
                                startActivity(new Intent(EditUserProfileActivity.this,
                                        MainActivity.class));
                            }//Check if user pressed back
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
            Log.d(TAG, "saveProfileAccount: " + errorMessage);
            Toast.makeText(this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uName", Objects.requireNonNull(mTextBoxes[0].getText()).toString());
                params.put("uSurname", Objects.requireNonNull(mTextBoxes[1].getText()).toString());
                params.put("uDOB", Objects.requireNonNull(mTextBoxes[2].getText()).toString());
                params.put("uLatitude", String.valueOf(sLocation.latitude));
                params.put("uLongitude", String.valueOf(sLocation.longitude));
                params.put("uSex", UserSex);
                params.put("uID", String.valueOf(getUser().getuID()));
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("ProfilePicture", new DataPart("_" + getUser().getuName() + ".jpg",
                        AppHelper.getFileDataFromDrawable(getBaseContext(), ivImages[0].getDrawable()),
                        "image/jpeg"));

                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }

    private void setCheck(String Checked) {
        UserSex = Checked;
        switch (Checked) {
            case "mase":
                mCBMale.setChecked(true);
                break;
            case "female":
                mCBFemale.setChecked(true);
                break;
            case "other":
                mCBOther.setChecked(true);
                break;
        }
    }

    private void EditUserEmail(MaterialEditText number, TextView tvEdit, Dialog myDialog) {
        stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/users/EditEmail",
                response -> {
                    Toast.makeText(EditUserProfileActivity.this, response, Toast.LENGTH_LONG).show();
                    tvEdit.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
                    try {
                        JSONObject JSONData = new JSONObject(response);

                        switch (JSONData.getString("data")) {
                            case "email":
                                number.setError("Already exists");
                                break;
                            case "saved":
                                myDialog.dismiss();
                                JSONArray jsonArray = new JSONArray(JSONData.getString("response"));
                                JSONObject JSONResponse = jsonArray.getJSONObject(0);
                                Toast.makeText(EditUserProfileActivity.this, "Saved", Toast.LENGTH_LONG).show();
                                getUser().setuEmail(JSONResponse.getString("uEmail"));
                                tvEmail.setText(getUser().getuEmail());
                                break;
                            default:
                                Toast.makeText(EditUserProfileActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            tvEdit.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("uEmail", Objects.requireNonNull(number.getText()).toString());
                params.put("uID", String.valueOf(getUser().getuID()));

                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    //Editing email and number
    private void EditUserNumber(MaterialEditText number, TextView tvEdit, Dialog myDialog) {
        stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/users/EditNumber",
                response -> {
                    Toast.makeText(EditUserProfileActivity.this, response, Toast.LENGTH_LONG).show();
                    tvEdit.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
                    try {
                        JSONObject JSONData = new JSONObject(response);

                        switch (JSONData.getString("data")) {
                            case "number":
                                number.setError("Already exists");
                                break;
                            case "saved":
                                myDialog.dismiss();
                                JSONArray jsonArray = new JSONArray(JSONData.getString("response"));
                                JSONObject JSONResponse = jsonArray.getJSONObject(0);
                                Toast.makeText(EditUserProfileActivity.this, "Saved", Toast.LENGTH_LONG).show();
                                getUser().setuNumber(JSONResponse.getString("uNumber"));
                                tvNumber.setText(getUser().getuNumber());
                                if (!sharedPrefsIsEmpty(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), getUser(), true);
                                    }
                                }
                                break;
                            default:
                                Toast.makeText(EditUserProfileActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            tvEdit.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("uNumber", Objects.requireNonNull(number.getText()).toString());
                params.put("uID", String.valueOf(getUser().getuID()));

                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    public void editEmail(View view) {
        ShowEditPopup("Edit email", "Email", getUser().getuEmail(), false);
    }

    public void editNumber(View view) {
        ShowEditPopup("Edit number", "Number", getUser().getuNumber(), true);
    }

    public void ShowEditPopup(String tvHeading1, String etHint, String etValue, boolean isNum) {
        myDialog.setContentView(R.layout.popup_edit_text);

        MaterialEditText etExtra = myDialog.findViewById(R.id.etExtra);
        CardView cvEditOption = myDialog.findViewById(R.id.cvEditOption);
        TextView tvCancel = myDialog.findViewById(R.id.tvCancel);
        TextView tvHeading = myDialog.findViewById(R.id.tvHeading);
        TextView tvEdit = myDialog.findViewById(R.id.tvEdit);

        tvHeading.setText(tvHeading1);
        etExtra.setText(etValue);
        etExtra.setHint(etHint);
        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        cvEditOption.setOnClickListener(view -> {
            if (Objects.requireNonNull(etExtra.getText()).toString().isEmpty()) {
                etExtra.setError("Required");
            } else {
                tvEdit.setBackground(getResources().getDrawable(R.drawable.ripple_effect_disable));
                if (isNum) {
                    EditUserNumber(etExtra, tvEdit, myDialog);
                } else {
                    EditUserEmail(etExtra, tvEdit, myDialog);
                }
                //PUTExtra(position, Objects.requireNonNull(etExtra.getText()).toString());
            }
        });

        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    public void ChangePassword(View view) {
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    //Done

    /**
     * trying to exit before saving
     */
    public void ShowConfirmationPopup() {
        TextView tvCancel, tvMessage, btnYes, btnNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        btnNo = myDialog.findViewById(R.id.btnNo);
        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("Would you like to save Changes made?");

        btnYes.setOnClickListener(view -> {
            if (allFieldsEntered(mTextBoxes)) {
                saveProfileAccount();
            }
        });

        btnNo.setOnClickListener(view -> finish());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    public void back(View view) {
        back();
    }

    public void save(View view) {
        if (allFieldsEntered(mTextBoxes)) {
            isBack = false;
            saveProfileAccount();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (!(Objects.requireNonNull(mTextBoxes[0].getText()).toString().equals(getUser().getuName())) ||
                !(Objects.requireNonNull(mTextBoxes[1].getText()).toString().equals(getUser().getuSurname())) ||
                !(Objects.requireNonNull(mTextBoxes[2].getText()).toString().equals(getUser().getuAddress())) ||
                !UserSex.equals(getUser().getuSex())) {
            ShowConfirmationPopup();
        } else {
            setIntFragment(2);
            startActivity(new Intent(EditUserProfileActivity.this, MainActivity.class));
        }
    }

    //Changing profile picture
    public void ProfilePicture(View view) {
        uploadImage.start(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uploadImage.onActivityResult(getCacheDir(), requestCode, resultCode, data, 0);
        if ((requestCode == 100) && (resultCode == RESULT_OK)) {
            Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(data));

            mTextBoxes[2].setText(place.getAddress());
            sLocation = place.getLatLng();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(Objects.requireNonNull(data));
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        uploadImage.onRequestPermissionsResult(requestCode, grantResults, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

}
