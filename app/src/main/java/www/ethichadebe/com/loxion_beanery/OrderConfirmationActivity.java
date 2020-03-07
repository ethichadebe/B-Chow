package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class OrderConfirmationActivity extends AppCompatActivity {

    Button btFinish;
    private View[] vLineGrey = new View[3];
    private View[] vLineLoad = new View[4];
    private View[] vLine = new View[4];
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.VISIBLE);
            vLineGrey[1].setVisibility(View.VISIBLE);
            vLineGrey[2].setVisibility(View.VISIBLE);

            vLineLoad[0].setVisibility(View.VISIBLE);
            vLineLoad[1].setVisibility(View.GONE);
            vLineLoad[2].setVisibility(View.GONE);
            vLineLoad[3].setVisibility(View.GONE);

            vLine[0].setVisibility(View.GONE);
            vLine[1].setVisibility(View.GONE);
            vLine[2].setVisibility(View.GONE);
            vLine[3].setVisibility(View.GONE);

            YoyoSlideRight(500, R.id.vLine1Load);
        }
    };

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.GONE);
            vLineGrey[1].setVisibility(View.VISIBLE);
            vLineGrey[2].setVisibility(View.VISIBLE);

            vLineLoad[0].setVisibility(View.GONE);
            vLineLoad[1].setVisibility(View.VISIBLE);
            vLineLoad[2].setVisibility(View.GONE);
            vLineLoad[3].setVisibility(View.GONE);

            vLine[0].setVisibility(View.VISIBLE);
            vLine[1].setVisibility(View.GONE);
            vLine[2].setVisibility(View.GONE);
            vLine[3].setVisibility(View.GONE);

            YoyoSlideRight(50, R.id.vLine2Load);
        }
    };

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.GONE);
            vLineGrey[1].setVisibility(View.GONE);
            vLineGrey[2].setVisibility(View.VISIBLE);

            vLineLoad[0].setVisibility(View.GONE);
            vLineLoad[1].setVisibility(View.GONE);
            vLineLoad[2].setVisibility(View.VISIBLE);
            vLineLoad[3].setVisibility(View.GONE);

            vLine[0].setVisibility(View.VISIBLE);
            vLine[1].setVisibility(View.VISIBLE);
            vLine[2].setVisibility(View.GONE);
            vLine[3].setVisibility(View.GONE);

            YoyoSlideRight(50, R.id.vLine3Load);
        }
    };

    Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.GONE);
            vLineGrey[1].setVisibility(View.GONE);
            vLineGrey[2].setVisibility(View.GONE);

            vLineLoad[0].setVisibility(View.GONE);
            vLineLoad[1].setVisibility(View.GONE);
            vLineLoad[2].setVisibility(View.GONE);
            vLineLoad[3].setVisibility(View.VISIBLE);

            vLine[0].setVisibility(View.VISIBLE);
            vLine[1].setVisibility(View.VISIBLE);
            vLine[2].setVisibility(View.VISIBLE);
            vLine[3].setVisibility(View.GONE);

            YoyoSlideRight(50, R.id.vLine4Load);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        btFinish = findViewById(R.id.btFinish);
        vLineGrey[0] = findViewById(R.id.vLine2Grey);
        vLineGrey[1] = findViewById(R.id.vLine3Grey);
        vLineGrey[2] = findViewById(R.id.vLine4Grey);

        vLineLoad[0] = findViewById(R.id.vLine1Load);
        vLineLoad[1] = findViewById(R.id.vLine2Load);
        vLineLoad[2] = findViewById(R.id.vLine3Load);
        vLineLoad[3] = findViewById(R.id.vLine4Load);

        vLine[0] = findViewById(R.id.vLine1);
        vLine[1] = findViewById(R.id.vLine2);
        vLine[2] = findViewById(R.id.vLine3);
        vLine[3] = findViewById(R.id.vLine4);


        handler.postDelayed(runnable, 0);
        handler.postDelayed(runnable1, 3000);
        handler.postDelayed(runnable2, 6000);
        handler.postDelayed(runnable3, 9000);
       /* YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);
        YoyoSlideRight(1);*/

        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderConfirmationActivity.this, MainActivity.class));
            }
        });
    }

    private void YoyoSlideRight(int repeat, int vLine) {
        YoYo.with(Techniques.ZoomInRight)
                .duration(1000)
                .repeat(repeat)
                .playOn(findViewById(vLine));
    }
}
