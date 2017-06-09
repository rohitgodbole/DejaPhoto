package com.example.cs110sau.dejaphoto;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Switch dejavumode;
    Switch yourphotos;
    Switch friendsphotos;
    Switch sharephotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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

        dejavumode = (Switch) findViewById(R.id.dejavumode);
        yourphotos = (Switch) findViewById(R.id.yourphotos);
        friendsphotos = (Switch) findViewById(R.id.friendsphotos);
        sharephotos = (Switch) findViewById(R.id.sharephotos);

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);

        dejavumode.setChecked(sharedPreferences.getBoolean("dejavumode", true));
        yourphotos.setChecked(sharedPreferences.getBoolean("yourphotos", true));
        friendsphotos.setChecked(sharedPreferences.getBoolean("friendsphotos", true));
        sharephotos.setChecked(sharedPreferences.getBoolean("sharephotos", true));

    }

    public void apply (View view) {


        boolean dejavumode_on, yourphotos_on, friendsphotos_on, sharephotos_on;
        dejavumode_on = yourphotos_on = friendsphotos_on = sharephotos_on = false;

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (dejavumode.isChecked())
            dejavumode_on = true;

        if (yourphotos.isChecked())
            yourphotos_on = true;

        if (friendsphotos.isChecked())
            friendsphotos_on = true;

        if (sharephotos.isChecked())
            sharephotos_on = true;

        editor.putBoolean("dejavumode", dejavumode_on);
        editor.putBoolean("yourphotos", yourphotos_on);
        editor.putBoolean("friendsphotos", friendsphotos_on);
        editor.putBoolean("sharephotos", sharephotos_on);
        editor.commit();

        finish();
    }

}
