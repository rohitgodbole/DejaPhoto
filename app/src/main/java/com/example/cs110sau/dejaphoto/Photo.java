package com.example.cs110sau.dejaphoto;

import android.location.Location;
import android.net.Uri;
import java.sql.Time;

/**
 * Created by rohit on 5/17/2017.
 */

public class Photo {

    // Constructor: Passes in filename and initializes fields
    Photo (String filename) {
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
    String getFilename() {
        return filename;
    }
    Uri getUri() {
        return uri;
    }
    Location getLocation() {
        return location;
    }
    Time getTime() {
        return time;
    }
    int getDayOfWeek() {
        return dayOfWeek;
    }
    boolean isKarmaOn() {
        return karma;
    }
    boolean isReleased() {
        return released;
    }
    int getScore () {
        return score;
    }

    // Setters:
    void setFilename (String filename) {
        this.filename = filename;
    }
    void setUri (Uri uri) {
        this.uri = uri;
    }
    void setLocation (Location location) {
        this.location = location;
    }
    void setTime (Time time) {
        this.time = time;
    }
    void setDayOfWeek (int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    void setKarma (boolean karma) {
        this.karma = karma;
    }
    void setReleased (boolean released) {
        this.released = released;
    }
    void setScore (int score) {
        this.score = score;
    }
}
