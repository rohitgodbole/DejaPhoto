package com.example.cs110sau.dejaphoto;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static java.lang.Boolean.TRUE;

public class KarmaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        finish();
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Added Karma", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int index = sharedPreferences.getInt("index", 0);
        editor.putBoolean(Integer.toString(index) + "karma", TRUE);
        editor.commit();
    }

}
