package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ShowLoadingPopup;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.OrderActivity.oID;
import static www.ethichadebe.com.loxion_beanery.OrdersFragment.getUpcomingOrderItem;

public class OrderConfirmationActivity extends AppCompatActivity {

    private Button btFinish;
    private TextView tvUpdate, tvUpdateMessage;
    private View[] vLineGrey = new View[3];
    private View[] vLineLoad = new View[4];
    private View[] vLine = new View[4];
    private Handler handler = new Handler();
    private LinearLayout llNav;
    private GifImageView givGif;
    private Dialog myDialog;
    private CardView cvCancel,cvNavigate;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.VISIBLE);
            vLineGrey[1].setVisibility(View.VISIBLE);

            vLineLoad[0].setVisibility(View.VISIBLE);
            vLineLoad[1].setVisibility(View.GONE);
            vLineLoad[2].setVisibility(View.GONE);

            vLine[0].setVisibility(View.GONE);
            vLine[1].setVisibility(View.GONE);
            vLine[2].setVisibility(View.GONE);

            tvUpdate.setText("Order has been placed");
            tvUpdateMessage.setText("Make your way to the shop within the next Hour");
            givGif.setImageResource(R.drawable.driving);
            YoyoSlideRight(5000, R.id.vLine1Load);
        }
    };

    private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            oID = -1;
            vLineGrey[0].setVisibility(View.GONE);
            vLineGrey[1].setVisibility(View.VISIBLE);

            vLineLoad[0].setVisibility(View.GONE);
            vLineLoad[1].setVisibility(View.VISIBLE);
            vLineLoad[2].setVisibility(View.GONE);

            vLine[0].setVisibility(View.VISIBLE);
            vLine[1].setVisibility(View.GONE);
            vLine[2].setVisibility(View.GONE);

            llNav.setVisibility(View.GONE);
            cvNavigate.setVisibility(View.GONE);
            btFinish.setVisibility(View.VISIBLE);

            tvUpdate.setText("Shop has been notified on your arrival");
            tvUpdateMessage.setText("and now lets patiently wait for the shop to complete the order");
            givGif.setImageResource(R.drawable.waiting);
            YoyoSlideRight(5000, R.id.vLine2Load);
        }
    };

    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.GONE);
            vLineGrey[1].setVisibility(View.GONE);

            vLineLoad[0].setVisibility(View.GONE);
            vLineLoad[1].setVisibility(View.GONE);
            vLineLoad[2].setVisibility(View.VISIBLE);

            vLine[0].setVisibility(View.VISIBLE);
            vLine[1].setVisibility(View.VISIBLE);
            vLine[2].setVisibility(View.GONE);

            cvNavigate.setVisibility(View.GONE);
            llNav.setVisibility(View.GONE);
            btFinish.setVisibility(View.VISIBLE);

            tvUpdate.setText("Your order is ready for collection");
            tvUpdateMessage.setText("Your order is complete, Please collect and enjoy");
            givGif.setImageResource(R.drawable.eating);
            YoyoSlideRight(500, R.id.vLine3Load);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        myDialog = new Dialog(this);
        btFinish = findViewById(R.id.btFinish);
        tvUpdate = findViewById(R.id.tvUpdate);
        cvCancel = findViewById(R.id.cvCancel);
        cvNavigate = findViewById(R.id.cvNavigate);
        tvUpdateMessage = findViewById(R.id.tvUpdateMsg);
        llNav = findViewById(R.id.llNav);
        givGif = findViewById(R.id.givGif);

        vLineGrey[0] = findViewById(R.id.vLine2Grey);
        vLineGrey[1] = findViewById(R.id.vLine3Grey);

        vLineLoad[0] = findViewById(R.id.vLine1Load);
        vLineLoad[1] = findViewById(R.id.vLine2Load);
        vLineLoad[2] = findViewById(R.id.vLine3Load);

        vLine[0] = findViewById(R.id.vLine1);
        vLine[1] = findViewById(R.id.vLine2);
        vLine[2] = findViewById(R.id.vLine3);

        if (getUpcomingOrderItem() != null) {
            switch (getUpcomingOrderItem().getStrStatus()) {
                case "Waiting arrival":
                    handler.postDelayed(runnable, 0);
                    break;
                case "Waiting for order":
                    handler.postDelayed(runnable1, 0);
                    break;
                case "Ready for collection":
                    handler.postDelayed(runnable2, 0);
                    break;
            }
        } else {
            handler.postDelayed(runnable, 0);
        }

        cvCancel.setOnClickListener(view -> {
            if (oID != -1) {

                ShowConfirmationPopup(oID);
            } else {
                ShowConfirmationPopup(getUpcomingOrderItem().getIntID());
            }
        });
        btFinish.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
    }

    private void YoyoSlideRight(int repeat, int vLine) {
        YoYo.with(Techniques.ZoomInRight)
                .duration(800)
                .repeat(repeat)
                .playOn(findViewById(vLine));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void arrived(View view) {
        if (oID != -1) {
            PUTArrived(oID);
        } else {
            PUTArrived(getUpcomingOrderItem().getIntID());
        }
    }

    private void PUTArrived(int oID) {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                "http://" + getIpAddress() + "/orders/Arrived/" + oID,
                response -> {
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    ShowLoadingPopup(myDialog, false);
                    handler.postDelayed(runnable1, 0);
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void PUTCancel(int oID) {
        ShowLoadingPopup(this.myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                "http://" + getIpAddress() + "/orders/Cancel/" + oID,
                response -> {
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).s  ();
                    ShowLoadingPopup(myDialog, false);
                    startActivity(new Intent(this, MainActivity.class));
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void ShowConfirmationPopup(int ID) {
        TextView tvCancel, tvMessage;
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("Are you sure?");

        cvYes.setOnClickListener(view -> PUTCancel(ID));

        cvNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void home(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
