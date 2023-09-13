package linhdvph25937.fpoly.appfooddelivery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.fomatter.MyRegex;
import linhdvph25937.fpoly.appfooddelivery.model.User;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private EditText edPasswd, edPhoneNumber;
    private ImageView imgShowAndHidePasswd;
    private TextView tvSignInWithSMS, tvSignInWithPasswd, tvCreateHere, tvErrorPhoneNumber, tvErrorPasswd;
    private LinearLayout back;
    private Button btnSignIn;
    private FirebaseAuth mAuth;
    public static final String TAG = SignInActivity.class.toString();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);

        initialView();
        goToSignUpActivity();
        showAndHidePasswd();
        transSMSorPasswd();
        setBtnSignIn();
    }

    private void setBtnSignIn() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edPhoneNumber.getText().toString();
                String passwd = edPasswd.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)){
                    tvErrorPhoneNumber.setVisibility(View.VISIBLE);
                    tvErrorPhoneNumber.setText("Vui lòng nhập số điện thoại");
                    return;
                }else{
                    tvErrorPhoneNumber.setVisibility(View.INVISIBLE);
                }
                if (!phoneNumber.matches(MyRegex.PHONE_NUMBER_VN_REGEX)){
                    tvErrorPhoneNumber.setVisibility(View.VISIBLE);
                    tvErrorPhoneNumber.setText("Số điện thoại không đúng định dạng");
                    return;
                }else{
                    tvErrorPhoneNumber.setVisibility(View.INVISIBLE);
                }

                if (tvSignInWithPasswd.isShown()){
                    signInWithSMS(phoneNumber);
                }else if(tvSignInWithSMS.isShown()){
                    signInWithPasswd(phoneNumber,passwd);
                }
            }
        });
    }

    private void signInWithPasswd(String phoneNumber, String passwd) {
        if (TextUtils.isEmpty(passwd)){
            tvErrorPasswd.setVisibility(View.VISIBLE);
            tvErrorPasswd.setText("Vui lòng nhập mật khẩu");
            return;
        }else{
            tvErrorPasswd.setVisibility(View.INVISIBLE);
        }
        MyRetrofit.retrofit.signIn(phoneNumber, passwd).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null){
                    Log.d(TAG, "onResponse: " + response.body());
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    editor.putBoolean("isSignIn", true);
                    editor.putString("phone_number_sign_in", phoneNumber);
                    editor.commit();
                    startActivity(intent);
                }else{
                    Toast.makeText(SignInActivity.this, "Thông tin không chính xác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
                Toast.makeText(SignInActivity.this, "Can't sign in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signInWithSMS(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setActivity(SignInActivity.this)
                .setPhoneNumber("+84"+phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
                        signInWithAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.w(TAG, "onVerificationFailed: ", e);
                        Toast.makeText(SignInActivity.this, "Verification failed in SignInActivity", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        Intent intent = new Intent(SignInActivity.this, VerifyPhoneNumberActivity.class);
                        intent.putExtra("phone_sign_in", edPhoneNumber.getText().toString());
                        intent.putExtra("verificationId", s);
                        intent.putExtra("activity", "SignIn");
                        startActivity(intent);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            GoToMainActivity(user);
                        }else{
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void GoToMainActivity(FirebaseUser user) {
        String phoneNumber = edPhoneNumber.getText().toString();
        String passwd = edPasswd.getText().toString();
        MyRetrofit.retrofit.signIn(phoneNumber, passwd)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body() != null){
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.putExtra("user_firebase", user);
                            editor.putBoolean("isSignIn", true);
                            editor.putString("phone_number_sign_in", phoneNumber);
                            editor.commit();
                            startActivity(intent);
                        }else{
                            Toast.makeText(SignInActivity.this, "Số điện thoại hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        Toast.makeText(SignInActivity.this, "Không thể đăng nhập", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToSignUpActivity() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvCreateHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    private void transSMSorPasswd() {
        tvSignInWithSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSignInWithSMS.setVisibility(View.GONE);
                edPasswd.setVisibility(View.GONE);
                tvSignInWithPasswd.setVisibility(View.VISIBLE);
                imgShowAndHidePasswd.setVisibility(View.GONE);
            }
        });

        tvSignInWithPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSignInWithSMS.setVisibility(View.VISIBLE);
                edPasswd.setVisibility(View.VISIBLE);
                tvSignInWithPasswd.setVisibility(View.GONE);
                imgShowAndHidePasswd.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showAndHidePasswd() {
       imgShowAndHidePasswd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (edPasswd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                   edPasswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                   imgShowAndHidePasswd.setImageResource(R.drawable.hide);
                   edPasswd.setSelection(edPasswd.getText().length());
               }else {
                   edPasswd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                   imgShowAndHidePasswd.setImageResource(R.drawable.view);
                   edPasswd.setSelection(edPasswd.getText().length());
               }
           }
       });
    }

    private void initialView() {
        edPasswd = findViewById(R.id.ed_passwd_sign_in);
        edPhoneNumber = findViewById(R.id.ed_phone_number_sign_in);
        imgShowAndHidePasswd = findViewById(R.id.img_show_or_hide_passwd);
        tvSignInWithSMS = findViewById(R.id.tv_sign_in_with_SMS);
        tvSignInWithPasswd = findViewById(R.id.tv_sign_in_with_passwd);
        tvErrorPhoneNumber = findViewById(R.id.tv_error_phone_number_sign_in);
        tvErrorPasswd = findViewById(R.id.tv_error_passwd_sign_in);
        tvCreateHere = findViewById(R.id.tv_go_sign_up);
        back = findViewById(R.id.linear_layout_arrow_back_sign_in);
        btnSignIn = findViewById(R.id.btn_sign_in);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}