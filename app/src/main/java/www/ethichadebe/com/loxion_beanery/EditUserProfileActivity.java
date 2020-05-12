package www.ethichadebe.com.loxion_beanery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.HelperMethods;

import static android.provider.Telephony.Carriers.PASSWORD;
import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.allFieldsEntered;
import static util.HelperMethods.saveData;
import static util.HelperMethods.sharedPrefsIsEmpty;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class EditUserProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private MaterialEditText[] mTextBoxes = new MaterialEditText[3];
    private CheckBox mCBMale, mCBFemale, mCBOther;
    private TextView tvNumber, tvEmail;
    private static String UserSex;
    private Dialog myDialog;
    private boolean isBack = true;
    private ImageView civProfilePicture;

    /*0 Name
    1 Surname
    2 DOB*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        myDialog = new Dialog(this);

        //TextView
        tvNumber = findViewById(R.id.tvNumber);
        tvEmail = findViewById(R.id.tvEmail);

        //Textboxes
        mTextBoxes[0] = findViewById(R.id.txtName);
        mTextBoxes[1] = findViewById(R.id.txtSurname);
        mTextBoxes[2] = findViewById(R.id.txtDOB);

        //CheckBoxes
        mCBMale = findViewById(R.id.cbMale);
        mCBFemale = findViewById(R.id.cbFemale);
        mCBOther = findViewById(R.id.cbOther);

        civProfilePicture = findViewById(R.id.civProfilePicture);

        mTextBoxes[0].setText(getUser().getuName());
        mTextBoxes[1].setText(getUser().getuSurname());
        mTextBoxes[2].setText(getUser().getuDOB());
        tvEmail.setText(getUser().getuEmail());
        tvNumber.setText(getUser().getuNumber());

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

        //Date p[icker
        mTextBoxes[2].setOnClickListener(view -> {
            DialogFragment datePickeer = new DatePickerFragment();
            datePickeer.show(getSupportFragmentManager(), "date picker");
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

    public void ShowConfirmationPopup() {
        TextView tvCancel, tvMessage;
        Button btnYes, btnNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        btnNo = myDialog.findViewById(R.id.btnNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("Would you like to save Changes made?");

        btnYes.setOnClickListener(view -> {
            if (allFieldsEntered(mTextBoxes)) {
                EditUserDetails();
            }
        });

        btnNo.setOnClickListener(view -> finish());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);

        String strCurrentDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
        mTextBoxes[2].setText(strCurrentDate);

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

    public void back(View view) {
        if (!(Objects.requireNonNull(mTextBoxes[0].getText()).toString().equals(getUser().getuName())) ||
                !(Objects.requireNonNull(mTextBoxes[1].getText()).toString().equals(getUser().getuSurname())) ||
                !(Objects.requireNonNull(mTextBoxes[2].getText()).toString().equals(getUser().getuDOB())) ||
                !UserSex.equals(getUser().getuSex())) {
            ShowConfirmationPopup();
        } else {
            finish();
        }
    }

    public void save(View view) {
        if (allFieldsEntered(mTextBoxes)) {
            isBack = false;
            EditUserDetails();
        }
    }

    private void EditUserEmail(MaterialEditText number, TextView tvEdit, Dialog myDialog) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void EditUserNumber(MaterialEditText number, TextView tvEdit, Dialog myDialog) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
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
                                        saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), getUser().getuNumber(),
                                                getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString("Password", ""));
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void EditUserDetails() {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/users/EditProfile",
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response);
                        if (JSONData.getString("data").equals("saved")) {
                            JSONArray jsonArray = new JSONArray(JSONData.getString("response"));
                            JSONObject JSONResponse = jsonArray.getJSONObject(0);

                            Toast.makeText(EditUserProfileActivity.this, "Saved", Toast.LENGTH_LONG).show();
                            getUser().setuDOB(JSONResponse.getString("uDOB"));
                            getUser().setuEmail(JSONResponse.getString("uEmail"));
                            getUser().setuID(JSONResponse.getInt("uID"));
                            getUser().setuName(JSONResponse.getString("uName"));
                            getUser().setuNumber(JSONResponse.getString("uNumber"));
                            getUser().setuSex(JSONResponse.getString("uSex"));
                            getUser().setuSurname(JSONResponse.getString("uSurname"));
                            if (isBack) {
                                startActivity(new Intent(EditUserProfileActivity.this, MainActivity.class));
                            }//Check if user pressed back
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            HelperMethods.ShowLoadingPopup(myDialog, false);
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("uName", Objects.requireNonNull(mTextBoxes[0].getText()).toString());
                params.put("uSurname", Objects.requireNonNull(mTextBoxes[1].getText()).toString());
                params.put("uDOB", Objects.requireNonNull(mTextBoxes[2].getText()).toString());
                params.put("uSex", UserSex);
                params.put("uID", String.valueOf(getUser().getuID()));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if (!(Objects.requireNonNull(mTextBoxes[0].getText()).toString().equals(getUser().getuName())) ||
                !(Objects.requireNonNull(mTextBoxes[1].getText()).toString().equals(getUser().getuSurname())) ||
                !(Objects.requireNonNull(mTextBoxes[2].getText()).toString().equals(getUser().getuDOB())) ||
                !UserSex.equals(getUser().getuSex())) {
            ShowConfirmationPopup();
        } else {
            finish();
        }
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

    public void ProfilePicture(View view) {
        if (CroperinoFileUtil.verifyStoragePermissions(this)) {
            prepareChooser();
        }
    }

    public void ChangePassword(View view) {
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    private void prepareChooser() {
        Croperino.prepareChooser(this, "Capture photo...",
                ContextCompat.getColor(this, android.R.color.background_dark));
    }

    private void prepareCamera() {
        Croperino.prepareCamera(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CroperinoConfig.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), this, true, 1, 1, R.color.gray, R.color.gray_variant);
                }
                break;
            case CroperinoConfig.REQUEST_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(data, this);
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), this, true, 1, 1, R.color.gray, R.color.gray_variant);
                }
                break;
            case CroperinoConfig.REQUEST_CROP_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Uri i = Uri.fromFile(CroperinoFileUtil.getTempFile());
                    civProfilePicture.setImageURI(i);
                }
                break;
            default:
                break;
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
}
