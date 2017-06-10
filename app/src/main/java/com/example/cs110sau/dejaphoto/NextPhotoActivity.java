package com.example.cs110sau.dejaphoto;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NextPhotoActivity extends AppCompatActivity {

    static final double DIST_LIMIT = 30.48;
    static final long MILLISECONDS_IN_HOUR = 3600000;
    static final long MILLISECONDS_IN_DAY = 86400000;

    // onCreate - runs when activity starts, cycles to next photo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Calculating next photo",".");

        finish();
        super.onCreate(savedInstanceState);
        finish();

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // If deja vu mode is on, adjust scores of each photo
        if (sharedPreferences.getBoolean("dejavumode", true) == true) {
            int size = sharedPreferences.getInt("size", 0); // size of picture array
            for (int i = 0; i < size; i++) {
                String pathName = sharedPreferences.getString(Integer.toString(i), null);
                if (pathName != null && !pathName.equals("RELEASED")) {
                    float multiplier = adjustPicScores(pathName);
                    int score = sharedPreferences.getInt(Integer.toString(i) + "score", 0);
                    editor.putInt(Integer.toString(i) + "score", (int) (score * multiplier));
                }
            }
        }

        // Shuffle between photos based on their relative probabilities (determined by scores)
        int totalScore = sharedPreferences.getInt("totalScore", 0);
        if (totalScore == 0) {
            Toast.makeText(this, "Error: no unreleased photos", Toast.LENGTH_SHORT).show();
            return;
        }
        int rand = (int) (totalScore * Math.random());
        int tracker = 0;
        int index = 0;
        int increment = sharedPreferences.getInt(Integer.toString(index) + "score", 1);
        while (tracker + increment < rand) {
            index++;
            tracker += increment;
            while (sharedPreferences.getString(Integer.toString(index), "RELEASED") == "RELEASED") {
                index++;
            }
            increment = sharedPreferences.getInt(Integer.toString(index) + "score", 1);
        }
        String nextPicName = sharedPreferences.getString(Integer.toString(index), "ERROR");

        editor.putInt("index", index);

        if (nextPicName.equals("ERROR")) {
            Toast.makeText(getApplicationContext(), "Error retrieving image", Toast.LENGTH_SHORT);
            return;
        }

        updateRecentPhotos();
        editor.putString("recentX", nextPicName);
        editor.commit();

        Log.i("Updating to wallpaper", ".");
        WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
        Bitmap bitmap = BitmapFactory.decodeFile(nextPicName);

        // Print location
        Location nextPicLocation = getPicLocation(nextPicName);
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String toPrint = sharedPreferences.getString(Integer.toString(index) + "location", null);
        if (toPrint == null) {
            Toast.makeText(this, "no custom location", Toast.LENGTH_SHORT).show(); // TODO
            try {
                List<Address> addresses = geocoder.getFromLocation(nextPicLocation.getLatitude(), nextPicLocation.getLongitude(), 1);
                if (addresses.size() > 0)
                    toPrint = addresses.get(0).getFeatureName();
                else
                    toPrint = "Location info not found.";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bitmap = drawTextToBitmap(getApplicationContext(), bitmap, toPrint);

        try {
            w.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        finish();

    }

    // updateRecentPhotos: Updates 10 most recent photos while going forwards
    public void updateRecentPhotos() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);

        String currentPic = sharedPreferences.getString("recentX", null);

        String[] recent = new String[10];

        for (int i = 0; i < 10; i++) {
            recent[i] = sharedPreferences.getString("recent" + i, null);
        }

        // shift all recent photos down one
        if (recent[0] != null) {
            for (int i = 8; i >= 0; i--) {
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

    // drawTextToBitmap: Takes in a bitmap, returns the same bitmap with
    //   text (passed in as parameter) written to bottom left corner
    public Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text) {
        if (text == null)
            return bitmap;

        Resources resources = context.getResources();
        DisplayMetrics metrics = getBaseContext().getResources().getDisplayMetrics();

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, metrics.widthPixels, metrics.heightPixels, false);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(canvas.getHeight()*40/1000);
        paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);
        int x = 0;
        int y = (int)(canvas.getHeight() * 0.78);

        canvas.drawText(text, x, y, paint);
        return bitmap;
    }

    // getPicLocation: given the path name of a string, get the name of the location where it was taken
    public Location getPicLocation(String pathName) {
        try {
            ExifInterface exif = new ExifInterface(pathName);
            float[] latlong = new float[2];
            exif.getLatLong(latlong);
            float latitude = latlong[0];
            float longitude = latlong[1];

            Location location = new Location("location");
            location.setLatitude(latitude);
            location.setLongitude(longitude);

            return location;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // adjustPicScores: called if Deja Vu Mode is on. Increases a picture's score if it matches the
    //   user's location, time of day, or day of the week at the time the button is pressed
    //   Returns the multiplier to be applied to each score
    public float adjustPicScores(String pathName) {

        float multiplier = 1;

        // Adjust based on location
        if (matchLocation(pathName)) {
            multiplier *= 1.5;
        }

        // Adjust based on time of day (and account for time zone)
        if (matchTime(pathName)) {
            multiplier *= 1.5;
        }

        // Adjust based on day of week
        if (matchDay(pathName)) {
            multiplier *= 1.5;
        }

        return multiplier;
    }

    // matchLocation: return true if picture passed by parameter is within 100 feet of user
    @SuppressWarnings({"SecurityException", "MissingPermission"})
    public boolean matchLocation(String pathName) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location locGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location locPic = getPicLocation(pathName);

        if (locPic == null)
            return false;
        double distance;
        if (locGPS != null) {
            distance = locGPS.distanceTo(locPic);
            if (distance <= DIST_LIMIT) {
                return true;
            }
        } else if (locNet != null) {
            distance = locGPS.distanceTo(locPic);
            if (distance <= DIST_LIMIT) {
                return true;
            }
        }
        return false;
    }


    // matchTime: return true if picture's time is within an hour of the current time
    public boolean matchTime(String pathName) {
        try {
            ExifInterface exif = new ExifInterface(pathName);
            String picTime = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
            if (picTime == null) return false;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

            Date picDate = simpleDateFormat.parse(picTime);
            Date currentDate = new Date();
            long timediff = currentDate.getTime() - picDate.getTime();
            long diff = timediff % MILLISECONDS_IN_DAY;
            if (diff <= MILLISECONDS_IN_HOUR || diff >= MILLISECONDS_IN_DAY - MILLISECONDS_IN_HOUR) {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    // matchDay: return true if the day of the week the picture was taken on is the same as the
    //   day of the week the method is called
    public boolean matchDay(String pathName) {
        try {
            ExifInterface exif = new ExifInterface(pathName);
            String picTime = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
            if (picTime == null) return false;


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

            Date picDate = simpleDateFormat.parse(picTime);
            Date currentDate = new Date();

            // Date's toString method begins w/ 3-letter code for day of week
            if (picDate.toString().substring(0,2).equals(currentDate.toString().substring(0,2)))
                return true;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}