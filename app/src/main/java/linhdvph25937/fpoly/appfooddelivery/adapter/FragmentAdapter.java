package linhdvph25937.fpoly.appfooddelivery.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import linhdvph25937.fpoly.appfooddelivery.fragment.Boarding1Fragment;
import linhdvph25937.fpoly.appfooddelivery.fragment.Boarding2Fragment;
import linhdvph25937.fpoly.appfooddelivery.fragment.Boarding3Fragment;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = Boarding1Fragment.newInstance();
                break;
            case 1:
                fragment = Boarding2Fragment.newInstance();
                break;
            case 2:
                fragment = Boarding3Fragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
