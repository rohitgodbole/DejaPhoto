package com.example.cs110sau.dejaphoto;

import android.app.WallpaperManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class NextPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_photo);

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int index = sharedPreferences.getInt("index",98); // TODO default value?

        index++;
        Toast.makeText(getApplicationContext(), Integer.toString(index), Toast.LENGTH_LONG).show();

        String nextPicName = sharedPreferences.getString(Integer.toString(index),"ERROR");
        if (nextPicName.equals("ERROR")) {
            index = 0;
            editor.putInt("index", index);
            nextPicName = sharedPreferences.getString(Integer.toString(index), "ERROR");
        }

        Toast.makeText(getApplicationContext(), Integer.toString(index), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), nextPicName, Toast.LENGTH_LONG).show();
        Uri nextPicUri = Uri.parse(nextPicName);

        WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), nextPicUri);
            w.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        finish();

    }

}
