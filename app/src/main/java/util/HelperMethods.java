package util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Objects;

import SingleItem.IngredientItem;
import SingleItem.IngredientItemCheckbox;
import www.ethichadebe.com.loxion_beanery.IngredientsActivity;
import www.ethichadebe.com.loxion_beanery.LoginActivity;
import www.ethichadebe.com.loxion_beanery.MenuActivity;
import www.ethichadebe.com.loxion_beanery.R;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class HelperMethods {
    public static final String SHARED_PREFS = "sharedPrefs";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

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

    public static String combineString(ArrayList<IngredientItemCheckbox> ingredientItems) {
        StringBuilder MenuList = new StringBuilder();
        for (int i = 0; i < ingredientItems.size(); i++) {
            if (ingredientItems.get(i).getChecked())
                MenuList.append(ingredientItems.get(i).getStrIngredientName()).append(", ");
        }

        return String.valueOf(MenuList).substring(0, String.valueOf(MenuList).length() - 2);
    }
    public static String combineString(MaterialEditText[] etOpen, MaterialEditText[] etClose) {
        StringBuilder MenuList = new StringBuilder();
        for (int i = 0; i < etOpen.length; i++) {
            MenuList.append(Objects.requireNonNull(etOpen[i].getText()).toString()).append(" - ").append(Objects.requireNonNull(etClose[i].getText()).toString()).append(", ");
        }

        return String.valueOf(MenuList).substring(0, String.valueOf(MenuList).length() - 2);
    }
}
