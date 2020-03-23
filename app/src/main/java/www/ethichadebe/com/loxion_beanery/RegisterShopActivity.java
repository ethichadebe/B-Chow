package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import SingleItem.MyShopItem;

public class RegisterShopActivity extends AppCompatActivity {
    private Dialog myDialog;
    private MaterialEditText txtName, txtShortDescription, txtFullDescription;
    private Boolean isBig;
    private static MyShopItem newShop;
    private CropImageView civSmall, civBig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);

        LinearLayout llBack = findViewById(R.id.llBack);

        myDialog = new Dialog(this);
        txtName = findViewById(R.id.txtName);
        civSmall = findViewById(R.id.civSmall);
        civBig = findViewById(R.id.civBig);
        txtShortDescription = findViewById(R.id.txtShortDescription);
        txtFullDescription = findViewById(R.id.txtFullDescription);

        llBack.setOnClickListener(view -> {
            if (!Objects.requireNonNull(txtName.getText()).toString().isEmpty() || !Objects.requireNonNull(txtFullDescription.getText()).toString().isEmpty() ||
                    !Objects.requireNonNull(txtShortDescription.getText()).toString().isEmpty()) {
                ShowPopup();
            } else {
                finish();
            }
        });
    }

    public static MyShopItem getNewShop() {
        return newShop;
    }

    public void ShowPopup() {
        TextView tvCancel, tvMessage;
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("All entered information will be lost\nAre you sure?");

        cvYes.setOnClickListener(view -> {
            myDialog.dismiss();
            finish();
        });

        cvNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                setImage(resultUri, isBig);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void next(View view) {
        if (Objects.requireNonNull(txtName.getText()).toString().isEmpty()) {
            txtName.setUnderlineColor(getResources().getColor(R.color.Red));
        } else {
            txtName.setUnderlineColor(getResources().getColor(R.color.Black));
            newShop = new MyShopItem(txtName.getText().toString(), Objects.requireNonNull(txtShortDescription.getText()).toString(),
                    Objects.requireNonNull(txtFullDescription.getText()).toString(), 1,1,new Location(""));

            startActivity(new Intent(RegisterShopActivity.this, OperatingHoursActivity.class));
        }
    }

    public void small(View view) {
        isBig = false;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1920, 1080)
                .start(this);
    }

    public void big(View view) {
        isBig = true;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(720, 282)
                .start(this);
    }

    private void setImage(Uri uri, boolean isBig) {
        if (isBig) {
            civBig.setImageUriAsync(uri);
        } else {
            civSmall.setImageUriAsync(uri);
        }
    }
}
