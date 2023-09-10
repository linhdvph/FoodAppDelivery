package linhdvph25937.fpoly.appfooddelivery.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.activity.AccountActivity;
import linhdvph25937.fpoly.appfooddelivery.activity.SignInActivity;
import linhdvph25937.fpoly.appfooddelivery.activity.SignUpActivity;
import linhdvph25937.fpoly.appfooddelivery.activity.SplashScreen;
import linhdvph25937.fpoly.appfooddelivery.model.User;
import linhdvph25937.fpoly.appfooddelivery.model.Receiver;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private TextView tvLogout, tvAccount;
    private ImageView imgUser;
    private TextView tvUsername, tvEmail;
    private Button btnSignUp, btnSignIn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private View viewBelow;
    private User objUser;
    private String phoneNumber;
    public static final String TAG = "Bau";
    public SettingsFragment() {
        // Required empty public constructor
    }
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initialView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showAndHide();
        getDataUserSignIn();
        signInAndSignUp();
        logOut();
        goToAccountActivity();
    }

    private void getDataUserSignIn() {
        MyRetrofit.retrofit.getUserByPhoneNumber(phoneNumber)
                .enqueue(new Callback<Receiver>() {
                    @Override
                    public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                        if (response.body() != null){
                            objUser = response.body().getData();
                            tvUsername.setText(objUser.getName());
                            tvEmail.setText(objUser.getAddress());
                        }else{
                            Log.e(TAG, "onResponse: Không có dữ liệu");
                        }
                    }

                    @Override
                    public void onFailure(Call<Receiver> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        Toast.makeText(getActivity(), "Không thể lấy dữ liệu người dùng từ server", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToAccountActivity() {
        tvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AccountActivity.class));
            }
        });
    }

    private void logOut() {
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Đăng xuất");
                builder.setMessage("Bạn chắc chắn muốn đăng xuất?");
                builder.setIcon(R.drawable.logout);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor = sharedPreferences.edit();
                        editor.remove("isSignIn");
                        editor.commit();
                        Intent intent = new Intent(getActivity(), SplashScreen.class);
                        // Sử dụng cờ để xóa tất cả các Activity khác trên đường dẫn đến MainActivity
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        //Kết thúc tất cả các Activity hiện tại.
                        getActivity().finishAffinity();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });
    }

    private void signInAndSignUp() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignInActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignUpActivity.class));
            }
        });
    }

    private void showAndHide() {
        if (sharedPreferences.getBoolean("isSignIn", false)){
            tvLogout.setVisibility(View.VISIBLE);
            tvAccount.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
            tvUsername.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.GONE);
            viewBelow.setVisibility(View.VISIBLE);
        }else{
            tvLogout.setVisibility(View.GONE);
            tvAccount.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
            tvUsername.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.VISIBLE);
            imgUser.setImageResource(R.drawable.user);
            viewBelow.setVisibility(View.GONE);
        }
    }

    private void initialView(View view) {
        tvLogout = view.findViewById(R.id.tv_log_out);
        tvUsername = view.findViewById(R.id.tv_name_user_setting);
        tvEmail = view.findViewById(R.id.tv_email_setting);
        btnSignIn = view.findViewById(R.id.btn_sign_in_setting);
        btnSignUp = view.findViewById(R.id.btn_sign_up_setting);
        imgUser = view.findViewById(R.id.img_user_setting);
        viewBelow = view.findViewById(R.id.view_below_logout_setting);
        tvAccount = view.findViewById(R.id.tv_account_setting);

        sharedPreferences = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phone_number_sign_in", "");


    }
}