package com.example.cs110sau.dejaphoto;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    // TODO edge case when there are no pics in the phone

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 258;
    static final long MILLISECONDS_IN_HOUR = 3600000;
    static final long HOURS_IN_MONTH = 730;

            Button refresh;
    Switch dejavumode;

    Spinner spinner;
    ArrayAdapter adapter;

    /* Runs when activity is started */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        while (!checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            checkPermissionREAD_EXTERNAL_STORAGE(this);
        }

        // TODO might need to give more permissions at runtime

        // refresh automatically if picture database is empty (a.k.a. first element is null)
        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        if (sharedPreferences.getString("0", null) == null)
            getCameraImages(getApplicationContext());

        /* onClick listeners for elements */
        refresh = (Button) findViewById(R.id.refresh);
        dejavumode = (Switch) findViewById(R.id.dejavumode);
        dejavumode.setChecked(true);  // deja vu mode is on by default

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCameraImages(getApplicationContext());
            }
        });
    }

    /* getCameraImages - gets the path of every photo taken by the phone's camera and stores them
     *   as strings. Also writes corresponding data about the photos to the sharedPreferences file. */
    @SuppressWarnings({"SecurityException", "MissingPermission"})
    public List<String> getCameraImages(Context context) {


        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";

        String CAMERA_IMAGE_BUCKET_ID = String.valueOf(CAMERA_IMAGE_BUCKET_NAME.toLowerCase().hashCode());
        String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        ContentResolver contentResolver = context.getContentResolver();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 258);

        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);

        List<String> pathNames = new ArrayList<String>(cursor.getCount());

        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                final String data = cursor.getString(dataColumn);
                pathNames.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("size", pathNames.size());

        int totalScore = 0; // keeps track of cumulative probability scores of photos

        // Give each photo a probability score 1-100, default is 10
        for (int i = 0; i < pathNames.size(); i++) {
            String key = Integer.toString(i);
            editor.putString(key, pathNames.get(i));
            // Calculate scores based on recency
            int monthsSincePhoto = 0;
            try {
                ExifInterface exif = new ExifInterface(pathNames.get(i));
                String picTime = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
                if (picTime != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                    Date picDate = simpleDateFormat.parse(picTime);
                    Date currentDate = new Date();
                    long timediff = picDate.getTime() - currentDate.getTime();
                    monthsSincePhoto = (int) ((timediff / MILLISECONDS_IN_HOUR) / HOURS_IN_MONTH);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            int score = 10 - monthsSincePhoto / 6;
            if (score <= 0)
                score = 1;
            editor.putInt(key + "score", score);
            totalScore += score;
        }

        // don't mess with index, unless it's outside our new array of path names
        int index = sharedPreferences.getInt("index", Integer.MAX_VALUE);
        if (index >= pathNames.size()) {
            editor.putInt("index", 0);
        }

        editor.putInt("totalScore", totalScore); // write total score, which determines probability
        editor.commit();

        Toast.makeText(context, "Photos Loaded From Camera", Toast.LENGTH_SHORT).show();
        return pathNames;
    }

    /* onStop - once user exits app, if deja vu mode is on, adjust scores of each picture accordingly */
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (dejavumode.isChecked()) {
            editor.putBoolean("dejavumode", true);
        }
        else {
            editor.putBoolean("dejavumode", false);
        }
    }

    // from Android developer site; ask for permission to read external storage at runtime
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    // from Android developer site; helper method for checkPermission
    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String>{
        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping");
            try{
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = "slept for "+params[0] + "seconds";
            }catch(Exception e){
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }
    }
}