package com.example.cs110sau.dejaphoto;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        final SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        Button addFriends;

        addFriends = (Button) findViewById(R.id.addFriends);
        final String friendID = ((EditText) findViewById(R.id.friendid)).getEditableText().toString();

        Log.d("friendID is ", friendID);

        addFriends.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                final String userid = sharedPreferences.getString("userid", null);
                if (userid.equals("")){
                    Toast.makeText(getApplicationContext(), "You must create an user ID first.", Toast.LENGTH_LONG).show();
                }
                else {

                    FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = firebaseDB.getReference();
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            /*
                            DatabaseStorage dbStorage = dataSnapshot.child(friendID).getValue(DatabaseStorage.class);
                            dbStorage.friends.add(userid);
                            dbStorage = dataSnapshot.child(userid).getValue(DatabaseStorage.class);
                            dbStorage.friends.add(friendID);
                            */
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }
        });

    }
    public void finish (View view) {
        finish();
    }

}
