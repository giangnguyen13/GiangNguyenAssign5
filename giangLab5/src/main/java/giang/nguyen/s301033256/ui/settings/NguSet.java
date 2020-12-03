package giang.nguyen.s301033256.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import giang.nguyen.s301033256.GiangActivity;
import giang.nguyen.s301033256.R;
/**
 * Giang Nguyen
 * Student# 301033256
 * Lab 5 - COPM304
 * Professor Haki Sharifi
 * */
public class NguSet extends Fragment {

    private NguSetViewModel mViewModel;
    View root;

    Button saveSetting;
    Spinner fontSizeSpinner;
    RadioButton backgroundRadioButton;
    RadioButton backgroundCurrentRadioButton;
    RadioButton timeFormatRadioButton;
    RadioButton timeFormatCurrentRadioButton;
    Switch portraitModeSwitch;

    public static NguSet newInstance() {
        return new NguSet();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.ngu_set, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        String backgroundColorFromShared = sharedPreferences.getString("Background_Color","THIS IS DEFAULT VALUE");
        String timeFormatFromShared = sharedPreferences.getString("Time_Format","THIS IS DEFAULT VALUE");
        int fontSizeFromShared = sharedPreferences.getInt("Font_Size",-99);
        boolean lockPortraitFromShared = sharedPreferences.getBoolean("Lock_Portrait",false);

        switch (backgroundColorFromShared){
            case "Green":
                backgroundCurrentRadioButton = (RadioButton)root.findViewById(R.id.giangGreenRbtn);
                backgroundCurrentRadioButton.setChecked(true);
                break;
            case "Light Blue":
                backgroundCurrentRadioButton = (RadioButton)root.findViewById(R.id.giangLightBlueRbtn);
                backgroundCurrentRadioButton.setChecked(true);
                break;
            case "Yellow":
                backgroundCurrentRadioButton = (RadioButton)root.findViewById(R.id.giangYellowRbtn);
                backgroundCurrentRadioButton.setChecked(true);
                break;
        }

        switch (timeFormatFromShared){
            case "12 Hour format":
                timeFormatCurrentRadioButton = (RadioButton)root.findViewById(R.id.giang12HrsRbtn);
                timeFormatCurrentRadioButton.setChecked(true);
                break;
            case "24 Hour format":
                timeFormatCurrentRadioButton = (RadioButton)root.findViewById(R.id.giang24HrsRbtn);
                timeFormatCurrentRadioButton.setChecked(true);
                break;
        }

        try {
            saveSetting = (Button)root.findViewById(R.id.giangSaveSettingBtn);
            fontSizeSpinner = (Spinner)root.findViewById(R.id.giangFontSizeSpinner);
            portraitModeSwitch = (Switch )root.findViewById(R.id.portraitModeSwitch);
            portraitModeSwitch.setChecked(lockPortraitFromShared);
            fontSizeSpinner.setSelection(getIndex(fontSizeSpinner, String.format("%d",fontSizeFromShared)));

            saveSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioGroup backgroundColorRadioGroup = (RadioGroup)root.findViewById(R.id.giangBackgroundColorRadio);
                    RadioGroup timeFormatColorRadioGroup = (RadioGroup)root.findViewById(R.id.giangTimeFormatRadioGroup);
                    int selectedId = backgroundColorRadioGroup.getCheckedRadioButtonId();
                    int selectedId2 = timeFormatColorRadioGroup.getCheckedRadioButtonId();
                    //find the radiobutton by returned id
                    backgroundRadioButton = (RadioButton)root.findViewById(selectedId);
                    timeFormatRadioButton = (RadioButton)root.findViewById(selectedId2);

                    String backgroundColor = backgroundRadioButton.getText().toString();
                    String timeFormat = timeFormatRadioButton.getText().toString();
                    int fontSize = Integer.parseInt((String)fontSizeSpinner.getSelectedItem());
                    boolean lockPortrait = portraitModeSwitch.isChecked();
                    //Toast.makeText(getContext(),String.format("%d %s %s %b",fontSize,backgroundColor,timeFormat,lockPortrait),Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Background_Color", backgroundColor);
                    editor.putString("Time_Format", timeFormat);
                    editor.putInt("Font_Size", fontSize);
                    editor.putBoolean("Lock_Portrait",lockPortrait);

                    editor.commit();
                }
            });
        }catch (Exception exception){
            Toast.makeText(getContext(),exception.toString(),Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NguSetViewModel.class);
        // TODO: Use the ViewModel
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }
}