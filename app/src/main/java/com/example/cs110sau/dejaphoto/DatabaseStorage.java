package com.example.cs110sau.dejaphoto;

import android.graphics.Bitmap;
import android.media.Image;
import android.provider.ContactsContract;

import java.util.ArrayList;


public class DatabaseStorage {
    public ArrayList<String> paths;
    public ArrayList<Integer> karma;
    public DatabaseStorage() {
        karma = new ArrayList<>();
        paths = new ArrayList<>();
        karma.add(100);
        paths.add("testpath1");
        karma.add(99);
        paths.add("testpath2");
    }
}
