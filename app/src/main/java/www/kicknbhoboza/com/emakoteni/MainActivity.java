package www.kicknbhoboza.com.emakoteni;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import util.User;

public class MainActivity extends AppCompatActivity {
    private View vLeft, vRight, vBottomRight, vBottomLeft;
    RelativeLayout rellay1, rellay2, rlLeft, rlRight;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    private Dialog myDialog;
    private EditText mTextPassword, mTextUsername;
    private TextView mViewError;
    private ImageView mImageLogo;
    private CardView mButtonLogin, mButtonRegister;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        handler.postDelayed(runnable, 500);

        vBottomLeft = findViewById(R.id.vBottomLeft);
        vBottomRight = findViewById(R.id.vBottomRight);
        vLeft = findViewById(R.id.vLeft);
        vRight = findViewById(R.id.vRight);

        rlLeft = findViewById(R.id.rlLeft);
        rlRight = findViewById(R.id.rlRight);

        setVisibility(View.VISIBLE, View.GONE);

        rlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.VISIBLE, View.GONE);
            }
        });
        rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.GONE  , View.VISIBLE);
            }
        });


        myDialog = new Dialog(this);
        mTextUsername = findViewById(R.id.txtUsername);
        mTextPassword = findViewById(R.id.txtPassword);

        //Image
        mImageLogo = findViewById(R.id.ivLogo);

        mButtonLogin = findViewById(R.id.btnLogin);
        mButtonRegister = findViewById(R.id.btnRegister);

        mViewError = findViewById(R.id.lblError);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTextUsername.getText().toString().isEmpty() && mTextPassword.getText().toString().isEmpty()) {
                    mTextUsername.setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                    mTextPassword.setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                    mViewError.setText("Enter Both fields");
                } else if (mTextUsername.getText().toString().isEmpty() && !mTextPassword.getText().toString().isEmpty()) {
                    mTextUsername.setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                    mTextPassword.setBackground(getResources().getDrawable(R.drawable.et_bg));
                    mViewError.setText("Enter Username");
                } else if (!mTextUsername.getText().toString().isEmpty() && mTextPassword.getText().toString().isEmpty()) {
                    mTextUsername.setBackground(getResources().getDrawable(R.drawable.et_bg));
                    mTextPassword.setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                    mViewError.setText("Enter Password");
                } else {
                    startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                }
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    private void setVisibility(int Left,int Right){
        vRight.setVisibility(Right);
        vLeft.setVisibility(Left);
        vBottomLeft.setVisibility(Left);
        vBottomRight.setVisibility(Right);
    }
}