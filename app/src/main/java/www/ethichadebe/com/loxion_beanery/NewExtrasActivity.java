package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.getMenuItems;
import static www.ethichadebe.com.loxion_beanery.RegisterShopActivity.getNewShop;

public class NewExtrasActivity extends AppCompatActivity {
    private static boolean isNew = false;
    public static boolean isNew() {
        return isNew;
    }

    public static void setIsNew(boolean isNew) {
        NewExtrasActivity.isNew = isNew;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_extras);
    }

    public void next(View view) {
        getNewShop().setMenuItems(getMenuItems());
        isNew = true;
        startActivity(new Intent(NewExtrasActivity.this, MyShopsActivity.class));
    }

    public void back(View view) {
        finish();
    }
}
