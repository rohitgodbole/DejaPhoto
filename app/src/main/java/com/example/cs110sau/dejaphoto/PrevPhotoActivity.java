package com.example.cs110sau.dejaphoto;

import android.app.WallpaperManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.io.IOException;

public class PrevPhotoActivity extends AppCompatActivity {

    // onCreate: runs when activity is started, switches wallpaper to most recently selected photo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        finish();
        super.onCreate(savedInstanceState);

        String nextPicName = updateRecentPhotos();

        if (nextPicName == null) {
            Toast.makeText(this, "Previous photo not found", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("recentX", nextPicName);
        editor.commit();

        WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
        Bitmap bitmap = BitmapFactory.decodeFile(nextPicName);

        try {
            w.setBitmap(bitmap);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // updateRecentPhotos: Updates 10 most recent photos while going backwards
    //   Also returns the path of the photo that should be displayed next
    public String updateRecentPhotos () {
        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);

        String [] recent = new String[10];

        for (int i = 0; i < 10; i++) {
            recent[i] = sharedPreferences.getString("recent" + i, null);
        }

        String returnVal = recent[0];

        // shift all recent photos up one
        for (int i = 1; i < 10; i++) {
            recent[i-1] = recent[i];
        }
        recent[9] = null;

        // write most recent to sharedPref file
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < 10; i++) {
            editor.putString("recent" + i, recent[i]);
        }
        editor.commit();

        return returnVal;
    }
}
