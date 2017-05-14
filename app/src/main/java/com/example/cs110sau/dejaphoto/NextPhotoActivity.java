package com.example.cs110sau.dejaphoto;

import android.app.WallpaperManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;

public class NextPhotoActivity extends AppCompatActivity {

    // TODO known bug: if all photos are released, app will cycle in an infinite loop

    // onCreate - runs when activity starts, cycles to next photo
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        finish();
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int index = sharedPreferences.getInt("index",-1);
        int size = sharedPreferences.getInt("size",1);

        String currentPic = sharedPreferences.getString(Integer.toString(index), null);
        String nextPicName = "";

        if (currentPic != null) {
            updateRecentPhotos (currentPic);
        }

        do {
            index++;
            if (index >= size) {
                index = 0;
            }
            nextPicName = sharedPreferences.getString(Integer.toString(index),"ERROR");
        } while (nextPicName.equals("RELEASED"));

        editor.putInt("index", index);
        editor.commit();

        if (nextPicName.equals("ERROR")) {
            Toast.makeText(getApplicationContext(), "Error retrieving image", Toast.LENGTH_SHORT);
            return;
        }

        Toast.makeText(getApplicationContext(), readGeoTagImage(nextPicName), Toast.LENGTH_SHORT).show();

        WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
        Bitmap bitmap = BitmapFactory.decodeFile(nextPicName);
        try {
            w.setBitmap(bitmap);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    //takes pathname and get's lat and long and returns ... for not just lat i think
    public String readGeoTagImage(String imagePath)
    {
        Location loc = new Location("");
        String lat;
        try {

            ExifInterface exif = new ExifInterface(imagePath);
            float [] latlong = new float[2] ;
            if(exif.getLatLong(latlong)){
                loc.setLatitude(latlong[0]);
                loc.setLongitude(latlong[1]);
            }

            lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            return loc.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //return lat;
        return "fail";
    }

    // updateRecentPhotos: Updates 10 most recent photos while going forwards
    public void updateRecentPhotos (String currentPic) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);

        String [] recent = new String[10];

        for (int i = 0; i < 10; i++) {
            recent[i] = sharedPreferences.getString("recent" + i, null);
        }

        // shift all recent photos down one
        if (recent[0] != null) {
            for (int i = 8; i >= 0; i--) {  // TODO magic numbers/strings? (here and in the other files)
                recent[i + 1] = recent[i];
            }
        }
            recent[0] = currentPic;

        // write 10 most recent to sharedPref file
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < 10; i++) {
            editor.putString("recent" + i, recent[i]);
        }
        editor.commit();
    }

}
