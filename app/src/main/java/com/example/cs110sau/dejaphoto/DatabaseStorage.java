package com.example.cs110sau.dejaphoto;

import android.graphics.Bitmap;
import android.media.Image;
import android.provider.ContactsContract;

import java.util.ArrayList;


public class DatabaseStorage {
    public ArrayList<String> friends;
    public ArrayList<String> requests; // incoming friend requests
    public ArrayList<Integer> karma;
    public DatabaseStorage() {
        karma = new ArrayList<>();
        friends = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            karma.add (i += 7);
        }
        friends.add("TEST_FRIEND1");
        friends.add("TEST_FRIEND2");
    }
}
