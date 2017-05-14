package com.example.cs110sau.dejaphoto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

// TODO modify next/prev methods to skip over released
public class ReleaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        finish();
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Released", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int index = sharedPreferences.getInt("index", 0);
        editor.putString(Integer.toString(index), "RELEASED");
        editor.commit();

        Intent intent = new Intent(this, NextPhotoActivity.class);
        startActivity(intent);

    }

}
