package com.example.cs110sau.dejaphoto;

import android.provider.ContactsContract;

import java.util.ArrayList;


public class DatabaseStorage {
    public ArrayList<String> paths;
    public ArrayList<Integer> karma;
    public DatabaseStorage() {
        paths = new ArrayList<String>();
        karma = new ArrayList<Integer>();
        paths.add("PATH1");
        karma.add(123);
        paths.add("PATH2");
        karma.add(99);
    }
}
