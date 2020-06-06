package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.AppHelper;
import util.HelperMethods;
import util.VolleyMultipartRequest;
import util.VolleySingleton;

import static util.AppHelper.getFileDataFromDrawable;
import static util.Constants.getIpAddress;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.addZero;
import static util.HelperMethods.allFieldsEntered;
import static util.HelperMethods.combineString;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.getNewShop;
import static www.ethichadebe.com.loxion_beanery.ShopSettingsActivity.isEdit;

public class OperatingHoursActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "OperatingHoursActivity";
    private RequestQueue requestQueue;
    private Dialog myDialog;
    private MaterialEditText[] etOpen = new MaterialEditText[8];
    private MaterialEditText[] etClose = new MaterialEditText[8];
    private TextView[] tvDays = new TextView[8];
    private int[] intBackground = {0, 0, 0, 0, 0, 0, 0, 0};
    private String DayOfWeek, strTimes = "";
    private Button btnNext;
    private boolean goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operating_hours);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } // Check if user is logged in

        goBack = false;
        myDialog = new Dialog(this);
        btnNext = findViewById(R.id.btnNext);

        tvDays[0] = findViewById(R.id.tvMon);
        tvDays[1] = findViewById(R.id.tvTue);
        tvDays[2] = findViewById(R.id.tvWed);
        tvDays[3] = findViewById(R.id.tvThu);
        tvDays[4] = findViewById(R.id.tvFri);
        tvDays[5] = findViewById(R.id.tvSat);
        tvDays[6] = findViewById(R.id.tvSun);
        tvDays[7] = findViewById(R.id.tvPH);

        etOpen[0] = findViewById(R.id.etOpenMon);
        etOpen[1] = findViewById(R.id.etOpenTue);
        etOpen[2] = findViewById(R.id.etOpenWed);
        etOpen[3] = findViewById(R.id.etOpenThu);
        etOpen[4] = findViewById(R.id.etOpenFri);
        etOpen[5] = findViewById(R.id.etOpenSat);
        etOpen[6] = findViewById(R.id.etOpenSun);
        etOpen[7] = findViewById(R.id.etOpenPH);

        etClose[0] = findViewById(R.id.etCloseMon);
        etClose[1] = findViewById(R.id.etCloseTue);
        etClose[2] = findViewById(R.id.etCloseWed);
        etClose[3] = findViewById(R.id.etCloseThu);
        etClose[4] = findViewById(R.id.etCloseFri);
        etClose[5] = findViewById(R.id.etCloseSat);
        etClose[6] = findViewById(R.id.etCloseSun);
        etClose[7] = findViewById(R.id.etClosePH);

        if (getNewShop().getIntID() != -1) {
            String[] strOpHours = getNewShop().getStrOperatingHRS().split(", ");
            for (int i = 0; i < etOpen.length; i++) {
                String[] strOpHours1 = strOpHours[i].split(" - ");
                etOpen[i].setText(strOpHours1[0]);
                etClose[i].setText(strOpHours1[1]);
            }
        }//Set times if they already exist

        dayOnClick(0);
        dayOnClick(1);
        dayOnClick(2);
        dayOnClick(3);
        dayOnClick(4);
        dayOnClick(5);
        dayOnClick(6);
        dayOnClick(7);

        etOnClick(etOpen[0], "o1");
        etOnClick(etOpen[1], "o2");
        etOnClick(etOpen[2], "o3");
        etOnClick(etOpen[3], "o4");
        etOnClick(etOpen[4], "o5");
        etOnClick(etOpen[5], "o6");
        etOnClick(etOpen[6], "o7");
        etOnClick(etOpen[7], "oPH");

        etOnClick(etClose[0], "c1");
        etOnClick(etClose[1], "c2");
        etOnClick(etClose[2], "c3");
        etOnClick(etClose[3], "c4");
        etOnClick(etClose[4], "c5");
        etOnClick(etClose[5], "c6");
        etOnClick(etClose[6], "c7");
        etOnClick(etClose[7], "cPH");

        if (isEdit) {
            btnNext.setText("Save");
            goBack=true;
        }
    }

    private void etOnClick(MaterialEditText etTime, String DayOfWeek) {
        etTime.setOnClickListener(view -> {
            DialogFragment timePicker = new TimePickerFragmaent();
            timePicker.show(getSupportFragmentManager(), "time picker");
            this.DayOfWeek = DayOfWeek;
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        switch (DayOfWeek) {
            case "o1":
                //Toast.makeText(OperatingHoursActivity.this, String.valueOf(hourOfDay).length(), Toast.LENGTH_LONG).show();
                etOpen[0].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etOpen, hourOfDay, minute);
                break;
            case "o2":
                etOpen[1].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etOpen, hourOfDay, minute);
                break;
            case "o3":
                etOpen[2].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etOpen, hourOfDay, minute);
                break;
            case "o4":
                etOpen[3].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etOpen, hourOfDay, minute);
                break;
            case "o5":
                etOpen[4].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etOpen, hourOfDay, minute);
                break;
            case "o6":
                etOpen[5].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etOpen, hourOfDay, minute);
                break;
            case "o7":
                etOpen[6].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etOpen, hourOfDay, minute);
                break;
            case "oPH":
                etOpen[7].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etOpen, hourOfDay, minute);
                break;

            case "c1":
                etClose[0].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etClose, hourOfDay, minute);
                break;
            case "c2":
                etClose[1].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etClose, hourOfDay, minute);
                break;
            case "c3":
                etClose[2].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etClose, hourOfDay, minute);
                break;
            case "c4":
                etClose[3].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etClose, hourOfDay, minute);
                break;
            case "c5":
                etClose[4].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etClose, hourOfDay, minute);
                break;
            case "c6":
                etClose[5].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etClose, hourOfDay, minute);
                break;
            case "c7":
                etClose[6].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etClose, hourOfDay, minute);
                break;
            case "cPH":
                etClose[7].setText(addZero(hourOfDay) + ":" + addZero(minute));
                checkCheckedDays(etClose, hourOfDay, minute);
                break;
        }
    }

    public void next(View view) {
        if (allFieldsEntered(etOpen, etClose)) {
            if (getNewShop().getIntID() == -1) {
                POSTRegisterShop();
            } else {
                PUTShop();
            }
        }
    }

    public void back(View view) {
        if (isEdit) {
            if (!combineString(etOpen, etClose).equals(getNewShop().getStrOperatingHRS())){
                ShowPopup();
            }else {
                finish();
            }
        } else {
            startActivity(new Intent(this, RegisterShopActivity.class));
        }
    }

    private void checkCheckedDays(MaterialEditText[] etClose, int Hour, int Minute) {
        for (int i = 0; i < tvDays.length; i++) {
            if (intBackground[i] == 1) {
                etClose[i].setText(addZero(Hour) + ":" + addZero(Minute));
            }
        }
    }

    private void POSTRegisterShop() {
        ShowLoadingPopup(myDialog, true);
        // loading or check internet connection or something...
        // ... then
        String url = getIpAddress() + "/shops/Register";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    getNewShop().setStrOperatingHRS(strTimes);      //Set Operating hours
                    try {
                        JSONObject JSONResponse = new JSONObject(String.valueOf(response));
                        getNewShop().setIntID(Integer.parseInt(JSONResponse.getString("data")));
                        getNewShop().setStrOperatingHRS(combineString(etOpen, etClose));
                        HelperMethods.ShowLoadingPopup(myDialog, false);
                        startActivity(new Intent(this, IngredientsActivity.class));
                        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(this, "Error: "+ error.toString(), Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sName", getNewShop().getStrShopName());
                params.put("sShortDescrption", getNewShop().getStrShortDescript());
                params.put("sFullDescription", getNewShop().getStrFullDescript());
                params.put("sLatitude", String.valueOf(getNewShop().getLlLocation().latitude));
                params.put("sLongitude", String.valueOf(getNewShop().getLlLocation().longitude));
                params.put("sAddress", getNewShop().getStrAddress());
                params.put("sOperatingHrs", combineString(etOpen, etClose));
                params.put("uID", String.valueOf(getUser().getuID()));
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("sSmallPicture", new DataPart("_"+getNewShop().getStrShopName()+".jpg",
                        getFileDataFromDrawable(getBaseContext(), getNewShop().getDraLogoSmall()),
                        "image/jpeg"));
                params.put("sBigPicture", new DataPart("_"+getNewShop().getStrShopName()+".jpg",
                        getFileDataFromDrawable(getBaseContext(), getNewShop().getDraLogoBig()),
                        "image/jpeg"));

                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }


    private void PUTShop() {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/shops/Register/OH/" + getNewShop().getIntID(),
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    getNewShop().setStrOperatingHRS(strTimes);      //Set Operating hours
                    try {
                        JSONObject JSONResponse = new JSONObject(response);
                        getNewShop().setStrOperatingHRS(combineString(etOpen, etClose));
                        HelperMethods.ShowLoadingPopup(myDialog, false);
                        if (goBack){
                            finish();
                        }else {
                            startActivity(new Intent(OperatingHoursActivity.this, IngredientsActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            //myDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("sOperatingHrs", combineString(etOpen, etClose));
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    private void dayOnClick(int index) {
        tvDays[index].setOnClickListener(view -> {
            if (intBackground[index] == 0) {
                YoYo.with(Techniques.ZoomIn)
                        .duration(400)
                        .repeat(0)
                        .playOn(tvDays[index]);
                tvDays[index].setBackground(getResources().getDrawable(R.drawable.circle_bg_checked));
                tvDays[index].setTextColor(getResources().getColor(R.color.colorPrimary));
                intBackground[index] = 1;
            } else {
                tvDays[index].setBackground(getResources().getDrawable(R.drawable.circle_bg_unchecked));
                tvDays[index].setTextColor(getResources().getColor(R.color.Grey));
                intBackground[index] = 0;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isEdit) {
            if (!combineString(etOpen, etClose).equals(getNewShop().getStrOperatingHRS())){
                ShowPopup();
            }else {
                finish();
            }
        } else {
            startActivity(new Intent(this, RegisterShopActivity.class));
        }
    }

    public void ShowPopup() {
        TextView tvCancel, tvMessage,btnYes, btnNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        btnNo = myDialog.findViewById(R.id.btnNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("Would you like to save changes before exiting?");
        btnYes.setOnClickListener(view -> {
            goBack = true;
            PUTShop();
        });

        btnNo.setOnClickListener(view ->finish());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
