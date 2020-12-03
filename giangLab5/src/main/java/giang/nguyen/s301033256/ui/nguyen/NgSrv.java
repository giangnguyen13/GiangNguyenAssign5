package giang.nguyen.s301033256.ui.nguyen;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import giang.nguyen.s301033256.R;
import giang.nguyen.s301033256.ui.giang.GiaDown;
/**
 * Giang Nguyen
 * Student# 301033256
 * Lab 5 - COPM304
 * Professor Haki Sharifi
 * */
public class NgSrv extends Fragment {

    private NgSrvViewModel mViewModel;
    View root;
    String apiKey;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    TextView output;
    String zipcode;
    EditText zipcodeTv;
    public static NgSrv newInstance() {
        return new NgSrv();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.ng_srv, container, false);
        apiKey = getResources().getString(R.string.api_key);
        Button callApiBtn = (Button)root.findViewById(R.id.giangCallApiBtn);
        output = (TextView)root.findViewById(R.id.giangOutputTextView);
        zipcodeTv = (EditText)root.findViewById(R.id.giangZipCodeEditText);
        callApiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipcode = zipcodeTv.getText().toString();
                if (zipcode.length() < 5) {
                    zipcodeTv.setError("Invalid Zipcode format");
                }else{
                    checkInternetPermission();
                    String url = getURL(zipcode,apiKey);
                    AsyncTaskExample asyncTask = new AsyncTaskExample();
                    asyncTask.execute(url);
                }
            }
        });
        return root;
    }

    private class AsyncTaskExample extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL ImageUrl = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                String x = readStream(is);
                return x;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Invalid response";
        }
        @Override
        protected void onPostExecute(String bitmap) {
            super.onPostExecute(bitmap);
            Log.println(Log.DEBUG,"JSON",bitmap);
            if(bitmap.equals("Invalid response")){
                output.setText("Zipcode not found");
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(bitmap);
                    JSONObject coord  = null;
                    coord = jsonObject.getJSONObject("coord");
                    double lon = coord.getDouble("lon");
                    Log.println(Log.DEBUG,"lon",String.format("%f",lon));
                    double lat = coord.getDouble("lat");
                    Log.println(Log.DEBUG,"lat",String.format("%f",lat));

                    JSONObject main  = jsonObject.getJSONObject("main");
                    double humidity = main.getDouble("humidity");
                    Log.println(Log.DEBUG,"humidity",String.format("%f",humidity));
                    String name = jsonObject.getString("name");
                    Log.println(Log.DEBUG,"name",name);
                    output.setText(String.format("%.2f %.2f g%.2f %s %s",lon,lat,humidity,name,zipcode));
                } catch (JSONException e) {
                    output.setText("Invalid response");
                    e.printStackTrace();
                }
            }
        }
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NgSrvViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(getContext(), "PERMISSION allowed", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "PERMISSION Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkInternetPermission() {
        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET);

            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
    }

    private String getURL(String zipcode, String apiKey){
        String textSource = String.format("https://api.openweathermap.org/data/2.5/weather?zip=%s&appid=%s",zipcode,apiKey);
        return textSource;
    }

}