package com.example.cs110sau.dejaphoto;

import android.location.Location;
import android.net.Uri;
import java.sql.Time;


public class Photo {

    // Constructor: Passes in filename and initializes fields
    public Photo (String filename) {
        this.filename = filename;
        // initialize fields using filename
    }

    // Fields:
    String filename;
    Uri uri;
    Location location;
    Time time;
    int dayOfWeek;
    boolean karma;
    boolean released;
    int score;

    // Getters:
    public String getFilename() {
        return filename;
    }
    public Uri getUri() {
        return uri;
    }
    public Location getLocation() {
        return location;
    }
    public Time getTime() {
        return time;
    }
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public boolean isKarmaOn() {
        return karma;
    }
    public boolean isReleased() {
        return released;
    }
    public int getScore () {
        return score;
    }

    // Setters:
    public void setFilename (String filename) {
        this.filename = filename;
    }
    public void setUri (Uri uri) {
        this.uri = uri;
    }
    public void setLocation (Location location) {
        this.location = location;
    }
    public void setTime (Time time) {
        this.time = time;
    }
    public void setDayOfWeek (int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public void setKarma (boolean karma) {
        this.karma = karma;
    }
    public void setReleased (boolean released) {
        this.released = released;
    }
    public void setScore (int score) {
        this.score = score;
    }
}