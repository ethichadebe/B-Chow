package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Adapter.ExtraItemAdapter;
import SingleItem.ExtraItem;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ButtonVisibility;
import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.getMenuItems;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.RegisterShopActivity.getNewShop;

public class NewExtrasActivity extends AppCompatActivity {
    private static ArrayList<ExtraItem> extraItems= new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ExtraItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Dialog myDialog;

    private static boolean isNew = false;
    public static boolean isNew() {
        return isNew;
    }

    private MaterialEditText etExtra;

    public static void setIsNew(boolean isNew) {
        NewExtrasActivity.isNew = isNew;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_extras);
        if (getUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

        etExtra = findViewById(R.id.etExtra);
        myDialog = new Dialog(this);

        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExtraItemAdapter(extraItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnExtraClickListener(new ExtraItemAdapter.OnExtraClickListener() {
            @Override
            public void onRemoveClick(int position) {
                extraItems.remove(position);
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onEditClick(int position) {

            }
        });
    }

    public void next(View view) {
        getNewShop().setMenuItems(getMenuItems());
        isNew = true;
        startActivity(new Intent(NewExtrasActivity.this, MyShopsActivity.class));
    }

    public void back(View view) {
        finish();
    }

    public void add(View view) {
        if (Objects.requireNonNull(etExtra.getText()).toString().isEmpty()){
            etExtra.setUnderlineColor(getResources().getColor(R.color.Red));
        }else {
            etExtra.setUnderlineColor(getResources().getColor(R.color.Grey));
            POSTRegisterShopExtra();
        }


    }

    private void POSTRegisterShopExtra() {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://" + getIpAddress() + "/shops/Register/Extra",
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response);
                        if (JSONData.getString("data").equals("saved")) {
                            JSONArray jsonArray = new JSONArray(JSONData.getString("response"));
                            JSONObject JSONResponse = jsonArray.getJSONObject(0);
                            extraItems.add(new ExtraItem(JSONResponse.getInt("eID"),JSONResponse.getString("eName")));
                            mAdapter.notifyItemInserted(extraItems.size());
                            etExtra.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            HelperMethods.ShowLoadingPopup(myDialog, false);
            Toast.makeText(NewExtrasActivity.this, error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("eName", Objects.requireNonNull(etExtra.getText()).toString());
                params.put("sID", String.valueOf(getNewShop().getIntID()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*private void DELETEIngredient(int position) {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                "http://" + getIpAddress() + "/shops/Register/Ingredient/" + ingredientItems.get(position).getIntID(), null,   //+getUser().getuID()
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response.toString());
                        if (JSONData.getString("data").equals("removed")) {
                            ingredientItems.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            ButtonVisibility(ingredientItems, btnNext);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Loads shops starting with the one closest to user
                },
                error -> {
                    if (error.toString().equals("com.android.volley.TimeoutError")) {
                        Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(objectRequest);

    }

    private void PUTIngredient(int position, String IngredientName, String Price) {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                "http://" + getIpAddress() + "/shops/Register/Ingredient/" + ingredientItems.get(position).getIntID(),
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    ingredientItems.get(position).setStrIngredientName(IngredientName);
                    ingredientItems.get(position).setDblPrice(Double.valueOf(Price));
                    mAdapter.notifyItemChanged(position);
                }, error -> {
            //myDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("iName", IngredientName);
                params.put("iPrice", Price);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/

}
