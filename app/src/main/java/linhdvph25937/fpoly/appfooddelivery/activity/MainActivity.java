package linhdvph25937.fpoly.appfooddelivery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.fragment.CartFragment;
import linhdvph25937.fpoly.appfooddelivery.fragment.ReceiptFragment;
import linhdvph25937.fpoly.appfooddelivery.fragment.HomeFragment;
import linhdvph25937.fpoly.appfooddelivery.fragment.SettingsFragment;
import linhdvph25937.fpoly.appfooddelivery.model.Cart;
import linhdvph25937.fpoly.appfooddelivery.model.Receiver;
import linhdvph25937.fpoly.appfooddelivery.model.User;
import linhdvph25937.fpoly.appfooddelivery.ultil.CheckConnection;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String phoneNumberFromSignIn;
    private User objUser;
    public static final String TAG = MainActivity.class.toString();
    public static final int REQUEST_CODE = 2003;
    private FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Initial();
        CheckLocation();
        FragmentManagement();
        getUserInfoLogin();
        eventFromAnotherActivity();

    }

    private void eventFromAnotherActivity() {
        Intent intent = getIntent();
        String fragment = intent.getStringExtra("fragment");
        if (fragment != null && fragment.equals("CartFragment")){
            ChooseFragment(CartFragment.newInstance());
            bottomNavigationView.setSelectedItemId(R.id.menu_cart);
        }else if(fragment != null && fragment.equals("toHomeFragment")){
            ChooseFragment(HomeFragment.newInstance());
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
        }else if(fragment != null && fragment.equals("ReceiptFragment")){
            ChooseFragment(ReceiptFragment.newInstance());
            bottomNavigationView.setSelectedItemId(R.id.menu_receipt);
        }
    }

    private void FragmentManagement() {
        ChooseFragment(HomeFragment.newInstance());//Thiết lập homeFragment xuất hiện đầu tiên khi vào ứng dụng.
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        ChooseFragment(HomeFragment.newInstance());
                        break;
                    case R.id.menu_cart:
                        ChooseFragment(CartFragment.newInstance());
                        break;
                    case R.id.menu_receipt:
                        ChooseFragment(ReceiptFragment.newInstance());
                        break;
                    case R.id.menu_settings:
                        ChooseFragment(SettingsFragment.newInstance());
                        break;
                }
                return true;
            }
        });
    }

    private void getUserInfoLogin() {
        MyRetrofit.retrofit.getUserByPhoneNumber(phoneNumberFromSignIn)
                .enqueue(new Callback<Receiver>() {
                    @Override
                    public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                        if (response.body() != null){
                            objUser = response.body().getData();
                            if (objUser.getRole() == 0){
                                bottomNavigationView.getMenu().findItem(R.id.menu_cart).setVisible(false);
                            }else{
                                bottomNavigationView.getMenu().findItem(R.id.menu_cart).setVisible(true);
                            }
                            editor = sharedPreferences.edit();
                            editor.putString("id_user_login", objUser.getId());
                            editor.putString("name_user_login", objUser.getName());
                            editor.putString("passwd_user_login", objUser.getPassword());
                            editor.putString("email_user_login", objUser.getEmail());
                            editor.putString("address_user_login", objUser.getAddress());
                            editor.putString("google_user_login", objUser.getGoogle());
                            editor.putString("facebook_user_login", objUser.getFacebook());
                            editor.putString("avatar_user_login", objUser.getAvatar());
                            editor.putInt("role_user_login", objUser.getRole());
                            editor.commit();
                            Log.e(TAG, "onResponse: " + response.body());
                        }else{
                            Log.e(TAG, "onResponse: Call api error when get data user with phone number in Main Activity");
                        }
                    }

                    @Override
                    public void onFailure(Call<Receiver> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        Toast.makeText(MainActivity.this, "Call api error while get data user in Main Activity ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ChooseFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    private void CheckLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                            editor = sharedPreferences.edit();
                            editor.putString("address", addresses.get(0).getAddressLine(0));
                            editor.commit();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
        }else{
            requestPermission();
        }
    }

    private void requestPermission(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                CheckLocation();
            }
        }
    }

    private void Initial() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        phoneNumberFromSignIn = sharedPreferences.getString("phone_number_sign_in", "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor = sharedPreferences.edit();
        editor.putBoolean("isDestroy", true);
        editor.commit();
    }
}