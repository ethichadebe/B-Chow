package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.OrdersFragment.getPosition;

public class RatingActivity extends AppCompatActivity {
    private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5, testStar;
    private Button btnNext;
    private LinearLayout llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        if (getUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

        ivStar1 = findViewById(R.id.ivStar1);
        ivStar2 = findViewById(R.id.ivStar2);
        ivStar3 = findViewById(R.id.ivStar3);
        ivStar4 = findViewById(R.id.ivStar4);
        ivStar5 = findViewById(R.id.ivStar5);
        llBack = findViewById(R.id.llBack);
        btnNext = findViewById(R.id.btnNext);

        ivStar1.setOnClickListener(view -> {
            ivStar1.setImageResource(R.drawable.star);
            ivStar2.setImageResource(R.drawable.star_empty);
            ivStar3.setImageResource(R.drawable.star_empty);
            ivStar4.setImageResource(R.drawable.star_empty);
            ivStar5.setImageResource(R.drawable.star_empty);
            //getPastOrderItem(getPosition()).setIntRating(1);
        });
        ivStar2.setOnClickListener(view -> {
            ivStar1.setImageResource(R.drawable.star);
            ivStar2.setImageResource(R.drawable.star);
            ivStar3.setImageResource(R.drawable.star_empty);
            ivStar4.setImageResource(R.drawable.star_empty);
            ivStar5.setImageResource(R.drawable.star_empty);
            //getPastOrderItem(getPosition()).setIntRating(2);
        });

        ivStar3.setOnClickListener(view  -> {
            ivStar1.setImageResource(R.drawable.star);
            ivStar2.setImageResource(R.drawable.star);
            ivStar3.setImageResource(R.drawable.star);
            ivStar4.setImageResource(R.drawable.star_empty);
            ivStar5.setImageResource(R.drawable.star_empty);
           // getPastOrderItem(getPosition()).setIntRating(3);
        });

        ivStar4.setOnClickListener(view -> {
            ivStar1.setImageResource(R.drawable.star);
            ivStar2.setImageResource(R.drawable.star);
            ivStar3.setImageResource(R.drawable.star);
            ivStar4.setImageResource(R.drawable.star);
            ivStar5.setImageResource(R.drawable.star_empty);
           // getPastOrderItem(getPosition()).setIntRating(4);
        });

        ivStar5.setOnClickListener(view -> {
            ivStar1.setImageResource(R.drawable.star);
            ivStar2.setImageResource(R.drawable.star);
            ivStar3.setImageResource(R.drawable.star);
            ivStar4.setImageResource(R.drawable.star);
            ivStar5.setImageResource(R.drawable.star);
            //getPastOrderItem(getPosition()).setIntRating(5);
        });


       /* btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPastOrderItem(getPosition()).getIntRating() == -1) {
                    ivStar1.setImageResource(R.drawable.star_empty_err);
                    ivStar2.setImageResource(R.drawable.star_empty_err);
                    ivStar3.setImageResource(R.drawable.star_empty_err);
                    ivStar4.setImageResource(R.drawable.star_empty_err);
                    ivStar5.setImageResource(R.drawable.star_empty_err);
                } else {
                    startActivity(new Intent(RatingActivity.this, MainActivity.class));
                }
            }
        });*/
    }
}
