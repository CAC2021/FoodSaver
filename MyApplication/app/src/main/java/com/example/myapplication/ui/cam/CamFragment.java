package com.example.myapplication.ui.cam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;

public class CamFragment extends Fragment {

    private CamViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(CamViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cam, container, false);
        TextView applesFresh = (TextView) root.findViewById(R.id.applesFresh);
        applesFresh.setText("91.23");
        TextView applesSpoiled = (TextView) root.findViewById(R.id.applesSpoiled);
        applesSpoiled.setText("8.77");
        TextView bananasFresh = (TextView) root.findViewById(R.id.bananasFresh);
        bananasFresh.setText("91.23");
        TextView bananasSpoiled = (TextView) root.findViewById(R.id.bananasSpoiled);
        bananasSpoiled.setText("8.77");
        TextView orangesFresh = (TextView) root.findViewById(R.id.orangesFresh);
        orangesFresh.setText("91.23");
        TextView orangesSpoiled = (TextView) root.findViewById(R.id.orangesSpoiled);
        orangesSpoiled.setText("8.77");
        return root;
    }
}