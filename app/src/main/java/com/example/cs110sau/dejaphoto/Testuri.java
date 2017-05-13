package com.example.cs110sau.dejaphoto;


import android.app.WallpaperManager;

import android.graphics.Bitmap;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

/**
 * Created by shy025 on 5/12/2017.
 * This is a class containing the global method uriToWallpaper so that it can be referenced in
 * both MainActivity and NextPhotoActivity.
 */

public class Testuri extends AppCompatActivity {

    Uri picUri;

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
}


