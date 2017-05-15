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
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NextPhotoActivity extends AppCompatActivity {

    // onCreate - runs when activity starts, cycles to next photo
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        finish();
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        // Shuffle between photos based on their relative probabilities
        int totalScore = sharedPreferences.getInt("totalScore", 0);
        if (totalScore == 0) {
            Toast.makeText(this, "Error: no unreleased photos", Toast.LENGTH_SHORT).show();
            return;
        }
        int rand = (int)(totalScore * Math.random());
        int tracker = 0;
        int ourIndex = 0;
        int increment = sharedPreferences.getInt(Integer.toString(ourIndex) + "score", 1);
        while (tracker + increment < rand) {
            ourIndex++;
            tracker += increment;
            while (sharedPreferences.getString(Integer.toString(ourIndex),"RELEASED") == "RELEASED") {
                ourIndex++;
            }
            increment = sharedPreferences.getInt(Integer.toString(ourIndex) + "score", 1);
        }
        String nextPicName = sharedPreferences.getString(Integer.toString(ourIndex),"ERROR");

        editor.putInt("index", ourIndex);
        editor.commit();

        if (nextPicName.equals("ERROR")) {
            Toast.makeText(getApplicationContext(), "Error retrieving image", Toast.LENGTH_SHORT);
            return;
        }

        WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
        Bitmap bitmap = BitmapFactory.decodeFile(nextPicName);

        // PRINT LOCATION (TODO)
        bitmap = drawTextToBitmap(getApplicationContext(), bitmap, getPicLocation(nextPicName));

        try {
            w.setBitmap(bitmap);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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

    // drawTextToBitmap: Takes in a bitmap, returns the same bitmap with
    //   text (passed in as parameter) written to bottom left corner
    public Bitmap drawTextToBitmap (Context context, Bitmap bitmap, String text) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = getBaseContext().getResources().getDisplayMetrics();
        float scale = resources.getDisplayMetrics().density;
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig,true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(61,61,61));
        paint.setTextSize((int)(10*scale));
        paint.setShadowLayer(1f,0f,1f,Color.WHITE);
        // TODO bitmap.getWidth() and bitmap.getHeight()
        int x = 0;
        int y = (int) metrics.ydpi + 35;
        canvas.drawText(text, x, y, paint);
        return bitmap;
    }

    // getPicLocation: given the path name of a string, get the name of the location where it was taken
    //   (as a name we can interpret, not a latitude/longitude)
    public String getPicLocation (String pathName) {
        try {
            ExifInterface exif = new ExifInterface(pathName);
            float [] latlong = new float[2];
            exif.getLatLong(latlong);
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latlong[0], latlong[1]+1, 1);  // TODO
            if (addresses.size() > 0)
                return addresses.get(0).getFeatureName();
            else
                return "Failed to get location.";
        }
        catch (IOException e) {
            e.printStackTrace();
            return "Failed to get location.";
        }
    }

}