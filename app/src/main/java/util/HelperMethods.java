package util;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Objects;

import www.ethichadebe.com.loxion_beanery.R;

public class HelperMethods {

    public static void ShowLoadingPopup(final Dialog myDialog, boolean show) {
        myDialog.setContentView(R.layout.popup_loading);
        handler(myDialog.findViewById(R.id.vLine), myDialog.findViewById(R.id.vLineGrey));
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (show) {
            myDialog.show();
        } else {
            myDialog.dismiss();
        }
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    public static Runnable LoaderMotion(final View vLine) {
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
     * @param txtBoxes Array of Textboxes
     * @param index    index of Textbox to make underline blue
     * @param clr      Colour
     */
    public static void MakeBlack(MaterialEditText[] txtBoxes, int index, int clr) {
        for (int i = 0; i < txtBoxes.length; i++) {
            if (i != index) {
                if (!txtBoxes[i].getText().toString().isEmpty()) {
                    txtBoxes[i].setUnderlineColor(clr);
                }
            }
        }
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

    public static boolean allFieldsEntered(MaterialEditText[] mText, int Red, int Black) {
        boolean allEntered = true;
        for (int i = 0; i < mText.length; i++) {
            if (Objects.requireNonNull(mText[i].getText()).toString().isEmpty()) {
                MakeBlack(mText, i, Black);
                mText[i].setUnderlineColor(Red);
                allEntered = false;
            }
        }
        for (int i = 0; i < mText.length; i++) {
            MakeBlack(mText, i, Black);
        }
        return allEntered;
    }

    public static boolean sexIsChecked(String UserSex, CheckBox Male, CheckBox Female, CheckBox Other, int Black) {
        if (UserSex.isEmpty()) {
            Male.setTextColor(Color.argb(255, 255, 23, 23));
            Female.setTextColor(Color.argb(255, 255, 23, 23));
            Other.setTextColor(Color.argb(255, 255, 23, 23));
            return false;
        }
        Male.setTextColor(Black);
        Female.setTextColor(Black);
        Other.setTextColor(Black);
        return true;
    }

}
