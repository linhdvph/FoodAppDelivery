package linhdvph25937.fpoly.appfooddelivery.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linhdvph25937.fpoly.appfooddelivery.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Boarding1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Boarding1Fragment extends Fragment {
    public Boarding1Fragment() {
        // Required empty public constructor
    }

    public static Boarding1Fragment newInstance() {
        Boarding1Fragment fragment = new Boarding1Fragment();
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
        return inflater.inflate(R.layout.fragment_boarding1, container, false);
    }
}