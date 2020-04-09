package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        if (getUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    public void back(View view) {
        finish();
    }

    public void MyShops(View view) {
        startActivity(new Intent(UserSettingsActivity.this, MyShopsActivity.class));

    }

    public void Profile(View view) {
        startActivity(new Intent(UserSettingsActivity.this, EditUserProfileActivity.class));
    }
}
