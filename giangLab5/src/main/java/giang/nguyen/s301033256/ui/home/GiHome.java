package giang.nguyen.s301033256.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import giang.nguyen.s301033256.GiangActivity;
import giang.nguyen.s301033256.R;

public class GiHome extends Fragment {
    TextView currentTimeTv;
    TextView currentDateTv;
    Spinner selectCourseSpinner;
    ImageButton viewCourseInfoImgBtn;
    Thread thread;
    Handler handler = new Handler();
    View root;
    Date c;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        root = inflater.inflate(R.layout.gi_home, container, false);

        try {
            startCounting();
            selectCourseSpinner = (Spinner)root.findViewById(R.id.giangCoursesSpinner);
            viewCourseInfoImgBtn = (ImageButton)root.findViewById(R.id.giangViewCourseImgBtn);
            viewCourseInfoImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView text_sel = (TextView)selectCourseSpinner.getSelectedView();
                    //Toast.makeText(getActivity(),text_sel.getText().toString(), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("You select: "+text_sel.getText().toString())
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Select course");
                    alert.show();
                }
            });

            currentDateTv = (TextView)root.findViewById(R.id.giangCurrentDateTextView);
            currentDateTv.setText(formattedDate);
        }
        catch (Exception exception){
            Toast.makeText(getActivity(),exception.toString(), Toast.LENGTH_LONG).show();
        }


        return root;
    }

    private void startCounting() {
        handler.post(run);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            c = Calendar.getInstance().getTime();
            String time = new SimpleDateFormat("HH:mm:ss").format(c);
            currentTimeTv = (TextView)root.findViewById(R.id.giangTimeTextView);
            currentTimeTv.setText(time);
//            number++;
//            tvFragment.setText(number);
            handler.postDelayed(this, 1000);
        }
    };
}