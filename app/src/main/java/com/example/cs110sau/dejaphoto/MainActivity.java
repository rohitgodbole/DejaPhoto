package com.example.cs110sau.dejaphoto;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CHOOSE_PIC_CODE = 12345;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 258;

    Button changeWallpaper;
    Button refresh;
    Uri picUri;
    Testuri test1;

    /* TODO modify below method
    * getCameraImages - should get the Uri of all photos and store into an array of strings
    * but the query method makes the app crash */
    @SuppressWarnings({"SecurityException", "MissingPermission"})
    public List<String> getCameraImages(Context context) {


        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";

        String CAMERA_IMAGE_BUCKET_ID = String.valueOf(CAMERA_IMAGE_BUCKET_NAME.toLowerCase().hashCode());
        String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        ContentResolver contentResolver = context.getContentResolver();

        // The query method is what makes this crash

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 258);

        //Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
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
        editor.putInt("index",5); // TODO remove later?
        for (int i = 0; i < pathNames.size(); i++) {
            String key = Integer.toString(i);
            editor.putString(key, pathNames.get(i));
        }

        return pathNames;

    }

    /* TODO Method Header: chooseWallpaper */
    public void chooseWallpaper(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, CHOOSE_PIC_CODE);

    }

    /* TODO Method Header: uriToWallpaper */
  /*  public void uriToWallpaper(Uri picUri) {
        WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
            w.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    /* TODO Method Header: onActivityResult */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_PIC_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                picUri = data.getData();
                test1.uriToWallpaper(picUri);
                Toast.makeText(getApplicationContext(), "Activity Finished", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* TODO Method Header: onCreate */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        while (!checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            checkPermissionREAD_EXTERNAL_STORAGE(this);
        }

        Toast.makeText(getApplicationContext(), "App Started", Toast.LENGTH_SHORT).show();

        List<String> pathNames = getCameraImages(getApplicationContext());

        /* onClick listeners for buttons */
        changeWallpaper = (Button) findViewById(R.id.chooseWallpaper);
        refresh = (Button) findViewById(R.id.refresh);

        changeWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseWallpaper(view);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCameraImages(getApplicationContext());
            }
        });
    }

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

}

