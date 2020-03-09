package util;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rengwuxian.materialedittext.MaterialEditText;

import www.ethichadebe.com.loxion_beanery.R;

public class HelperMethods {

    public static void ShowLoadingPopup(final Dialog myDialog) {
        Handler handler = new Handler();
        myDialog.setContentView(R.layout.popup_loading);

        handler.postDelayed(LoaderMotion(myDialog.findViewById(R.id.vLineGrey)), 0);
        handler.postDelayed(LoaderMotion(myDialog.findViewById(R.id.vLine)), 500);


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private static Runnable LoaderMotion(final View vLine){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.ZoomInRight)
                        .duration(1000)
                        .repeat(5000)
                        .playOn(vLine);
            }
        };

        return runnable;
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



}
