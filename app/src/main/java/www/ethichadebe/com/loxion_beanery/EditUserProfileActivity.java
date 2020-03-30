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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.Constants;

import static util.HelperMethods.MakeBlack;
import static util.HelperMethods.allFieldsEntered;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class EditUserProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private MaterialEditText[] mTextBoxes = new MaterialEditText[5];
    private TextView tvSave;
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

        tvSave = findViewById(R.id.tvSave);
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
        switch (getUser().getuSex()){
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
            }else {
                mCBMale.setChecked(true);
            }
        });
        mCBOther.setOnClickListener(view -> {
            if (mCBOther.isChecked()) {
                mCBMale.setChecked(false);
                mCBFemale.setChecked(false);
                UserSex = "other";
            }else {
                mCBOther.setChecked(true);
            }
        });
        mCBFemale.setOnClickListener(view -> {
            if (mCBFemale.isChecked()) {
                mCBMale.setChecked(false);
                mCBOther.setChecked(false);
                UserSex = "female";
            }else {
                mCBFemale.setChecked(true);
            }
        });

        tvSave.setOnClickListener(view -> {
            for (int i = 0; i < mTextBoxes.length; i++) {
                if (!allFieldsEntered(mTextBoxes, getResources().getColor(R.color.Red), getResources().getColor(R.color.Black))) {
                    mViewError.setText(R.string.enter_all_required_details);
                }else if (allFieldsEntered(mTextBoxes, getResources().getColor(R.color.Red), getResources().getColor(R.color.Black))) {
                    startActivity(new Intent(EditUserProfileActivity.this, MainActivity.class));
                }
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

    /*private void PUTMinutes() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                "http://" + Constants.getIP() + "/team28/public/api/p_member/" + user.getStrID() + "/meeting/" + meeting.getStrMeetID(),
                response -> {
                    //myDialog.dismiss();
                }, error -> {
            //myDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("M_minutes", mEditMinutes.getText().toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/
}
