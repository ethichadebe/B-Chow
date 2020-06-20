package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import www.ethichadebe.co.za.uploadpicture.UploadImage;

public class UploadPicActivity extends AppCompatActivity {
    private static final String TAG = "UploadPicActivity";
    private Dialog myDialog;
    private String pathToFile;
    private ImageView civProfilePicture;
    private UploadImage uploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pic);
        myDialog = new Dialog(this);
        civProfilePicture = findViewById(R.id.civProfilePicture);

        uploadImage = new UploadImage(this,this,getPackageManager(),myDialog,civProfilePicture,
                "www.ethichadebe.com.loxion_beanery",TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uploadImage.onActivityResult(getCacheDir(), requestCode, resultCode, data);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        uploadImage.onRequestPermissionsResult(requestCode,grantResults);
    }

    public void Skip(View view) {
        startActivity(new Intent(this, UserTypeActivity.class));
    }

    //Changing profile picture
    public void ProfilePicture(View view) {
        uploadImage.start();
    }
}
