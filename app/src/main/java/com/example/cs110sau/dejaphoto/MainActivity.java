package com.example.cs110sau.dejaphoto;

import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CHOOSE_PIC_CODE = 12345;

    Button changeWallpaper;
    Uri picUri;

    /* TODO modify below method
    * getCameraImages - should get the Uri of all photos and store into an array of strings
    * but the query method makes the app crash */
    public List<String> getCameraImages(Context context) {

        /*
        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String CAMERA_IMAGE_BUCKET_ID = String.valueOf(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera".toLowerCase().hashCode());
        String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        ContentResolver contentResolver = context.getContentResolver();
        //Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
        List<String> uriArray = new ArrayList<String>(cursor.getCount());
        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                final String data = cursor.getString(dataColumn);
                uriArray.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return uriArray;
        */

        // test strings since above code isn't working for now
        List<String> asdf = new ArrayList<String>();
        asdf.add("test - 3");
        asdf.add("test - 2");
        asdf.add("test - 1");
        return asdf;

    }

    /* TODO Method Header: chooseWallpaper */
    public void chooseWallpaper(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, CHOOSE_PIC_CODE);

    }

    /* TODO Method Header: uriToWallpaper */
    public void uriToWallpaper(Uri picUri) {
        WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
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
                Toast.makeText(getApplicationContext(), "Activity Finished", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void nextPic(View view) {
        //TODO @Oscar
    }

    public void prevPic(View view) {
        //TODO @Oscar
    }

    /* TODO Method Header: onCreate */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> a = getCameraImages(getApplicationContext());
        for (int i = 0; i < a.size(); i++) {
            Toast.makeText(getApplicationContext(), a.get(i), Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getApplicationContext(), "App Started", Toast.LENGTH_SHORT).show();

        /* change wallpaper button */
        changeWallpaper = (Button) findViewById(R.id.changeWallpaper);

        changeWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseWallpaper(view);
            }
        });


    }
}

