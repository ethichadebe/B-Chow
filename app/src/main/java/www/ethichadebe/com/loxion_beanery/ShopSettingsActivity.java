package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class ShopSettingsActivity extends AppCompatActivity {

    private CardView cvShop;
    private LinearLayout llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_settings);
        if (getUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

        llBack = findViewById(R.id.llBack);
        cvShop = findViewById(R.id.cvShop);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cvShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShopSettingsActivity.this, RegisterShopActivity.class));
            }
        });
    }
}
