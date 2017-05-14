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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.media.ExifInterface;
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
    // TODO edge case when there are no pics in the phone?

    private static final int CHOOSE_PIC_CODE = 12345;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 258;

    Button changeWallpaper;
    Button refresh;
    Uri picUri;
    Testuri test1;
    String testuri;          //string of valid pathname
    Canvas canvas = new Canvas();   //used to create a canvas to try to add location



    /* Runs when activity is started */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        while (!checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            checkPermissionREAD_EXTERNAL_STORAGE(this);
        }

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



    /* TODO modify below method?
    * getCameraImages - should get the Uri of all photos and store into an array of strings */
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

        // TODO replace with "app started" message?
        Toast.makeText(getApplicationContext(), "Size: "+pathNames.size(), Toast.LENGTH_SHORT).show();

        int totalScore = 0; // keeps track of cumulative probability scores of photos
        for (int i = 0; i < pathNames.size(); i++) {
            String key = Integer.toString(i);
            editor.putString(key, pathNames.get(i));
            editor.putInt(key + "score", 10);  // probability score 1-100, default is 10
            totalScore += 10;
        }

        // don't mess with index, unless it's outside our new array of path names
        int index = sharedPreferences.getInt("index", Integer.MAX_VALUE);
        if (index >= pathNames.size()) {
            editor.putInt("index", 0);
        }

        editor.putInt("totalScore", totalScore); // write total score, which determines probability
        editor.commit();

        return pathNames;
    }



    /* chooseWallpaper: Allows user to choose a picture in the gallery to change to their wallpaper */
    public void chooseWallpaper(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, CHOOSE_PIC_CODE);

    }



    /* uriToWallpaper: takes in a Uri as a parameter, changes picture to wallpaper */
    public void uriToWallpaper(Uri picUri) {
        WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
            // display location on the homescreen
            /*Bitmap mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            canvas.setBitmap(mutable);
            Paint paint = new Paint();
            paint.setTextSize(1000);

            canvas.drawText(readGeoTagImage(testuri),0, 0, paint);
            canvas.drawBitmap(mutable,0,0,null);
            */
            // change to mutable below
            w.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /* TODO Method Header: onActivityResult */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_PIC_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                picUri = data.getData();
                uriToWallpaper(picUri);
                // toast for testing latitude
                Toast.makeText(getApplicationContext(), readGeoTagImage(testuri), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
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



    //takes pathname and get's lat and long and returns ... for not just lat i think
    public String readGeoTagImage(String imagePath)
    {
        Location loc = new Location("");
        String lat;
        try {

            Toast.makeText(getApplicationContext(), "Trying", Toast.LENGTH_SHORT).show();
            ExifInterface exif = new ExifInterface(imagePath);
            float [] latlong = new float[2] ;
            Toast.makeText(getApplicationContext(), "Trying", Toast.LENGTH_SHORT).show();
            if(exif.getLatLong(latlong)){
                loc.setLatitude(latlong[0]);
                loc.setLongitude(latlong[1]);
            }

            lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            return loc.toString();
            //SimpleDateFormat fmt_Exif = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            //loc.setTime(fmt_Exif.parse(date).getTime());

        } catch (IOException e) {
            e.printStackTrace();
        }
        //return lat;
        return "fail";
    }
}