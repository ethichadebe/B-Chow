package util;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.rengwuxian.materialedittext.MaterialEditText;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import SingleItem.IngredientItemCheckbox;
import www.ethichadebe.com.loxion_beanery.MainActivity;
import www.ethichadebe.com.loxion_beanery.R;


public class HelperMethods extends AppCompatActivity {
    private static final String TAG = "HelperMethods";
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_REQUEST_CODE = 1234;
    private static boolean mLocationGranted = false;
    public static final String SHARED_PREFS = "sharedPrefs";
    private static final String U_NUMBER = "uNumber";
    private static final String U_ID = "uID";
    public static final int REQUEST_CHECK_SETTINGS = 200;
    private static final String U_NAME = "uName";
    private static final String U_SURNAME = "uSurname";
    private static final String U_ADDRESS = "uAddress";
    private static final String U_LONGITUDE = "uLongitude";
    private static final String U_LATITUDE = "uLatitude";
    private static final String U_SEX = "uSex";
    private static final String U_EMAIL = "uEmail";
    private static final String U_PICTURE = "uPicture";
    private static final String U_TYPE = "uType";
    private static final String REMEMBER_ME = "rememberMe";
    private static DatePickerDialog.OnDateSetListener dateSetListener;

    public static boolean ismLocationGranted() {
        return mLocationGranted;
    }

    public static void setmLocationGranted(boolean mLocationGranted) {
        HelperMethods.mLocationGranted = mLocationGranted;
    }

    public static void ShowLoadingPopup(final Dialog myDialog, boolean show) {
        myDialog.setContentView(R.layout.popup_loading);
        handler(myDialog.findViewById(R.id.vLine), myDialog.findViewById(R.id.vLineGrey));
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (show) {
            myDialog.show();
        } else {
            myDialog.dismiss();
        }
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    private static Runnable LoaderMotion(final View vLine) {
        return () -> YoYo.with(Techniques.ZoomInRight)
                .duration(1000)
                .repeat(5000)
                .playOn(vLine);
    }

    public static void handler(View vLine, View vLineGrey) {
        Handler handler = new Handler();
        handler.postDelayed(LoaderMotion(vLine), 0);
        handler.postDelayed(LoaderMotion(vLineGrey), 500);
    }

    /**
     * Only display finish button when there's a menu item added
     */
    public static void ButtonVisibility(ArrayList list, Button btn) {
        if (list.isEmpty()) {
            btn.setVisibility(View.GONE);
        } else {
            btn.setVisibility(View.VISIBLE);
        }
    }

    public static String convertedTime(String inputTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        try {
            Date date = sdf.parse(inputTime);
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            // assuming a timezone in India
            outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
            return outputFormat.format(date);
        } catch (ParseException e) {
            Log.d(TAG, "convertedTime: " + e.toString());
        }
        return "";
    }

    public static String convertedDateTime(String inputTime) {
        String outputTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        try {
            Date date = sdf.parse(inputTime);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            // assuming a timezone in India
            outputTime = outputFormat.format(date);
            outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        } catch (ParseException e) {
            Log.d(TAG, "convertedDateTime: " + e.toString());
        }
        return outputTime;
    }

    public static boolean allFieldsEntered(MaterialEditText[] mText) {
        boolean allEntered = true;
        for (MaterialEditText materialEditText : mText) {
            if (Objects.requireNonNull(materialEditText.getText()).toString().isEmpty()) {
                materialEditText.setError("Required field");
                allEntered = false;
            }
        }
        return allEntered;
    }

    public static boolean allFieldsEntered(MaterialEditText[] mText, MaterialEditText[] mText2) {
        boolean allEntered = true;
        for (MaterialEditText materialEditText : mText) {
            if (Objects.requireNonNull(materialEditText.getText()).toString().isEmpty()) {
                materialEditText.setError("Required field");
                allEntered = false;
            }
        }
        for (MaterialEditText materialEditText : mText2) {
            if (Objects.requireNonNull(materialEditText.getText()).toString().isEmpty()) {
                materialEditText.setError("Required field");
                allEntered = false;
            }
        }
        return allEntered;
    }

    public static void saveData(SharedPreferences sharedPreferences, User user, boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(REMEMBER_ME, rememberMe);
        if (user != null) {
            editor.putInt(U_ID, user.getuID());
            editor.putString(U_NAME, Objects.requireNonNull(user.getuName()));
            editor.putString(U_SURNAME, Objects.requireNonNull(user.getuSurname()));
            editor.putString(U_ADDRESS, Objects.requireNonNull(user.getuAddress()));
            editor.putString(U_LATITUDE, String.valueOf(user.getuLocation().latitude));
            editor.putString(U_LONGITUDE, String.valueOf(user.getuLocation().longitude));
            editor.putString(U_SEX, Objects.requireNonNull(user.getuSex()));
            editor.putString(U_EMAIL, Objects.requireNonNull(user.getuEmail()));
            editor.putString(U_NUMBER, Objects.requireNonNull(user.getuNumber()));
            editor.putString(U_PICTURE, Objects.requireNonNull(user.getuPicture()));
            editor.putInt(U_TYPE, user.getuType());

        }
        editor.apply();
    }

    public void getDeviceLocation(Context context, Activity activity, FusedLocationProviderClient mFusedLocationProviderClient) {
        Log.d(TAG, "PostLogin getDeviceLocation: getting current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(context));

        try {
            if (ismLocationGranted()) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "PostLogin onComplete: location found");
                        if (task.getResult() == null) {
                            Log.d(TAG, "PostLogin turnOnLocation: location is null");
                            //turnOnLocation(context, activity);
                        } else {
                            Location currentLocation = (Location) task.getResult();
                            //      userLocation = new LatLng(Objects.requireNonNull(currentLocation).getLatitude(),currentLocation.getLongitude());
                        }

                    } else {
                        Log.d(TAG, "PostLogin onComplete: Unable to get location");
                        Toast.makeText(context, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security exception " + e.getMessage());
        }
    }

    public static User loadData(SharedPreferences sharedPreferences) {
        return new User(sharedPreferences.getInt(U_ID, 0), sharedPreferences.getString(U_NAME, ""), sharedPreferences.getString(U_SURNAME, ""),
                sharedPreferences.getString(U_ADDRESS, ""),
                new LatLng(Double.parseDouble(Objects.requireNonNull(sharedPreferences.getString(U_LONGITUDE, ""))),
                        Double.parseDouble(Objects.requireNonNull(sharedPreferences.getString(U_LATITUDE, "")))),
                sharedPreferences.getString(U_SEX, ""), sharedPreferences.getString(U_EMAIL, ""), sharedPreferences.getString(U_NUMBER, ""),
                sharedPreferences.getString(U_PICTURE, ""), sharedPreferences.getInt(U_TYPE, 0));
    }

    public static boolean checkData(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(REMEMBER_ME, false);
    }

    public static boolean sharedPrefsIsEmpty(SharedPreferences sharedPreferences) {
        return Objects.requireNonNull(sharedPreferences.getString(U_NAME, "")).isEmpty();
    }

    public static String combineString(ArrayList<IngredientItemCheckbox> ingredientItems) {
        String MenuList = "";
        for (int i = 0; i < ingredientItems.size(); i++) {
            if (ingredientItems.get(i).getChecked()) {
                if (!ingredientItems.get(i).getStrIngredientName().equals("")) {
                    MenuList += ingredientItems.get(i).getStrIngredientName() + ", ";
                }
            }
        }

        if (MenuList.isEmpty()) {
            return "";
        } else {
            return MenuList.substring(0, MenuList.length() - 2);
        }
    }

    public static String combineString(String menu) {
        return menu.substring(0, menu.length() - 2);
    }

    public static String combineString(MaterialEditText[] etOpen, MaterialEditText[] etClose) {
        String MenuList = "";
        for (int i = 0; i < etOpen.length; i++) {
            if (!Objects.requireNonNull(etOpen[i].getText()).toString().isEmpty()) {
                MenuList += Objects.requireNonNull(etOpen[i].getText()).toString() + " - ";
                if (!Objects.requireNonNull(etClose[i].getText()).toString().isEmpty()) {
                    MenuList += Objects.requireNonNull(etClose[i].getText()).toString() + ", ";
                }
            }
        }

        if (MenuList.isEmpty()) {
            return "";
        } else {
            return MenuList.substring(0, MenuList.length() - 2);
        }
    }

    public static void setOHVISIBILITY(ExpandableLayout expandableLayout, TextView tvMore, TextView[] tvDays, String OH) {
        setOHForEachDay(tvDays, OH);
        if (!expandableLayout.isExpanded()) {
            expandableLayout.expand();
            tvMore.setText("Show less");
        } else {
            expandableLayout.collapse();
            tvMore.setText("Show more");
        }
    }

    private static void setOHForEachDay(TextView[] tvDays, String strOH) {
        String[] strOpHours = strOH.split(", ");

        for (int i = 0; i < tvDays.length; i++) {
            tvDays[i].setText(strOpHours[i]);
        }
    }

    public static String addZero(int string) {
        if (String.valueOf(string).length() == 1) {
            return "0" + string;
        }
        return "" + string;

    }

    public static double roundOf(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static Boolean randomNumber(int max) {
        return ((int) (Math.random() * max) + 1) == 1;
    }

    public static void DisplayImage(ImageView imageView, String url) {
        try {
            LoadImage loadImage = new LoadImage(imageView);
            loadImage.execute(url);
        }catch (Exception e){
            Log.d(TAG, "DisplayImage: " + e.toString());
        }
    }

    public static void displayDatePicker(Context context, TextView tv) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener, year, month, day);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dateSetListener = (datePicker, year1, month1, day1) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, month1);
            calendar.set(Calendar.DAY_OF_MONTH, day1);

            String strCurrentDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
            tv.setText(strCurrentDate);
        };
    }

}