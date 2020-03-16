package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class OrderConfirmationActivity extends AppCompatActivity {

    private Button btFinish;
    private TextView tvUpdate;
    private View[] vLineGrey = new View[3];
    private View[] vLineLoad = new View[4];
    private View[] vLine = new View[4];
    private Handler handler = new Handler();
    private LinearLayout llNav;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.VISIBLE);
            vLineGrey[1].setVisibility(View.VISIBLE);

            vLineLoad[0].setVisibility(View.VISIBLE);
            vLineLoad[1].setVisibility(View.GONE);
            vLineLoad[2].setVisibility(View.GONE);

            vLine[0].setVisibility(View.GONE);
            vLine[1].setVisibility(View.GONE);
            vLine[2].setVisibility(View.GONE);

            tvUpdate.setText("Order has been placed");
            YoyoSlideRight(500, R.id.vLine1Load);
        }
    };

    private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.GONE);
            vLineGrey[1].setVisibility(View.VISIBLE);

            vLineLoad[0].setVisibility(View.GONE);
            vLineLoad[1].setVisibility(View.VISIBLE);
            vLineLoad[2].setVisibility(View.GONE);

            vLine[0].setVisibility(View.VISIBLE);
            vLine[1].setVisibility(View.GONE);
            vLine[2].setVisibility(View.GONE);

            llNav.setVisibility(View.GONE);
            btFinish.setVisibility(View.VISIBLE);

            tvUpdate.setText("Shop has been notified on your arrival");
            YoyoSlideRight(50, R.id.vLine2Load);
        }
    };

    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.GONE);
            vLineGrey[1].setVisibility(View.GONE);

            vLineLoad[0].setVisibility(View.GONE);
            vLineLoad[1].setVisibility(View.GONE);
            vLineLoad[2].setVisibility(View.VISIBLE);

            vLine[0].setVisibility(View.VISIBLE);
            vLine[1].setVisibility(View.VISIBLE);
            vLine[2].setVisibility(View.GONE);

            btFinish.setVisibility(View.GONE);

            tvUpdate.setText("Your order is ready for collection");
            YoyoSlideRight(50, R.id.vLine3Load);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        btFinish = findViewById(R.id.btFinish);
        tvUpdate = findViewById(R.id.tvUpdate);
        llNav = findViewById(R.id.llNav);
        vLineGrey[0] = findViewById(R.id.vLine2Grey);
        vLineGrey[1] = findViewById(R.id.vLine3Grey);

        vLineLoad[0] = findViewById(R.id.vLine1Load);
        vLineLoad[1] = findViewById(R.id.vLine2Load);
        vLineLoad[2] = findViewById(R.id.vLine3Load);

        vLine[0] = findViewById(R.id.vLine1);
        vLine[1] = findViewById(R.id.vLine2);
        vLine[2] = findViewById(R.id.vLine3);


        handler.postDelayed(runnable, 0);
        handler.postDelayed(runnable1, 5000);
        handler.postDelayed(runnable2, 10000);
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderConfirmationActivity.this, MainActivity.class));
            }
        });
    }

    private void YoyoSlideRight(int repeat, int vLine) {
        YoYo.with(Techniques.ZoomInRight)
                .duration(800)
                .repeat(repeat)
                .playOn(findViewById(vLine));
    }
}
