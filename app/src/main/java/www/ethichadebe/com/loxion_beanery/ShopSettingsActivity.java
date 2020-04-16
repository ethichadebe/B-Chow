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
        startActivity(new Intent(this, MyShopsActivity.class));
    }

    public void shop(View view) {
        startActivity(new Intent(this, RegisterShopActivity.class));
    }

    public void OH(View view) {
        startActivity(new Intent(this, OperatingHoursActivity.class));
    }

    public void Extra(View view) {
        startActivity(new Intent(this, NewExtrasActivity.class));
    }

    public void Menu(View view) {
        startActivity(new Intent(this, MenuActivity.class));
    }

    public void Ingredients(View view) {
        startActivity(new Intent(this, IngredientsActivity.class));
    }
}
