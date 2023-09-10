package linhdvph25937.fpoly.appfooddelivery.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
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
import linhdvph25937.fpoly.appfooddelivery.fomatter.MyRegex;
import linhdvph25937.fpoly.appfooddelivery.model.User;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private EditText edPhoneNumber;
    private LinearLayout back;
    private Button btnSignUp;
    private ConstraintLayout constraintGoogle, constraintFacebook;
    private TextView tvSignIn, tvErrorPhoneNumber;
    private FirebaseAuth mAuth;
    public static final String TAG = "Prrr";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private GoogleSignInOptions options;
    private GoogleSignInClient client;
    private ActivityResultLauncher<Intent> launcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(linhdvph25937.fpoly.appfooddelivery.R.layout.activity_sign_up);

        initialView();
        backToSignIn();
        sendVerificationCode();
        signUpWithGoogleAccount();
    }

    private void signUpWithGoogleAccount() {
        constraintGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = client.getSignInIntent();
                launcher.launch(intent);
            }
        });
    }

    private void backToSignIn() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });
    }

    private void sendVerificationCode() {
       btnSignUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String phoneNumber = edPhoneNumber.getText().toString();
               if (TextUtils.isEmpty(phoneNumber)){
//            Toast.makeText(this, "Phone number can't empty", Toast.LENGTH_SHORT).show();
//            edPhoneNumber.setError("Phone number can't empty");
                   tvErrorPhoneNumber.setVisibility(View.VISIBLE);
                   tvErrorPhoneNumber.setText("Vui lòng nhập số điện thoại");
//            edPhoneNumber.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.selected_nav));
                   return;
               }
               if (!phoneNumber.matches(MyRegex.PHONE_NUMBER_VN_REGEX)){
//            Toast.makeText(this, "Phone number is invalid", Toast.LENGTH_SHORT).show();
//            edPhoneNumber.setError("Phone number is invalid");
                   tvErrorPhoneNumber.setVisibility(View.VISIBLE);
                   tvErrorPhoneNumber.setText("Số điện thoại không đúng định dạng");
//            edPhoneNumber.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.selected_nav));
                   return;
               }
               PhoneAuthOptions options =
                       PhoneAuthOptions.newBuilder(mAuth)
                               .setPhoneNumber("+84"+edPhoneNumber.getText().toString())
                               .setTimeout(60L, TimeUnit.SECONDS)
                               .setActivity(SignUpActivity.this)
                               .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                   @Override
                                   public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                       Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                                       signInWithPhoneAuthCredential(phoneAuthCredential);
                                   }

                                   @Override
                                   public void onVerificationFailed(@NonNull FirebaseException e) {
                                       Log.d(TAG, "onVerificationFailed:" + e);
                                       Toast.makeText(SignUpActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
                                   }

                                   @Override
                                   public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                       super.onCodeSent(s, forceResendingToken);
                                       Intent intent = new Intent(SignUpActivity.this, VerifyPhoneNumberActivity.class);
                                       intent.putExtra("phone", edPhoneNumber.getText().toString());
                                       intent.putExtra("verificationId", s);
                                       intent.putExtra("activity", "SignUp");
                                       startActivity(intent);
                                   }
                               })
                               .build();
               PhoneAuthProvider.verifyPhoneNumber(options);
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
                                Toast.makeText(SignUpActivity.this, "Sign in with credential failure", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void GoToMainActivity(FirebaseUser user) {
        String phoneNumber = edPhoneNumber.getText().toString();
        MyRetrofit.retrofit.addUser(new User("","",phoneNumber,"","","Ngõ 123, Phương Canh, Thị Cấm, Nam Từ Liêm, Hà Nội",1,"","", getDateNow()))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()){
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.putExtra("user_firebase", user);
                            editor.putBoolean("isSignIn", true);
                            editor.putString("phone_number_sign_in", phoneNumber);
                            editor.commit();
                            startActivity(intent);
                        }else{
                            Toast.makeText(SignUpActivity.this, "Can't add user", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(SignUpActivity.this, "Call api error when add user in sign up activity", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure: " + t);
                    }
                });
    }

    private String getDateNow(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        String currentDateTime = sdf.format(date);
        return currentDateTime;
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            updateUI(account);
        }catch (ApiException e){
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void initialView() {
        back = findViewById(R.id.linear_layout_arrow_back_sign_up);
        edPhoneNumber = findViewById(R.id.ed_phone_number_sign_up);
        btnSignUp = findViewById(R.id.btn_sign_up);
        constraintGoogle = findViewById(R.id.constraint_sign_up_with_google);
        constraintFacebook = findViewById(R.id.constraint_sign_up_with_facebook);
        tvSignIn = findViewById(R.id.tv_go_sign_in);
        tvErrorPhoneNumber = findViewById(R.id.tv_error_phone_number);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(SignUpActivity.this, options);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                }else{
                    Toast.makeText(SignUpActivity.this, "Đăng nhập bằng tài khoản google thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null){
            MyRetrofit.retrofit.addUser(new User("","","","","","",1,account.getEmail(),"", getDateNow()))
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()){
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.putExtra("google_address", account.getEmail());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(SignUpActivity.this, "Call api error when add user in sign up activity", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onFailure: " + t);
                        }
                    });
        }
    }
}