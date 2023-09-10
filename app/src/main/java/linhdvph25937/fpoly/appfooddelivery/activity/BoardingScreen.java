package linhdvph25937.fpoly.appfooddelivery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.adapter.FragmentAdapter;

public class BoardingScreen extends AppCompatActivity {
    private TextView tvSkip, tvNext;
    private ViewPager2 viewPager2;
    private DotsIndicator dotsIndicator;
    private FragmentAdapter fragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(linhdvph25937.fpoly.appfooddelivery.R.layout.activity_boarding_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AnhXa();
        fragmentAdapter = new FragmentAdapter(this);
        viewPager2.setAdapter(fragmentAdapter);
        dotsIndicator.attachTo(viewPager2);
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager2.setCurrentItem(2);
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager2.getCurrentItem() < 2){
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                }
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 2){
                    tvNext.setVisibility(View.INVISIBLE);
                    tvSkip.setVisibility(View.INVISIBLE);
                }else{
                    tvNext.setVisibility(View.VISIBLE);
                    tvSkip.setVisibility(View.VISIBLE);
                }
            }
        });
    }



    private void AnhXa() {
        tvSkip = findViewById(R.id.tvSkip);
        tvNext = findViewById(R.id.tvNext);
        viewPager2 = findViewById(R.id.viewPager2);
        dotsIndicator = findViewById(R.id.dots_indicator);
    }
}