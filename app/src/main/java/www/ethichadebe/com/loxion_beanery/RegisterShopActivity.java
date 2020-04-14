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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import SingleItem.MyShopItem;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.setNewShop;

public class RegisterShopActivity extends AppCompatActivity {
    private Dialog myDialog;
    private TextView tvName;
    private MaterialEditText etName, etShortDescription, etFullDescription;
    private Boolean isBig;
    private CropImageView civSmall, civBig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } //Check if user is logged in

        myDialog = new Dialog(this);
        etName = findViewById(R.id.etName);
        tvName = findViewById(R.id.tvName);
        civSmall = findViewById(R.id.civSmall);
        civBig = findViewById(R.id.civBig);
        etShortDescription = findViewById(R.id.etShortDescription);
        etFullDescription = findViewById(R.id.etFullDescription);

        if(getNewShop() != null){
            etName.setText(getNewShop().getStrShopName());
            if (getNewShop().getStrShortDescript() != null){
                etShortDescription.setText(getNewShop().getStrShortDescript());
            }
            if (getNewShop().getStrFullDescript() != null)
                etFullDescription.setText(getNewShop().getStrFullDescript());
        }//If user pressed back from Operation Hours activity

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    tvName.setText(charSequence);
                } else {
                    tvName.setText("Shop name");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
            startActivity(new Intent(this, MyShopsActivity.class));
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
        if (Objects.requireNonNull(etName.getText()).toString().isEmpty()) {
            etName.setError("required");
        } else {
            if (getNewShop() == null){
                setNewShop(new MyShopItem(etName.getText().toString(), Objects.requireNonNull(etShortDescription.getText()).toString(),
                        Objects.requireNonNull(etFullDescription.getText()).toString(), 1, 1, new Location("")));
            }

            startActivity(new Intent(RegisterShopActivity.this, OperatingHoursActivity.class));
        }
    }

    public void Small(View view) {
        isBig = false;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(720, 282)
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

    public void back(View view) {
        if (!Objects.requireNonNull(etName.getText()).toString().isEmpty() || !Objects.requireNonNull(etFullDescription.getText()).toString().isEmpty() ||
                !Objects.requireNonNull(etShortDescription.getText()).toString().isEmpty()) {
            ShowPopup();
        } else {
            startActivity(new Intent(this, MyShopsActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        if (!Objects.requireNonNull(etName.getText()).toString().isEmpty() || !Objects.requireNonNull(etFullDescription.getText()).toString().isEmpty() ||
                !Objects.requireNonNull(etShortDescription.getText()).toString().isEmpty()) {
            ShowPopup();
        } else {
            startActivity(new Intent(this, MyShopsActivity.class));
        }
    }
}
