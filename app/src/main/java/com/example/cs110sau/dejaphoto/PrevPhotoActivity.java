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
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PrevPhotoActivity extends AppCompatActivity {

    // onCreate: runs when activity is started, switches wallpaper to most recently selected photo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Fetching prev photo", ".");
        finish();
        super.onCreate(savedInstanceState);

        String nextPicName = updateRecentPhotos();

        if (nextPicName == null) {
            Toast.makeText(this, "Previous photo not found", Toast.LENGTH_SHORT).show();
            return;
        }

        while (nextPicName.equals("RELEASED")) {
            nextPicName = updateRecentPhotos();
            if (nextPicName == null) {
                Toast.makeText(this, "Previous photo not found", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("recentX", nextPicName);
        editor.commit();

        WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
        Bitmap bitmap = BitmapFactory.decodeFile(nextPicName);

        // Print location
        Location nextPicLocation = getPicLocation(nextPicName);
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String toPrint = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(nextPicLocation.getLatitude(), nextPicLocation.getLongitude(), 1);
            if (addresses.size() > 0)
                toPrint = addresses.get(0).getFeatureName();
            else
                toPrint = "Location info not found.";
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = drawTextToBitmap(getApplicationContext(), bitmap, toPrint);

        try {
            w.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // updateRecentPhotos: Updates 10 most recent photos while going backwards
    //   Also returns the path of the photo that should be displayed next
    public String updateRecentPhotos () {
        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);

        String [] recent = new String[10];

        for (int i = 0; i < 10; i++) {
            recent[i] = sharedPreferences.getString("recent" + i, null);
        }

        String returnVal = recent[0];

        // shift all recent photos up one
        for (int i = 1; i < 10; i++) {
            recent[i-1] = recent[i];
        }
        recent[9] = null;

        // write most recent to sharedPref file
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < 10; i++) {
            editor.putString("recent" + i, recent[i]);
        }
        editor.commit();

        return returnVal;
    }

    // drawTextToBitmap: Takes in a bitmap, returns the same bitmap with
    //   text (passed in as parameter) written to bottom left corner
    public Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text) {
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
}
