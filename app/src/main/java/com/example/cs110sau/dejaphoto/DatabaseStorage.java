package com.example.cs110sau.dejaphoto;

import android.graphics.Bitmap;
import android.media.Image;
import android.provider.ContactsContract;

import java.util.ArrayList;


public class DatabaseStorage {
    public ArrayList<Bitmap> photos;
    public ArrayList<Integer> karma;
    public DatabaseStorage() {
        photos = new ArrayList<>();
        karma = new ArrayList<>();
        karma.add(100);
        karma.add(99);
    }
}
