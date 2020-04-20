package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class RatingActivity extends AppCompatActivity {
    private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5, testStar;

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
    }

    public void back(View view) {
        finish();
    }
}
