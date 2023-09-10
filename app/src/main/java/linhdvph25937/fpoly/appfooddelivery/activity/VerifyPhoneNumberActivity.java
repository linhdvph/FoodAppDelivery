package linhdvph25937.fpoly.appfooddelivery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.model.User;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhoneNumberActivity extends AppCompatActivity {
    private ImageView imgBack;
    private EditText edNum1, edNum2, edNum3, edNum4, edNum5, edNum6;
    private Button btnConfirm;
    private TextView tvReSendCode, tvCountDownTime, tvPhoneVerification;
    public static final String TAG = "Prrr";
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private int timeLeft = 60;//Thời gian đếm ngược trong 60s
    private String verificationId, phoneNumber, phoneNumberSignIn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(linhdvph25937.fpoly.appfooddelivery.R.layout.activity_verify_phone_number);

        initialView();
        backSignUp();
        confirmVerification();
        startTime();
        reSendCode();
    }

    private void backSignUp() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void reSendCode() {
        tvReSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime();
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+84"+phoneNumber)
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(VerifyPhoneNumberActivity.this)
                                .setForceResendingToken(mResendToken)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                                        signInWithPhoneAuthCredential(phoneAuthCredential);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Log.d(TAG, "onVerificationFailed:" + e);
                                        Toast.makeText(VerifyPhoneNumberActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(s, forceResendingToken);
                                        mResendToken = forceResendingToken;
                                        verificationId = s;
                                    }
                                })
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    }

    private void startTime() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (timeLeft == 0){
                    tvCountDownTime.setVisibility(View.GONE);
                    tvReSendCode.setVisibility(View.VISIBLE);
                }else{
                    tvCountDownTime.setText("Please wait "+timeLeft+" seconds to resend the code");
                    timeLeft--;
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void confirmVerification() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edNum1.getText().toString()
                        + edNum2.getText().toString()
                        + edNum3.getText().toString()
                        + edNum4.getText().toString()
                        + edNum5.getText().toString()
                        + edNum6.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "signInWithCredential: success");
                            FirebaseUser user = task.getResult().getUser();
                            GoToMainActivity(user);
                        }else{
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(VerifyPhoneNumberActivity.this, "Sign in with credential failure", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void GoToMainActivity(FirebaseUser user) {
        String activity = getIntent().getStringExtra("activity");
        if (activity.equals("SignUp")){
            MyRetrofit.retrofit.addUser(new User("","",phoneNumber,"","","Ngõ 123, Phương Canh, Thị Cấm, Nam Từ Liêm, Hà Nội",1,"","", getDateNow()))
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()){
                                Intent intent = new Intent(VerifyPhoneNumberActivity.this, MainActivity.class);
                                intent.putExtra("user_firebase", user);
                                editor.putBoolean("isSignIn", true);
                                editor.putString("phone_number_sign_in", phoneNumber);
                                editor.commit();
                                startActivity(intent);
                            }else{
                                Toast.makeText(VerifyPhoneNumberActivity.this, "Can't add user", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(VerifyPhoneNumberActivity.this, "Call api error while verification in VerifyPhoneNumberActivity", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onFailure: " + t);
                        }
                    });
        }
        if (activity.equals("SignIn")){
            MyRetrofit.retrofit.signIn(phoneNumberSignIn, "")
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.body() != null){
                                Intent intent = new Intent(VerifyPhoneNumberActivity.this, MainActivity.class);
                                intent.putExtra("user_firebase", user);
                                editor.putBoolean("isSignIn", true);
                                editor.putString("phone_number_sign_in", phoneNumber);
                                editor.commit();
                                startActivity(intent);
                            }else{
                                Toast.makeText(VerifyPhoneNumberActivity.this, "Can't sign in", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e(TAG, "onFailure: " + t);
                            Toast.makeText(VerifyPhoneNumberActivity.this, "Can't sign in", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }
    
    private String getDateNow(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        String dateTimeNow = sdf.format(date);
        return dateTimeNow;
    }

    private void initialView() {
        imgBack = findViewById(R.id.img_arrow_back_verify_phone_number);
        edNum1 = findViewById(R.id.ed_num1);
        edNum2 = findViewById(R.id.ed_num2);
        edNum3 = findViewById(R.id.ed_num3);
        edNum4 = findViewById(R.id.ed_num4);
        edNum5 = findViewById(R.id.ed_num5);
        edNum6 = findViewById(R.id.ed_num6);
        btnConfirm = findViewById(R.id.btn_confirm_verification_id);
        tvReSendCode = findViewById(R.id.tv_re_send_code);
        tvCountDownTime = findViewById(R.id.tv_count_down_time);
        tvPhoneVerification = findViewById(R.id.tv_phone_verification);

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(getIntent() != null){
            phoneNumber = getIntent().getStringExtra("phone");
            phoneNumberSignIn = getIntent().getStringExtra("phone_sign_in");
            verificationId = getIntent().getStringExtra("verificationId");
        }
        if (!TextUtils.isEmpty(phoneNumber)){
            tvPhoneVerification.setText(phoneNumber);
        }
        edNum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    edNum2.requestFocus();
                }else{
                    edNum1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edNum2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()){
                    edNum2.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    edNum3.requestFocus();
                }else{
                    edNum1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edNum3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    edNum4.requestFocus();
                }else{
                    edNum2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edNum4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    edNum5.requestFocus();
                }else{
                    edNum3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edNum5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()){
                    edNum6.requestFocus();
                }else{
                    edNum4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edNum6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()){
                    edNum5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
    }
}