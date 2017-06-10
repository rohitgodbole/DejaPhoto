package com.example.cs110sau.dejaphoto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class ReleaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("Releasing photo", "...");
        finish();
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Released", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // indicate the picture has been released
        int index = sharedPreferences.getInt("index", 0);
        editor.putString(Integer.toString(index), "RELEASED");

        // update total score (impacts probability function)
        int totalScore = sharedPreferences.getInt("totalScore", 0);
        totalScore -= sharedPreferences.getInt(Integer.toString(index) + "score", 0);
        editor.putInt("totalScore",totalScore);

        editor.commit();

        Intent intent = new Intent(this, NextPhotoActivity.class);
        startActivity(intent);

    }

}
