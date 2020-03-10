package util;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import www.ethichadebe.com.loxion_beanery.R;

public class HelperMethods {

    public static void ShowLoadingPopup(final Dialog myDialog) {
        Handler handler = new Handler();
        myDialog.setContentView(R.layout.popup_loading);

        handler.postDelayed(LoaderMotion(myDialog.findViewById(R.id.vLineGrey)), 0);
        handler.postDelayed(LoaderMotion(myDialog.findViewById(R.id.vLine)), 500);


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    public static Runnable LoaderMotion(final View vLine) {
        return new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.ZoomInRight)
                        .duration(1000)
                        .repeat(5000)
                        .playOn(vLine);
            }
        };
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
    public static void ButtonVisibility(ArrayList list, Button btn){
        if (list.isEmpty()){
            btn.setVisibility(View.GONE);
        }else {
            btn.setVisibility(View.VISIBLE);
        }
    }

}
