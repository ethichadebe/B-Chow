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

import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.checkData;
import static util.HelperMethods.loadData;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;

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
        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }
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
