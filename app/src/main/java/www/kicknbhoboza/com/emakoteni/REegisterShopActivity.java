package www.kicknbhoboza.com.emakoteni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class REegisterShopActivity extends AppCompatActivity {

    Button btnOder;
    LinearLayout llBack;
    RelativeLayout rlPicture;
    TextView tvHeading;
    CardView cvShop2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reegister_shop);

        llBack = findViewById(R.id.llBack);
        btnOder = findViewById(R.id.btnOder);
        rlPicture = findViewById(R.id.rlPicture);
        tvHeading = findViewById(R.id.tvHeading);
        cvShop2 = findViewById(R.id.cvShop2);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(REegisterShopActivity.this, MenuCreationActivity.class));
            }
        });
    }
}
