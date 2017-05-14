package com.example.cs110sau.dejaphoto;

import android.app.WallpaperManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class PrevPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_photo);

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int index = sharedPreferences.getInt("index", -1); // TODO default value?
        int size = sharedPreferences.getInt("size", 1);

        index--;
        if (index < 0) {
            index = size - 1;
        }
        editor.putInt("index", index);
        editor.commit();

        String nextPicName = sharedPreferences.getString(Integer.toString(index),"ERROR");
        if (nextPicName.equals("ERROR")) {
            Toast.makeText(getApplicationContext(), "Error retrieving image", Toast.LENGTH_SHORT);
            finish();
        }

        WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
        Bitmap bitmap = BitmapFactory.decodeFile(nextPicName);
        try {
            w.setBitmap(bitmap);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        finish();

    }

}
