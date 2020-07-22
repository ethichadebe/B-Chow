package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.messaging.FirebaseMessaging;

import static util.HelperMethods.DisplayImage;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private LinearLayout llEditProfile;
    static boolean isLogout = false;
    private TextView tvNameSur, tvEmail, tvNumber, tvAddress, tvSex, tvDeactivate, tvLogOut;
    private ImageView civProfilePicture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_profile, container, false);

        if (getUser() == null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        llEditProfile = v.findViewById(R.id.llEditProfile);

        tvNameSur = v.findViewById(R.id.tvNameSur);
        civProfilePicture = v.findViewById(R.id.civProfilePicture);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvNumber = v.findViewById(R.id.tvNumber);
        tvAddress = v.findViewById(R.id.tvAddress);
        tvSex = v.findViewById(R.id.tvSex);
        tvDeactivate = v.findViewById(R.id.tvDeactivate);
        tvLogOut = v.findViewById(R.id.tvLogOut);

        tvNameSur.setText(getUser().getuName() + " " + getUser().getuSurname());
        tvEmail.setText(getUser().getuEmail());
        tvNumber.setText(getUser().getuNumber());
        tvAddress.setText(getUser().getuAddress());
        tvSex.setText(getUser().getuSex());
        DisplayImage(civProfilePicture, getUser().getuPicture());

        llEditProfile.setOnClickListener(view -> startActivity(new Intent(getActivity(), EditUserProfileActivity.class)));
        tvLogOut.setOnClickListener(view -> {
            isLogout = true;
            FirebaseMessaging.getInstance().unsubscribeFromTopic(String.valueOf(getUser().getuID()))
                    .addOnCompleteListener(task -> {
                        String msg = "msg_subscribed";
                        if (!task.isSuccessful()) {
                            msg = "msg_subscribe_failed";
                        }
                        Log.d(TAG, msg);
                    });
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });
        return v;
    }
}
