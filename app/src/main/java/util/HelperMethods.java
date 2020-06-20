package util;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rengwuxian.materialedittext.MaterialEditText;
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
import www.ethichadebe.com.loxion_beanery.R;


public class HelperMethods {
    public static final int STORAGE_PERMISSION = 1;
    public static final int CAMERA_PERMISSION = 20;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_REQUEST_CODE = 1234;
    private static boolean mLocationGranted = false;
    public static final String SHARED_PREFS = "sharedPrefs";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
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
            System.out.println(e);
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
            System.out.println(e);
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

    public static void saveData(SharedPreferences sharedPreferences, String Username, String Password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERNAME, Objects.requireNonNull(Username));
        editor.putString(PASSWORD, Objects.requireNonNull(Password));

        editor.apply();
    }

    public static void loadData(SharedPreferences sharedPreferences, MaterialEditText Username,
                                MaterialEditText Password) {
        Password.setText(sharedPreferences.getString(PASSWORD, ""));
        Username.setText(sharedPreferences.getString(USERNAME, ""));
    }

    public static boolean sharedPrefsIsEmpty(SharedPreferences sharedPreferences) {
        return Objects.requireNonNull(sharedPreferences.getString(PASSWORD, "")).isEmpty();
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

    public static void setOHVISIBILITY(LinearLayout llOpHours, TextView tvMore, TextView[] tvDays, String OH) {
        setOHForEachDay(tvDays, OH);
        if (llOpHours.getVisibility() == View.GONE) {
            llOpHours.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.SlideInDown)
                    .duration(500)
                    .repeat(0)
                    .playOn(llOpHours);
            tvMore.setText("Show less");
        } else {
            YoYo.with(Techniques.SlideOutUp)
                    .duration(500)
                    .repeat(0)
                    .playOn(llOpHours);
            llOpHours.setVisibility(View.GONE);
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

    public static void requestPermission(Activity activity, Context context, int permission) {
        if (permission == STORAGE_PERMISSION) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(context)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed so you can gain access to your gallery")
                        .setPositiveButton("Enable permission", (dialogInterface, i) -> {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);

                        })
                        .setNegativeButton("close", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            }
        } else if (permission == CAMERA_PERMISSION) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(context)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed so you can gain access to your camera")
                        .setPositiveButton("Enable permission", (dialogInterface, i) -> {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                    CAMERA_PERMISSION);

                        })
                        .setNegativeButton("close", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, CAMERA_PERMISSION);
            }
        }
    }


    public static void DisplayImage(ImageView imageView, String url) {
        LoadImage loadImage = new LoadImage(imageView);
        loadImage.execute(url);
    }

    public static void displayDatePicker(Context context, TextView tv) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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