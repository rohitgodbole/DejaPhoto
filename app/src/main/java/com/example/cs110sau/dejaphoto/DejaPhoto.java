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
    public DejaPhoto (Context context) {
        this.photos = null;
        this.context = context;
        this.size = 0;
    }

    // Fields
    ArrayList<Photo> photos;
    ArrayList<Photo> recent;
    boolean dejaVuMode;
    Context context;
    int size;
    // TODO variable for automatic refresh rate?

    // Methods:
    public void nextPhoto() {}
    public void prevPhoto() {}
    public void updateScores() {}
    public void addKarma(Photo photo) {}
    public void release(Photo photo) {}
    public void addPhoto (Photo photo) {}; // don't forget to size++

    // Getters:
    public Photo getCurrentPhoto() {
        return photos.get(0);
    }
    public boolean isDejaVuModeOn() {
        return false;
    }

    // Setters:
    public void setCurrentPhoto(Photo photo) {
        WallpaperManager w = WallpaperManager.getInstance(context);
        Bitmap bitmap = BitmapFactory.decodeFile(photo.getFilename());
    }
    public void setDejaVuMode(boolean dejaVuMode) {
        this.dejaVuMode = dejaVuMode;
    }

}
