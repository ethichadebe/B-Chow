package www.ethichadebe.com.loxion_beanery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class ProfileFragment extends Fragment {
    private LinearLayout llBack;
    private TextView tvShops, tvNameSur, tvEmail, tvNumber, tvDOB, tvSex;
    private CardView cvEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_profile, container, false);

        llBack = v.findViewById(R.id.llBack);
        tvShops = v.findViewById(R.id.tvShops);
        cvEdit = v.findViewById(R.id.cvEdit);

        tvNameSur = v.findViewById(R.id.tvNameSur);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvNumber = v.findViewById(R.id.tvNumber);
        tvDOB = v.findViewById(R.id.tvDOB);
        tvSex = v.findViewById(R.id.tvSex);

        tvNameSur.setText(getUser().getuName() + " " + getUser().getuSurname());
        tvEmail.setText(getUser().getuEmail());
        tvNumber.setText(getUser().getuNumber());
        tvDOB.setText(getUser().getuDOB());
        tvSex.setText(getUser().getuSex());

        tvShops.setOnClickListener(view -> startActivity(new Intent(getActivity(), MyShopsActivity.class)));

        cvEdit.setOnClickListener(view -> startActivity(new Intent(getActivity(), EditUserProfileActivity.class)));
        return v;
    }
}
