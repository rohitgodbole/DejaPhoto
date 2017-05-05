package com.example.cs110sau.dejaphoto;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringDef;
import android.support.annotation.StyleRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button changeWallpaper;
    Uri picUri;

    public void changeWallpaper (View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 12345);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "Activity Finished", Toast.LENGTH_SHORT).show();

        if (requestCode == 12345 && resultCode == RESULT_OK) {
            if (data != null) {
                picUri = data.getData();
                WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
               try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    w.setBitmap(bitmap);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void nextPic (View view) {
        //TODO @Oscar
    }

    public void prevPic (View view) {
        //TODO @Oscar
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* change wallpaper button */
        changeWallpaper = (Button) findViewById(R.id.changeWallpaper);

        changeWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWallpaper(view);
            }
        });

        /* previous/next button, to be completed*/
        //TODO @Oscar
        ImageButton buttonNext = (ImageButton) findViewById(R.id.next_button);
        ImageButton buttonPrev = (ImageButton) findViewById(R.id.prev_button);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPic(view);
            }
        });

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevPic(view);
            }
        });

    }
}
