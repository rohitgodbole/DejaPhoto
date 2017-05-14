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

public class NextPhotoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        finish();
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int index = sharedPreferences.getInt("index",-1); // TODO default value?
        int size = sharedPreferences.getInt("size",1);

        index++;
        if (index >= size) {
            index = 0;
        }
        editor.putInt("index", index);
        editor.commit();

        String nextPicName = sharedPreferences.getString(Integer.toString(index),"ERROR");
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

}
