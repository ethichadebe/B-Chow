package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

public class REegisterShopActivity extends AppCompatActivity {

    private MaterialEditText txtName, txtShortDescription, txtFullDescription;
    private Dialog myDialog;
    Button btnOder;
    LinearLayout llBack;
    RelativeLayout rlPicture;
    TextView tvHeading, lblError;
    CardView cvShop2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reegister_shop);

        llBack = findViewById(R.id.llBack);
        btnOder = findViewById(R.id.btnOder);
        rlPicture = findViewById(R.id.rlPicture);
        tvHeading = findViewById(R.id.tvHeading);
        cvShop2 = findViewById(R.id.cvShop2);
        lblError = findViewById(R.id.lblError);

        myDialog = new Dialog(this);
        txtName = findViewById(R.id.txtName);
        txtShortDescription = findViewById(R.id.txtShortDescription);
        txtFullDescription = findViewById(R.id.txtFullDescription);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtName.getText().toString().isEmpty() || !txtFullDescription.getText().toString().isEmpty() ||
                        !txtShortDescription.getText().toString().isEmpty()){
                    ShowPopup();
                }else {
                    finish();
                }
            }
        });

        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (txtName.getText().toString().isEmpty()){
                txtName.setUnderlineColor(getResources().getColor(R.color.Red));
            } else {
                txtName.setUnderlineColor(getResources().getColor(R.color.Black));
                startActivity(new Intent(REegisterShopActivity.this, MenuCreationActivity.class));
            }
            }
        });
    }

    public void ShowPopup(){
        TextView tvCancel, tvMessage;
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.confirmation_popup);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        tvMessage.setText("All entered information will be lost\nAre you sure?");

        cvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                finish();
            }
        });

        cvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
