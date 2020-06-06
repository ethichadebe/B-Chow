package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ShowLoadingPopup;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.getNewShop;

public class ShopSettingsActivity extends AppCompatActivity {
    private static final String TAG = "ShopSettingsActivity";
    private RequestQueue requestQueue;
    static boolean isEdit = false;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_settings);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        myDialog = new Dialog(this);

    }

    public void back(View view) {
        startActivity(new Intent(this, OrdersActivity.class));
    }

    public void shop(View view) {
        isEdit = true;
        startActivity(new Intent(this, RegisterShopActivity.class));
    }

    public void OH(View view) {
        isEdit = true;
        startActivity(new Intent(this, OperatingHoursActivity.class));
    }

    public void Extra(View view) {
        isEdit = true;
        startActivity(new Intent(this, NewExtrasActivity.class));
    }

    public void Menu(View view) {
        isEdit = true;
        startActivity(new Intent(this, MenuActivity.class));
    }

    public void Ingredients(View view) {
        isEdit = true;
        startActivity(new Intent(this, IngredientsActivity.class));
    }

    public void Deactivate(View view) {
        ShowConfirmationPopup();
    }

    public void ShowConfirmationPopup() {
        TextView tvCancel, tvMessage;
        Button btnYes, btnNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        btnNo = myDialog.findViewById(R.id.btnNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("All Sop information will be lost for good.\nAre you sure?");

        btnYes.setOnClickListener(view -> DeactivateShop());

        btnNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void DeactivateShop() {
        ShowLoadingPopup(this.myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/shops/deactivate/" + getNewShop().getIntID(),
                response -> {
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).s  ();
                    ShowLoadingPopup(myDialog, false);
                    startActivity(new Intent(this, UploadPicActivity.class));
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, OrdersActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
