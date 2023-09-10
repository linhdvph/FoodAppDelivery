package linhdvph25937.fpoly.appfooddelivery.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import linhdvph25937.fpoly.appfooddelivery.fragment.AllProductFragment;
import linhdvph25937.fpoly.appfooddelivery.fragment.RatingProductFragment;

public class FragmentProductHomeAdapter extends FragmentStateAdapter {
    public FragmentProductHomeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = AllProductFragment.newInstance();
                break;
            case 1:
                fragment = RatingProductFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
