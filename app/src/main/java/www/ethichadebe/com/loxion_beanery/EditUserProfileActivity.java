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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import static util.HelperMethods.MakeBlack;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class EditUserProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private MaterialEditText[] mTextBoxes = new MaterialEditText[5];
    private LinearLayout llBack;
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

        llBack = findViewById(R.id.llBack);
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
            }
        });
        mCBOther.setOnClickListener(view -> {
            if (mCBOther.isChecked()) {
                mCBMale.setChecked(false);
                mCBFemale.setChecked(false);
                UserSex = "other";
            }
        });
        mCBFemale.setOnClickListener(view -> {
            if (mCBFemale.isChecked()) {
                mCBMale.setChecked(false);
                mCBOther.setChecked(false);
                UserSex = "female";
            }
        });

        llBack.setOnClickListener(view -> ShowConfirmationPopup());

        tvSave.setOnClickListener(view -> {
            for (int i = 0; i < mTextBoxes.length; i++) {
                if (Objects.requireNonNull(mTextBoxes[i].getText()).toString().isEmpty() || (!mCBMale.isChecked() && !mCBFemale.isChecked() && !mCBOther.isChecked())) {
                    //Check if all text boxes are filled in
                    mViewError.setText("");

                    if (mCBMale.isChecked() || mCBFemale.isChecked() || mCBOther.isChecked()) {
                        mCBMale.setTextColor(getResources().getColor(R.color.Black));
                        mCBFemale.setTextColor(getResources().getColor(R.color.Black));
                        mCBOther.setTextColor(getResources().getColor(R.color.Black));

                    } else {
                        mCBMale.setTextColor(Color.argb(255, 255, 23, 23));
                        mCBFemale.setTextColor(Color.argb(255, 255, 23, 23));
                        mCBOther.setTextColor(Color.argb(255, 255, 23, 23));
                    }

                    MakeBlack(mTextBoxes, i, getResources().getColor(R.color.Black));
                    mViewError.setText(R.string.enter_all_required_details);
                    mTextBoxes[i].setUnderlineColor(getResources().getColor(R.color.Red));
                } else if (Objects.requireNonNull(mTextBoxes[2].getText()).toString().length() != 10) {
                    //Check if username already exists
                    mTextBoxes[2].setUnderlineColor(getResources().getColor(R.color.Red));
                    mViewError.setText("Phone number should be a 10 digit value");
                }
            }

            if ((!Objects.requireNonNull(mTextBoxes[0].getText()).toString().isEmpty()) &&
                    (!Objects.requireNonNull(mTextBoxes[1].getText()).toString().isEmpty()) &&
                    (!Objects.requireNonNull(mTextBoxes[2].getText()).toString().isEmpty()) &&
                    (!Objects.requireNonNull(mTextBoxes[3].getText()).toString().isEmpty()) &&
                    (!Objects.requireNonNull(mTextBoxes[4].getText()).toString().isEmpty()) &&
                    (mCBMale.isChecked() || mCBFemale.isChecked() ||
                            mCBOther.isChecked())) {
                mViewError.setText("");
                MakeBlack(mTextBoxes, 0, getResources().getColor(R.color.Black));
                MakeBlack(mTextBoxes, 1, getResources().getColor(R.color.Black));
                MakeBlack(mTextBoxes, 2, getResources().getColor(R.color.Black));
                MakeBlack(mTextBoxes, 3, getResources().getColor(R.color.Black));
                MakeBlack(mTextBoxes, 4, getResources().getColor(R.color.Black));

                startActivity(new Intent(EditUserProfileActivity.this, MainActivity.class));
                //Manipulate loading and button disabling
                //startActivity(new Intent(RegistrationActivity.this, AcountAuthActivity.class));
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
}
