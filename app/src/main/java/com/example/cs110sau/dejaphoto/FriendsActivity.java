package com.example.cs110sau.dejaphoto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class FriendsActivity extends AppCompatActivity {

    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        final SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        Button addFriends;
        Button viewfriendsphoto;
        String friendID;

        addFriends = (Button) findViewById(R.id.addFriends);
        friendID = ((EditText) findViewById(R.id.friendid)).getEditableText().toString();
        viewfriendsphoto = (Button) findViewById(R.id.viewFriendsPhoto);

        addFriends.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                String userid = sharedPreferences.getString("userid", null);
                if (userid.equals("")){
                    Toast.makeText(getApplicationContext(), "You must create an user ID first.", Toast.LENGTH_LONG).show();
                }


            }
        });
        viewfriendsphoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view)  {
                File myDir = new File(root, "/DejaPhotoFriends");
                myDir.mkdirs();
             //   intent.setAction(Intent.ACTION_VIEW);
             //   intent.setDataAndType(Uri.parse(myDir, "image/*");
              //  startActivity(intent);
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivity(photoPickerIntent);
            }

        });

    }
    public void finish (View view) {
        finish();
    }

}
