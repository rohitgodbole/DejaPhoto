package com.example.cs110sau.dejaphoto;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static java.lang.Boolean.TRUE;

public class KarmaActivity extends AppCompatActivity {

    // onCreate: runs when the activity is started.
    //   For now, adding karma gives the photo a one-time +5 bonus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        finish();
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);

        int index = sharedPreferences.getInt("index", 0);
        if (sharedPreferences.getBoolean(Integer.toString(index) + "karma", false) == false) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Integer.toString(index) + "karma", true);
            int score = sharedPreferences.getInt(Integer.toString(index)+"score", 0);
            score += 5;
            editor.putInt(Integer.toString(index) + "score", score);
            editor.commit();
            Toast.makeText(this, "Added Karma", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Karma Already Added", Toast.LENGTH_SHORT).show();
        }
    }

}
