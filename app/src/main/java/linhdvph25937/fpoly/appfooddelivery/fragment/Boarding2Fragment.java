package linhdvph25937.fpoly.appfooddelivery.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linhdvph25937.fpoly.appfooddelivery.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Boarding2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Boarding2Fragment extends Fragment {
    public Boarding2Fragment() {
        // Required empty public constructor
    }
    public static Boarding2Fragment newInstance() {
        Boarding2Fragment fragment = new Boarding2Fragment();
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
        return inflater.inflate(R.layout.fragment_boarding2, container, false);
    }
}