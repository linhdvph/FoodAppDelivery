package linhdvph25937.fpoly.appfooddelivery.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import linhdvph25937.fpoly.appfooddelivery.R;

public class SplashScreen extends AppCompatActivity {
    private boolean isFirstTime;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(linhdvph25937.fpoly.appfooddelivery.R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CheckIsFirstTime();
                finish();
            }
        }, 2000);
    }

    private void CheckIsFirstTime() {
        isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        if (isFirstTime){
            startActivity(new Intent(SplashScreen.this, BoardingScreen.class));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.commit();
            finish();
        }else{
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
    }
}