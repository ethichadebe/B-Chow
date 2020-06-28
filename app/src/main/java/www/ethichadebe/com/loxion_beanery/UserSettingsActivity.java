package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.checkData;
import static util.HelperMethods.loadData;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;

public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
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
