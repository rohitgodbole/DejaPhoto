package com.example.cs110sau.dejaphoto;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        final SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        Button addFriends;
        String friendID;

        addFriends = (Button) findViewById(R.id.addFriends);
        friendID = ((EditText) findViewById(R.id.friendid)).getEditableText().toString();
        addFriends.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                String userid = sharedPreferences.getString("userid", null);
                if (userid.equals("")){
                    Toast.makeText(getApplicationContext(), "You must create an user ID first.", Toast.LENGTH_LONG).show();
                }


            }
        });

    }
    public void finish (View view) {
        finish();
    }

}
