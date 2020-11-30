package giang.nguyen.s301033256.ui.giang;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import giang.nguyen.s301033256.R;

public class GiaDown extends Fragment {

    private GiangViewModel mViewModel;

    View root;
    ProgressBar loading;

    public static GiaDown newInstance() {
        return new GiaDown();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.gia_down, container, false);
        Button downloadImgBtn = (Button)root.findViewById(R.id.giangDownloadImgBtn);
        loading = (ProgressBar)root.findViewById(R.id.progress);
        downloadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"BTN WORKING", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GiangViewModel.class);
        // TODO: Use the ViewModel
    }

}