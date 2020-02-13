package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OrdersFragment extends Fragment {
    private View vLeft, vRight, vBottomRight, vBottomLeft;
    private RelativeLayout rlLeft, rlRight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_orders,container, false);
        vBottomLeft = v.findViewById(R.id.vBottomLeft);
        vBottomRight = v.findViewById(R.id.vBottomRight);
        vLeft = v.findViewById(R.id.vLeft);
        vRight = v.findViewById(R.id.vRight);
        rlLeft = v.findViewById(R.id.rlLeft);
        rlRight = v.findViewById(R.id.rlRight);

        setVisibility(View.VISIBLE, View.GONE);

        rlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.VISIBLE, View.GONE);
            }
        });
        rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.GONE, View.VISIBLE);
            }
        });


        return v;
    }
    private void setVisibility(int Left, int Right) {
        vRight.setVisibility(Right);
        vLeft.setVisibility(Left);
        vBottomLeft.setVisibility(Left);
        vBottomRight.setVisibility(Right);
    }
}
