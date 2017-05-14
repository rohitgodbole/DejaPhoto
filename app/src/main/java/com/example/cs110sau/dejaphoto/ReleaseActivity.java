package com.example.cs110sau.dejaphoto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ReleaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        finish();
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Release", Toast.LENGTH_SHORT).show();
    }

}
