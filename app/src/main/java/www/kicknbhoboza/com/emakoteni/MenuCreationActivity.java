package www.kicknbhoboza.com.emakoteni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MenuCreationActivity extends AppCompatActivity {

    LinearLayout llBack;
    Button btnOder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_creation);

        btnOder = findViewById(R.id.btnOder);
        llBack = findViewById(R.id.llBack);

        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuCreationActivity.this, MenuActivity.class));
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
