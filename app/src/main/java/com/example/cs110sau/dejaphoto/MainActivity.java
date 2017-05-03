package com.example.cs110sau.dejaphoto;

import android.app.WallpaperManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button changeWallpaper;
    ImageView default_img;

    public void changeWallpaper (View view) {

        System.out.println("TEST");

        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);

        WallpaperManager w = WallpaperManager.getInstance (getApplicationContext());
        try {
            w.setResource( + R.drawable.wallpaper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
        /*
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.default_img);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        */
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
        default_img = (ImageView) findViewById(R.id.default_img);

        changeWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                changeWallpaper(view);
            }
        });

        /* previous/next button, to be completed*/
        //TODO @Oscar
        ImageButton buttonNext = (ImageButton) findViewById(R.id.next_button);
        ImageButton buttonPrev = (ImageButton)  findViewById(R.id.prev_button);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                nextPic(view);
            }
        });

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick (View view) {
                prevPic(view);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
