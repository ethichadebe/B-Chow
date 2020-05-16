package util;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import SingleItem.IngredientItem;
import SingleItem.IngredientItemCheckbox;
import www.ethichadebe.com.loxion_beanery.IngredientsActivity;
import www.ethichadebe.com.loxion_beanery.LoginActivity;
import www.ethichadebe.com.loxion_beanery.MenuActivity;
import www.ethichadebe.com.loxion_beanery.R;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class HelperMethods {
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_REQUEST_CODE = 1234;
    private static boolean mLocationGranted = false;
    public static final String SHARED_PREFS = "sharedPrefs";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

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

    public static void loadData(SharedPreferences sharedPreferences, MaterialEditText Username, MaterialEditText Password) {
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

    public static String getStringImage(Bitmap image){
        if (image != null){
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
            byte[] imageByte = bOut.toByteArray();
            return Base64.encodeToString(imageByte, Base64.DEFAULT);
        }
        return "no image";
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    /**
     * Set Rating
     *
     * @param rating
     * @param ivStar1
     * @param ivStar2
     * @param ivStar3
     * @param ivStar4
     * @param ivStar5
     */
    public static void setStarRating(int rating, ImageView ivStar1, ImageView ivStar2, ImageView ivStar3, ImageView ivStar4,
                                     ImageView ivStar5) {
        switch (rating) {
            case 0:
                ivStar1.setImageResource(0);
                ivStar2.setImageResource(0);
                ivStar3.setImageResource(0);
                ivStar4.setImageResource(0);
                ivStar5.setImageResource(0);
                break;
            case 1:
                ivStar1.setImageResource(0);
                ivStar2.setImageResource(0);
                ivStar3.setImageResource(0);
                ivStar4.setImageResource(0);
                ivStar5.setVisibility(View.VISIBLE);
                ivStar5.setVisibility(View.VISIBLE);
                break;
            case 2:
                ivStar1.setImageResource(0);
                ivStar2.setImageResource(0);
                ivStar3.setImageResource(0);
                ivStar4.setVisibility(View.VISIBLE);
                ivStar5.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivStar1.setImageResource(0);
                ivStar2.setImageResource(0);
                ivStar3.setVisibility(View.VISIBLE);
                ivStar4.setVisibility(View.VISIBLE);
                ivStar5.setVisibility(View.VISIBLE);
                break;
            case 4:
                ivStar1.setImageResource(0);
                ivStar2.setVisibility(View.VISIBLE);
                ivStar3.setVisibility(View.VISIBLE);
                ivStar4.setVisibility(View.VISIBLE);
                ivStar5.setVisibility(View.VISIBLE);
                break;
            case 5:
                ivStar1.setVisibility(View.VISIBLE);
                ivStar2.setVisibility(View.VISIBLE);
                ivStar3.setVisibility(View.VISIBLE);
                ivStar4.setVisibility(View.VISIBLE);
                ivStar5.setVisibility(View.VISIBLE);
                break;
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
}
