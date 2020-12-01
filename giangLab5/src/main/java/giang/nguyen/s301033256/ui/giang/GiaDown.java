package giang.nguyen.s301033256.ui.giang;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import giang.nguyen.s301033256.R;

public class GiaDown extends Fragment {

    private GiangViewModel mViewModel;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    URL ImageUrl = null;
    InputStream is = null;
    Bitmap bmImg = null;
    ImageView imageView= null;
    ProgressBar p;

    View root;
    ProgressBar loading;

    final String fileName = "yoshino1234.png";
    final String imageURL = "https://i.redd.it/1rlog9bjz1041.jpg";

    public static GiaDown newInstance() {
        return new GiaDown();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.gia_down, container, false);
        Button downloadImgBtn = (Button)root.findViewById(R.id.giangDownloadImgBtn);
        imageView=root.findViewById(R.id.giangImgView);

        p = (ProgressBar)root.findViewById(R.id.progress);
        File file = getContext().getFileStreamPath(fileName);
        if (file.exists()) {
            imageView.setImageBitmap(loadImageBitmap(getContext(), fileName));
        }

        downloadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkWritePermission();
                    AsyncTaskExample asyncTask = new AsyncTaskExample();
                    asyncTask.execute(imageURL);
                }
                catch (Exception exception){
                    Toast.makeText(getContext(),exception.toString(), Toast.LENGTH_SHORT).show();
                }
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

    private class AsyncTaskExample extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = (ProgressBar) root.findViewById(R.id.progress);
            p.setVisibility(View.VISIBLE);
            p.setProgress(5);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                p.setProgress(10);
                ImageUrl = new URL(strings[0]);
                p.setProgress(20);
                HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                p.setProgress(30);
                conn.setDoInput(true);
                p.setProgress(40);
                conn.connect();
                p.setProgress(50);
                is = conn.getInputStream();
                p.setProgress(60);
                BitmapFactory.Options options = new BitmapFactory.Options();
                p.setProgress(70);
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                p.setProgress(80);
                bmImg = BitmapFactory.decodeStream(is, null, options);
                p.setProgress(90);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bmImg;
        }

//        @Override
//        protected void onProgressUpdate(String... values) {
//            //super.onProgressUpdate(values);
//            int x = values.length;
//            for (int i = 0; i < x; i++){
//                p.setProgress((int) ((i / (float) x) * 100));
//            }
//            //p.setProgress(Integer.parseInt(values[0]));
//        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            File file = getContext().getFileStreamPath(fileName);
            if (!file.exists()) {
                Toast.makeText(getContext(),"File not exist", Toast.LENGTH_LONG).show();
                p.setProgress(95);
                saveImage(getContext(), bitmap, fileName);
            }else{
                Toast.makeText(getContext(),"---> File exist", Toast.LENGTH_LONG).show();
            }
            p.setProgress(99);
            if(imageView!=null) {
                p.setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(loadImageBitmap(getContext(), fileName));
            }else {
                p.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void checkWritePermission() {
        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
    }

    private void checkReadPermission() {
        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
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

    private void checkPermission() {
        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET);

            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                ,Manifest.permission.INTERNET
                                ,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
    }

    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}