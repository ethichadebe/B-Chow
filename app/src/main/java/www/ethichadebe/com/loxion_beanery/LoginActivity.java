package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout rellay1, rellay2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    private Dialog myDialog;
    private MaterialEditText mTextPassword, mTextUsername;
    private TextView mViewError;
    private CardView mImageLogo;
    private CardView mButtonLogin, mButtonRegister;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        handler.postDelayed(runnable, 500);


        myDialog = new Dialog(this);
        mTextUsername = findViewById(R.id.txtUsername);
        mTextPassword = findViewById(R.id.txtPassword);

        //Check if shared prefs are empty
        loadData();
        /*if (!mTextUsername.getText().toString().isEmpty() && !mTextPassword.getText().toString().isEmpty()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }*/
        //Image
        mImageLogo = findViewById(R.id.ivLogo);

        mButtonLogin = findViewById(R.id.btnLogin);
        mButtonRegister = findViewById(R.id.btnRegister);

        mViewError = findViewById(R.id.lblError);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTextUsername.getText().toString().isEmpty() && mTextPassword.getText().toString().isEmpty()) {
                    mTextUsername.setUnderlineColor(getResources().getColor(R.color.Red));
                    mTextPassword.setUnderlineColor(getResources().getColor(R.color.Red));
                    mViewError.setText("Enter Both fields");
                } else if (mTextUsername.getText().toString().isEmpty() && !mTextPassword.getText().toString().isEmpty()) {
                    mTextUsername.setUnderlineColor(getResources().getColor(R.color.Red));
                    mTextPassword.setUnderlineColor(getResources().getColor(R.color.Black));
                    mViewError.setText("Enter Username");
                } else if (!mTextUsername.getText().toString().isEmpty() && mTextPassword.getText().toString().isEmpty()) {
                    mTextPassword.setUnderlineColor(getResources().getColor(R.color.Red));
                    mTextUsername.setUnderlineColor(getResources().getColor(R.color.Black));
                    mViewError.setText("Enter Password");
                } else {
                    saveData();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }


    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERNAME, mTextUsername.getText().toString());
        editor.putString(PASSWORD, mTextPassword.getText().toString());

        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        mTextPassword.setText(sharedPreferences.getString(PASSWORD, ""));
        mTextUsername.setText(sharedPreferences.getString(USERNAME, ""));
    }
}