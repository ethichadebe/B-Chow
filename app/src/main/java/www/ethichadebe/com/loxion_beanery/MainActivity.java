package www.ethichadebe.com.loxion_beanery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class MainActivity extends AppCompatActivity {
    private static int intFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

        BottomNavigationView bottomNav= findViewById(R.id.bottom_navigation );

        //Start Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        //On Back Press
        switch (intFragment){
            case 1:
                intFragment = -1;
                bottomNav.setSelectedItemId(R.id.nav_orders);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OrdersFragment()).commit();

                break;
            case 2:
                intFragment = -1;
                bottomNav.setSelectedItemId(R.id.nav_profile);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
        }

        bottomNav.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_orders:
                    selectedFragment = new OrdersFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

            return true;
        });

    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    public static void setIntFragment(int intFragment) {
        MainActivity.intFragment = intFragment;
    }
}