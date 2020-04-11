package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class ShopSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_settings);
        if (getUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    public void back(View view) {finish();
    }

    public void shop(View view) {
        startActivity(new Intent(ShopSettingsActivity.this, RegisterShopActivity.class));
    }
}
