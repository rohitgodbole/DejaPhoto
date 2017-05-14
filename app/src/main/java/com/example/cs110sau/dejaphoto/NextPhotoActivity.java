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

    // TODO known bug: if all photos are released, app will cycle in an infinite loop

    // onCreate - runs when activity starts, cycles to next photo
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        finish();
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // TODO probabilities

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
        paint.setTextSize((int)(30*scale));
        paint.setShadowLayer(1f,0f,1f,Color.WHITE);
        // TODO bitmap.getWidth() bitmap.getHeight()
        int x = 0;
        int y = 350;
        canvas.drawText(text, x, y, paint);
        return bitmap;
    }

    public String getPicLocation (String pathName) {
        try {
            ExifInterface exif = new ExifInterface(pathName);
            float [] latlong = new float[2];
            exif.getLatLong(latlong);
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latlong[0], latlong[1], 1);
            return addresses.get(0).getFeatureName();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "Failed to get location.";
        }
    }

}