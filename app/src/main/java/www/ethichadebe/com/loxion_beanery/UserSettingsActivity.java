package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;

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
        setIntFragment(2);
        startActivity(new Intent(this, MainActivity.class));
    }

    public void MyShops(View view) {
        startActivity(new Intent(this, UploadPicActivity.class));

    }

    public void Profile(View view) {
        startActivity(new Intent(this, EditUserProfileActivity.class));
    }

    @Override
    public void onBackPressed() {
        setIntFragment(2);
        startActivity(new Intent(this, MainActivity.class));
    }
}
