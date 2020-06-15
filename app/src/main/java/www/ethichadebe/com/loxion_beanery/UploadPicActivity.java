package www.ethichadebe.com.loxion_beanery;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.RequestQueue;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Objects;

import static util.HelperMethods.CAMERA_PERMISSION;
import static util.HelperMethods.STORAGE_PERMISSION;
import static util.HelperMethods.createFile;
import static util.HelperMethods.requestPermission;
import static util.HelperMethods.startCrop;

public class UploadPicActivity extends AppCompatActivity {
    private static final String TAG = "UploadPicActivity";
    private Dialog myDialog;
    private String pathToFile;
    private ImageView civProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pic);
        myDialog = new Dialog(this);
        civProfilePicture = findViewById(R.id.civProfilePicture);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        //civProfilePicture.
        if ((requestCode == STORAGE_PERMISSION) && (resultCode == RESULT_OK)) {
            uri = Objects.requireNonNull(data).getData();
            if (uri != null) {
                startCrop(this, getCacheDir(), uri, 350, 350);
            }
        } else if ((requestCode == CAMERA_PERMISSION) && (resultCode == RESULT_OK)) {
            if (BitmapFactory.decodeFile(pathToFile) != null) {
                startCrop(this, getCacheDir(), Uri.fromFile(new File(pathToFile)),350, 350);
            }
        } else if ((requestCode == UCrop.REQUEST_CROP) && (resultCode == RESULT_OK)) {
            uri = UCrop.getOutput(Objects.requireNonNull(data));
            if (uri != null) {
                civProfilePicture.setImageDrawable(null);
                civProfilePicture.setImageURI(uri);
            }
        }

    }


    public void ShowDPEditPopup() {
        TextView tvCancel, tvMessage,btnYes, btnNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        btnNo = myDialog.findViewById(R.id.btnNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        btnNo.setText("Open Gallery");
        btnYes.setText("Open Camera");
        tvMessage.setText("Choose Profile Picture");

        btnYes.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "ShowDPEditPopup: Take picture");
                takePicture();
                myDialog.dismiss();
            } else {
                myDialog.dismiss();
                requestPermission(this, this, CAMERA_PERMISSION);
            }

        });

        btnNo.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                myDialog.dismiss();
                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),
                        STORAGE_PERMISSION);
            } else {
                myDialog.dismiss();
                requestPermission(this, this, STORAGE_PERMISSION);
            }
        });

        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if ((requestCode == STORAGE_PERMISSION) && ((grantResults.length) > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    STORAGE_PERMISSION);
        } else if ((requestCode == CAMERA_PERMISSION) && ((grantResults.length) > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePicture();
        }
    }

    private void takePicture() {
        Log.d(TAG, "takePicture: Taking picture");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check if there's an app available to take picture
        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "takePicture: getPackageManager()) != null");
            File photo = null;
            photo = createFile(TAG);
            //Save picture into the photo var
            if (photo != null) {
                pathToFile = Objects.requireNonNull(photo).getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(this,
                        "www.ethichadebe.com.loxion_beanery.fileprovider", photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                Log.d(TAG, "takePicture: Start picture taking activity");
                startActivityForResult(intent, CAMERA_PERMISSION);
            }
        }

    }

    public void Skip(View view) {
        startActivity(new Intent(this, UserTypeActivity.class));
    }

    //Changing profile picture
    public void ProfilePicture(View view) {
        ShowDPEditPopup();
    }
}
