package net.sharksystem.sbc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.sbc.R;

public class BroadcastsFragment extends Fragment {

    public BroadcastsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_broadcasts, container, false);
        return  inflater.inflate(R.layout.fragment_broadcasts, container, false);
    }
}
