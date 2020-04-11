package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import static util.Constants.getIpAddress;
import static util.HelperMethods.allFieldsEntered;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class EditUserProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private MaterialEditText[] mTextBoxes = new MaterialEditText[5];
    private CheckBox mCBMale, mCBFemale, mCBOther;
    private TextView mViewError;
    private static String UserSex;
    private Dialog myDialog;

    /*0 Name
1 Surname
2 Number
3 Email
4 DOB*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        if (getUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

        myDialog = new Dialog(this);

        //Labels
        mViewError = findViewById(R.id.lblError);

        //Textboxes
        mTextBoxes[0] = findViewById(R.id.txtName);
        mTextBoxes[1] = findViewById(R.id.txtSurname);
        mTextBoxes[2] = findViewById(R.id.txtPhone);
        mTextBoxes[3] = findViewById(R.id.txtEmail);
        mTextBoxes[4] = findViewById(R.id.txtDOB);

        //CheckBoxes
        mCBMale = findViewById(R.id.cbMale);
        mCBFemale = findViewById(R.id.cbFemale);
        mCBOther = findViewById(R.id.cbOther);

        mTextBoxes[0].setText(getUser().getuName());
        mTextBoxes[1].setText(getUser().getuSurname());
        mTextBoxes[2].setText(getUser().getuNumber());
        mTextBoxes[3].setText(getUser().getuEmail());
        mTextBoxes[4].setText(getUser().getuDOB());
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
        mTextBoxes[4].setOnClickListener(view -> {
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
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("All entered information will be lost\nAre you sure?");

        cvYes.setOnClickListener(view -> {
            myDialog.dismiss();
            finish();
        });

        cvNo.setOnClickListener(view -> myDialog.dismiss());
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
        mTextBoxes[4].setText(strCurrentDate);

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
        ShowConfirmationPopup();
    }

    public void save(View view) {
        boolean allIsWell = false;
        for (int i = 0; i < mTextBoxes.length; i++) {
            if (!allFieldsEntered(mTextBoxes, getResources().getColor(R.color.Red), getResources().getColor(R.color.Black))) {
                mViewError.setText(R.string.enter_all_required_details);
            } else if (allFieldsEntered(mTextBoxes, getResources().getColor(R.color.Red), getResources().getColor(R.color.Black))) {
                allIsWell = true;
            }
        }

        if (allIsWell) {
            EditUserRecord();
        }
    }

    private void EditUserRecord() {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                "http://" + getIpAddress() + "/users/EditProfile",
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response);
                        if (JSONData.getString("data").equals("saved")) {
                            JSONArray jsonArray = new JSONArray(JSONData.getString("response"));
                            JSONObject JSONResponse = jsonArray.getJSONObject(0);

                            switch (JSONData.getString("data")) {
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
                                case "saved":
                                    Toast.makeText(EditUserProfileActivity.this, "Saved", Toast.LENGTH_LONG).show();
                                    getUser().setuDOB(JSONResponse.getString("uDOB"));
                                    getUser().setuEmail(JSONResponse.getString("uEmail"));
                                    getUser().setuID(JSONResponse.getInt("uID"));
                                    getUser().setuName(JSONResponse.getString("uName"));
                                    getUser().setuNumber(JSONResponse.getString("uNumber"));
                                    getUser().setuSex(JSONResponse.getString("uSex"));
                                    getUser().setuSurname(JSONResponse.getString("uSurname"));
                                    startActivity(new Intent(EditUserProfileActivity.this, MainActivity.class));
                                    break;
                                default:
                                    Toast.makeText(EditUserProfileActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            HelperMethods.ShowLoadingPopup(myDialog, false);
            Toast.makeText(EditUserProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                if (!getUser().getuNumber().equals(Objects.requireNonNull(mTextBoxes[2].getText()).toString()) &&
                        !getUser().getuEmail().equals(Objects.requireNonNull(mTextBoxes[3].getText()).toString())) {
                    params.put("uNumber", Objects.requireNonNull(mTextBoxes[2].getText()).toString());
                    params.put("uEmail", Objects.requireNonNull(mTextBoxes[3].getText()).toString());
                } else if (!getUser().getuEmail().equals(Objects.requireNonNull(mTextBoxes[3].getText()).toString())) {
                    params.put("uEmail", Objects.requireNonNull(mTextBoxes[3].getText()).toString());
                } else if (!getUser().getuNumber().equals(Objects.requireNonNull(mTextBoxes[2].getText()).toString())) {
                    params.put("uNumber", Objects.requireNonNull(mTextBoxes[2].getText()).toString());
                }
                params.put("uName", Objects.requireNonNull(mTextBoxes[0].getText()).toString());
                params.put("uSurname", Objects.requireNonNull(mTextBoxes[1].getText()).toString());
                params.put("uDOB", Objects.requireNonNull(mTextBoxes[4].getText()).toString());
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
        ShowConfirmationPopup();    }
}
