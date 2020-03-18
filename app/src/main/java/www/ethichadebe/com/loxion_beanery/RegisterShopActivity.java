package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import SingleItem.MyShopItem;

public class RegisterShopActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private MaterialEditText txtName, txtShortDescription, txtFullDescription, etOpen, etClose;
    private LinearLayout[] llDay = new LinearLayout[8];
    private View[] vDayLine = new View[8];
    private String[] strTimes = new String[8];
    private Dialog myDialog;
    private Boolean isOpen;
    private Button btnNext;
    private LinearLayout llBack;
    private RelativeLayout rlPicture;
    private TextView tvHeading, lblError;
    private CardView cvAdd;
    private static MyShopItem newShop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);

        llBack = findViewById(R.id.llBack);
        btnNext = findViewById(R.id.btnNext);
        rlPicture = findViewById(R.id.rlPicture);
        tvHeading = findViewById(R.id.tvHeading);
        cvAdd = findViewById(R.id.cvAdd);
        lblError = findViewById(R.id.lblError);
        etOpen = findViewById(R.id.etOpen);
        etClose = findViewById(R.id.etClose);

        llDay[0] = findViewById(R.id.llMon);
        llDay[1] = findViewById(R.id.llTue);
        llDay[2] = findViewById(R.id.llWed);
        llDay[3] = findViewById(R.id.llThu);
        llDay[4] = findViewById(R.id.llFri);
        llDay[5] = findViewById(R.id.llSat);
        llDay[6] = findViewById(R.id.llSun);
        llDay[7] = findViewById(R.id.llPH);
        vDayLine[0] = findViewById(R.id.llMonLine);
        vDayLine[1] = findViewById(R.id.llTueLine);
        vDayLine[2] = findViewById(R.id.llWedLine);
        vDayLine[3] = findViewById(R.id.llThuLine);
        vDayLine[4] = findViewById(R.id.llFriLine);
        vDayLine[5] = findViewById(R.id.llSatLine);
        vDayLine[6] = findViewById(R.id.llSunLine);
        vDayLine[7] = findViewById(R.id.llPHLine);

        myDialog = new Dialog(this);
        txtName = findViewById(R.id.txtName);
        txtShortDescription = findViewById(R.id.txtShortDescription);
        txtFullDescription = findViewById(R.id.txtFullDescription);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.requireNonNull(txtName.getText()).toString().isEmpty() || !txtFullDescription.getText().toString().isEmpty() ||
                        !txtShortDescription.getText().toString().isEmpty()) {
                    ShowPopup();
                } else {
                    finish();
                }
            }
        });

        etOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragmaent();
                timePicker.show(getSupportFragmentManager(), "time picker");
                isOpen = true;
            }
        });

        etClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragmaent();
                timePicker.show(getSupportFragmentManager(), "time picker");
                isOpen = false;
            }
        });

        setHighlight(0);
        llDay[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighlight(0);
            }
        });
        llDay[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighlight(1);
            }
        });
        llDay[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighlight(2);
            }
        });
        llDay[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighlight(3);
            }
        });
        llDay[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighlight(4);
            }
        });
        llDay[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighlight(5);
            }
        });
        llDay[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighlight(6);
            }
        });
        llDay[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighlight(7);
            }
        });

        cvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOpen.getText().toString().isEmpty() && etClose.getText().toString().isEmpty()) {
                    etOpen.setUnderlineColor(getResources().getColor(R.color.Red));
                    etClose.setUnderlineColor(getResources().getColor(R.color.Red));

                } else if (!etOpen.getText().toString().isEmpty() && etClose.getText().toString().isEmpty()) {
                    etOpen.setUnderlineColor(getResources().getColor(R.color.Black));
                    etClose.setUnderlineColor(getResources().getColor(R.color.Red));

                } else if (etOpen.getText().toString().isEmpty() && !etClose.getText().toString().isEmpty()) {
                    etClose.setUnderlineColor(getResources().getColor(R.color.Black));
                    etOpen.setUnderlineColor(getResources().getColor(R.color.Red));
                } else {
                    etClose.setUnderlineColor(getResources().getColor(R.color.Black));
                    etOpen.setUnderlineColor(getResources().getColor(R.color.Black));

                    int intNextPos = 0;         //index to move the highlighted date
                    for (int i = 0; i < 8; i++) {
                        if (vDayLine[i].getVisibility() == View.VISIBLE) {
                            strTimes[i] = etOpen.getText().toString() + "-" + etClose.getText().toString();

                            if (i == 7){
                                intNextPos = 0;
                            }else {
                                intNextPos  = i + 1;
                            }
                        }
                    }
                    setHighlight(intNextPos);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtName.getText().toString().isEmpty()) {
                    txtName.setUnderlineColor(getResources().getColor(R.color.Red));
                } else {
                    txtName.setUnderlineColor(getResources().getColor(R.color.Black));
                    newShop = new MyShopItem(1,txtName.getText().toString(),1,
                            txtShortDescription.getText().toString(),txtFullDescription.getText().toString(),
                            new Location(""),"0",0);

                    startActivity(new Intent(RegisterShopActivity.this, IngredientsActivity.class));
                }
            }
        });
    }

    public static MyShopItem getNewShop() {
        return newShop;
    }

    public void ShowPopup() {
        TextView tvCancel, tvMessage;
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        tvMessage.setText("All entered information will be lost\nAre you sure?");

        cvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                finish();
            }
        });

        cvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void setHighlight(int vDayLine) {
        for (int i = 0; i < 8; i++) {
            if (vDayLine == i) {
                this.vDayLine[i].setVisibility(View.VISIBLE);
                DisplayTime(i);
            } else {
                this.vDayLine[i].setVisibility(View.GONE);
            }
        }
    }

    //Display time for specific day
    private void DisplayTime(int intDay){
        etClose.setUnderlineColor(getResources().getColor(R.color.Black));
        etOpen.setUnderlineColor(getResources().getColor(R.color.Black));
        if (strTimes[intDay] != null){
            String[] times = strTimes[intDay].split("-");
            etOpen.setText(times[0]);
            etClose.setText(times[1]);
        }else{
            etOpen.setText("");
            etClose.setText("");
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        if (isOpen){
            etOpen.setText(hourOfDay + ":" + minute);
        }else {
            etClose.setText(hourOfDay + ":" + minute);
        }
    }
}
