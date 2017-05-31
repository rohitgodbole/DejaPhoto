package com.example.cs110sau.dejaphoto;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

// TODO below implementation works BUT what we should be writing to Firebase are just the
// filenames (paths) of the photos & their karma scores

public class DejaPhoto {

    // Constructor
    public DejaPhoto (Context context) {
        this.photos = null;
        this.recent = null;
        this.dejaVuMode = false;
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
    public void addPhoto (Photo photo) {}; // don't forget to size++ & update database

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