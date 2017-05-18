package com.example.cs110sau.dejaphoto;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

/**
 * Created by rohit on 5/17/2017.
 */

public class DejaPhoto {

    // Constructor
    DejaPhoto (Context context) {
        this.photos = null;
        this.context = context;
    }

    // Fields
    ArrayList<Photo> photos;
    ArrayList<Photo> recent;
    boolean dejaVuMode;
    Context context;
    // TODO variable for automatic refresh rate

    // Methods:
    void nextPhoto() {}
    void prevPhoto() {}
    void updateScores() {}
    void addKarma(Photo photo) {}
    void release(Photo photo) {}

    // Getters:
    Photo getCurrentPhoto() {
        return photos.get(0);
    }
    boolean isDejaVuModeOn() {
        return false;
    }

    // Setters:
    void setCurrentPhoto(Photo photo) {
        WallpaperManager w = WallpaperManager.getInstance(context);
        Bitmap bitmap = BitmapFactory.decodeFile(photo.getFilename());
    }
    void setDejaVuMode(boolean dejaVuMode) {
        this.dejaVuMode = dejaVuMode;
    }

}
