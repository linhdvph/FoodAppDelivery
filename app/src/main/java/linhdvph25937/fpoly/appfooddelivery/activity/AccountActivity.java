package linhdvph25937.fpoly.appfooddelivery.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.fomatter.MyRegex;
import linhdvph25937.fpoly.appfooddelivery.model.User;
import linhdvph25937.fpoly.appfooddelivery.model.Receiver;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {
    private TextView tvChangePasswd, tvChangeNameUser, tvChangeAddress, tvChangePhoneNumber;
    private ImageView imgBack;
    private String phoneNumberFromSignIn;
    private SharedPreferences sharedPreferences;
    public static final String TAG = "Bau";
    private User objUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account);

        initialView();
        onBack();
        getDataUserWithPhoneNumber();
        showDialogChangePasswd();
        showDialogChangeNameUser();
        showDialogChangeAddressUser();
        showDialogChangePhoneNumber();
    }

    private void showDialogChangePhoneNumber() {
        tvChangePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AccountActivity.this, android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_update_phone_number_user);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                ImageView imgBack = dialog.findViewById(R.id.img_arrow_back_confirm_update_phone_number_user);
                EditText edPhoneNumber = dialog.findViewById(R.id.ed_update_phone_number_user);
                TextView tvErrorPhoneNumber = dialog.findViewById(R.id.tv_error_update_phone_number_user);
                Button btnSave = dialog.findViewById(R.id.btn_confirm_update_phone_number_name);

                edPhoneNumber.setText(objUser.getPhoneNumber());
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phoneNumber = edPhoneNumber.getText().toString();
                        if (TextUtils.isEmpty(phoneNumber)){
                            tvErrorPhoneNumber.setVisibility(View.VISIBLE);
                            tvErrorPhoneNumber.setText("Vui lòng nhập số điện thoại của bạn.");
                            return;
                        }else if(!phoneNumber.matches(MyRegex.PHONE_NUMBER_VN_REGEX)){
                            tvErrorPhoneNumber.setVisibility(View.VISIBLE);
                            tvErrorPhoneNumber.setText("Vui lòng nhập đúng định dạng số điện thoại.");
                            return;
                        }else{
                            tvErrorPhoneNumber.setVisibility(View.GONE);
                        }
                        MyRetrofit.retrofit.updatePhoneNumberUser(objUser.getId(), phoneNumber)
                                .enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if (response.body() != null){
                                            openDialogLogout();
                                            dialog.dismiss();
                                        }else{
                                            Toast.makeText(AccountActivity.this, "Không thể cập nhật số điện thoại người dùng.", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Log.d(TAG, "onFailure: "+t);
                                        Toast.makeText(AccountActivity.this, "Lỗi server khi cập nhật số điện thoại người dùng.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

                imgBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void showDialogChangeAddressUser() {
        tvChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AccountActivity.this, android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_update_address_user);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                ImageView imgBack = dialog.findViewById(R.id.img_arrow_back_confirm_update_address_user);
                EditText edAddress = dialog.findViewById(R.id.ed_update_address_user);
                TextView tvErrorAddress = dialog.findViewById(R.id.tv_error_update_address_user);
                Button btnSave = dialog.findViewById(R.id.btn_confirm_update_address_name);

                edAddress.setText(objUser.getAddress());
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String address = edAddress.getText().toString();
                        if (TextUtils.isEmpty(address)){
                            tvErrorAddress.setVisibility(View.VISIBLE);
                            tvErrorAddress.setText("Vui lòng nhập địa chỉ của bạn.");
                            return;
                        }else{
                            tvErrorAddress.setVisibility(View.GONE);
                        }
                        MyRetrofit.retrofit.updateAddressUser(objUser.getId(), address)
                                .enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if (response.body() != null){
                                            Toast.makeText(AccountActivity.this, "Cập nhật địa chỉ người dùng thành công!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }else{
                                            Toast.makeText(AccountActivity.this, "Không thể cập nhật địa chỉ người dùng.", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Log.d(TAG, "onFailure: "+t);
                                        Toast.makeText(AccountActivity.this, "Lỗi server khi cập nhật địa chỉ người dùng.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

                imgBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void showDialogChangeNameUser() {
        tvChangeNameUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AccountActivity.this, android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_update_name_user);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                ImageView imgBack = dialog.findViewById(R.id.img_arrow_back_confirm_update_name_user);
                EditText edName = dialog.findViewById(R.id.ed_update_name_user);
                TextView tvErrorName = dialog.findViewById(R.id.tv_error_update_name_user);
                Button btnSave = dialog.findViewById(R.id.btn_confirm_update_user_name);

                edName.setText(objUser.getName());
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edName.getText().toString();
                        if (TextUtils.isEmpty(name)){
                            tvErrorName.setVisibility(View.VISIBLE);
                            tvErrorName.setText("Vui lòng nhập tên của bạn.");
                            return;
                        }else if(!name.matches(MyRegex.NAME_REGEX)){
                            tvErrorName.setVisibility(View.VISIBLE);
                            tvErrorName.setText("Tên phải có độ dài từ 6 - 20 kí tự chữ, số, gạch chân hoặc dấu châm.");
                            return;
                        }else{
                            tvErrorName.setVisibility(View.GONE);
                        }
                        MyRetrofit.retrofit.updateNameUser(objUser.getId(), name)
                                .enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if (response.body() != null){
                                            Toast.makeText(AccountActivity.this, "Cập nhật tên người dùng thành công!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }else{
                                            Toast.makeText(AccountActivity.this, "Không thể cập nhật tên người dùng.", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Log.d(TAG, "onFailure: "+t);
                                        Toast.makeText(AccountActivity.this, "Lỗi server khi cập nhật tên người dùng.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

                imgBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void showDialogChangePasswd() {
        tvChangePasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AccountActivity.this, android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_confirm_passwd);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                ImageView imgBack = dialog.findViewById(R.id.img_arrow_back_confirm_passwd);
                EditText edConfirmPasswd = dialog.findViewById(R.id.ed_confirm_passwd);
                Button btnConfirmPasswd = dialog.findViewById(R.id.btn_confirm_passwd);
                TextView tvErrorConfirmPasswd = dialog.findViewById(R.id.tv_error_confirm_passwd);
                ImageView imgShowAndHidePasswd = dialog.findViewById(R.id.img_show_or_hide_passwd_confirm_passwd);
                showAndHidePasswd(imgShowAndHidePasswd, edConfirmPasswd);

                btnConfirmPasswd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String passwd = edConfirmPasswd.getText().toString();
                        if (TextUtils.isEmpty(passwd)){
                            tvErrorConfirmPasswd.setVisibility(View.VISIBLE);
                            tvErrorConfirmPasswd.setText("Vui lòng nhập mật khẩu hiện tại của bạn.");
                            return;
                        }else if (!passwd.equals(objUser.getPassword())){
                            tvErrorConfirmPasswd.setVisibility(View.VISIBLE);
                            tvErrorConfirmPasswd.setText("Mật khẩu hiện tại không đúng");
                            return;
                        }else{
                            tvErrorConfirmPasswd.setVisibility(View.GONE);
                        }
                        Toast.makeText(AccountActivity.this, "Xác nhận mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        openDialogChangePasswd();
                        dialog.dismiss();
                    }
                });
                imgBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void openDialogChangePasswd() {
        Dialog dialog = new Dialog(AccountActivity.this, android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_change_passwd_account);
        Window window = dialog.getWindow();//Chịu trách nhiệm hiển thị giao diện người dùng.
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        ImageView imgBack = dialog.findViewById(R.id.img_arrow_back_change_passwd);
        EditText edNewPasswd, edReNewPasswd;
        edNewPasswd = dialog.findViewById(R.id.ed_new_passwd);
        edReNewPasswd = dialog.findViewById(R.id.ed_re_new_passwd);
        Button btnConfirm = dialog.findViewById(R.id.btn_change_passwd);
        TextView tvErrorNewPasswd = dialog.findViewById(R.id.tv_error_new_passwd_change);
        TextView tvErrorReNewPasswd = dialog.findViewById(R.id.tv_error_re_new_passwd_change);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passNew = edNewPasswd.getText().toString();
                String passReNew = edReNewPasswd.getText().toString();
                String idUser = objUser.getId();
                if (TextUtils.isEmpty(passNew)){
                    tvErrorNewPasswd.setVisibility(View.VISIBLE);
                    tvErrorNewPasswd.setText("Vui lòng nhập mật khẩu.");
                    return;
                }else if(!passNew.matches(MyRegex.PASSWORD_REGEX)){
                    tvErrorNewPasswd.setVisibility(View.VISIBLE);
                    tvErrorNewPasswd.setText("Mật khẩu phải có độ dài từ 6 - 20 kí tự trong đó có cả chữ và số");
                    return;
                }else if(TextUtils.isEmpty(passReNew)){
                    tvErrorReNewPasswd.setVisibility(View.VISIBLE);
                    tvErrorReNewPasswd.setText("Vui lòng điền đầy đủ thông tin.");
                    return;
                }else if(!passReNew.equals(passNew)){
                    tvErrorReNewPasswd.setVisibility(View.VISIBLE);
                    tvErrorReNewPasswd.setText("Mật khẩu nhập lại không chính xác");
                    return;
                }else{
                    tvErrorNewPasswd.setVisibility(View.GONE);
                    tvErrorReNewPasswd.setVisibility(View.GONE);
                }
                MyRetrofit.retrofit.updatePasswdUser(idUser, passNew)
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.body() != null){
                                    openDialogLogout();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(AccountActivity.this, "Không thể cập nhật mật khẩu người dùng!", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onFailure: " + t);
                            }
                        });
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openDialogLogout() {
        new AlertDialog.Builder(AccountActivity.this)
                .setTitle("Đăng xuất")
                .setMessage("Cập nhật thành công, vui lòng đăng nhập lại!")
                .setIcon(R.drawable.logout)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(AccountActivity.this, SignInActivity.class));
                        finish();
                    }
                })
                .show();
    }

    private void showAndHidePasswd(ImageView imgShowAndHidePasswd, EditText edPasswd) {
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

    private void getDataUserWithPhoneNumber() {
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        phoneNumberFromSignIn = sharedPreferences.getString("phone_number_sign_in", "");
        MyRetrofit.retrofit.getUserByPhoneNumber(phoneNumberFromSignIn)
                .enqueue(new Callback<Receiver>() {
                    @Override
                    public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                        if (response.body() != null){
                            objUser = response.body().getData();
                            Log.e(TAG, "onResponse: " + response.body());
                        }else{
                            Toast.makeText(AccountActivity.this, "Không lấy được thông tin người dùng theo số điện thoại.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Receiver> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        Toast.makeText(AccountActivity.this, "Lỗi server khi lấy thông tin người dùng theo số điện thoại.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void onBack() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initialView() {
        tvChangePasswd = findViewById(R.id.tv_change_passwd_account);
        tvChangeNameUser = findViewById(R.id.tv_change_name_user_account);
        tvChangePhoneNumber = findViewById(R.id.tv_change_phone_number_account_setting);
        tvChangeAddress = findViewById(R.id.tv_change_address_account_setting);
        imgBack = findViewById(R.id.img_arrow_back_account);
    }
}