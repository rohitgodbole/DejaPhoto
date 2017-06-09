package com.example.cs110sau.dejaphoto;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.cs110sau.dejaphoto.R.id.parent;

public class MainActivity extends AppCompatActivity  {

    // unique ints to act as request codes
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 258;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1470;
    static final long MILLISECONDS_IN_HOUR = 3600000;
    static final long HOURS_IN_MONTH = 730;
    static final int REQUEST_IMAGE_CAPTURE = 1;
//    static final int REQUEST_TAKE_PHOTO = 145;
    //used to take a photo within the app and store the photo onto the gallery
    static final int REQUEST_TAKE_PHOTO = 1;
    String CurrentPhotoPath;
    Intent takePictureIntent;
    Bitmap imageBitmap;

    //must be different than image capture
    static final int SELECT_PHOTO = 2;
    String imageEncoded;
    List<String> imagesEncodedList;
    Intent photoPickerIntent;


    DejaPhoto dejaPhoto;

    Button refresh;
    Switch dejavumode;
    Button importPhotos;
    Button takePhoto;
    Button friends;
    Button settings;
    Button autoRef;
    EditText time;
    //TextView finalResult;

    FirebaseDatabase database;
    DatabaseReference myRef;

    Spinner spinner;
    ArrayAdapter adapter;
    long changeRate;

    /* Runs when activity is started */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dejaPhoto = new DejaPhoto(getApplicationContext());

        while (!checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            checkPermissionREAD_EXTERNAL_STORAGE(this);
        }

        while (!checkPermissionACCESS_FINE_LOCATION(this)) {
            checkPermissionACCESS_FINE_LOCATION(this);
        }

        /* onClick listeners for elements */
        refresh = (Button) findViewById(R.id.refresh);
        dejavumode = (Switch) findViewById(R.id.dejavumode);
        importPhotos = (Button) findViewById(R.id.importphotos);
        takePhoto = (Button) findViewById(R.id.takephoto);
        friends = (Button) findViewById(R.id.friends);
        settings = (Button) findViewById(R.id.settings);
        autoRef = (Button) findViewById(R.id.auto_ref);
        time = (EditText) findViewById(R.id.edittext);

        autoRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = time.getText().toString();
                runner.execute(sleepTime);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                getCameraImages(getApplicationContext());
            }
        });


        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.choose_rate, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemselected = parent.getItemAtPosition(position).toString();
                changeRate = Long.valueOf(itemselected).longValue() * 1000;

                SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putLong("refresh rate", changeRate);
                editor.commit();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "Nothing selected " , Toast.LENGTH_SHORT).show();
            }
        });


        dejavumode.setChecked(true);  // deja vu mode is on by default

        importPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                // TODO import photos activity
                photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                startActivityForResult(photoPickerIntent,SELECT_PHOTO);
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {

            //this will take you to the built in camera it simply just goes there
            @Override
            public void onClick (View view) {
                takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);

            }



        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                // TODO start friends activity
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                // TODO settings activity
            }
        });



    }

    //gets called after a pictre was taken and gives us a bitmap of the picture then we put the bitmap
    //to a file and creates a folder in the gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        //if we just took a picture from inside the app
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            File myDir = new File(root, "DejaPhoto");
            myDir.mkdirs();
            String imageFileName = "Image_"+ timeStamp + "_";

            try {
                File imageF = File.createTempFile(imageFileName, ".jpg", myDir);
                CurrentPhotoPath = imageF.getAbsolutePath();
                FileOutputStream fout = new FileOutputStream(CurrentPhotoPath);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageF));
                Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                File f = new File(CurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                MainActivity.this.sendBroadcast(mediaScanIntent);

            } catch ( IOException e){

            }
        }
        //if we are trying to import photos
        //TODO right now i believe i am getting the uri but dont know what to do with them yet
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK){
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            ClipData clipData = data.getClipData();
            ArrayList<Uri> theArryUri = new ArrayList<Uri>();
            for (int i = 0; i < clipData.getItemCount(); i++)
            {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                theArryUri.add(uri);
                // Get the cursor
                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imageEncoded  = cursor.getString(columnIndex);
                imagesEncodedList.add(imageEncoded);
                cursor.close();
                File myDir = new File(root, "DejaPhotoCopied");
                myDir.mkdirs();
                String copyFileName = "Copy_" + timeStamp + "_";
                try{
                    File copyF = File.createTempFile(copyFileName, ".jpg", myDir);
                    CurrentPhotoPath = copyF.getAbsolutePath();
                    FileOutputStream fout = new FileOutputStream(CurrentPhotoPath);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(copyF));
                    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    File f = new File(CurrentPhotoPath);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    MainActivity.this.sendBroadcast(mediaScanIntent);

                } catch( IOException e){

                }
            }





//            String[] filePathCol = {MediaStore.Images.Media.DATA};
//


//            imagesEncodedList = new ArrayList<String>();
//            imagesEncodedList.add(filePathCol[0]);
//
//
//
//
////            if(data.getData() != null){
////
//                Uri imageUri = data.getData();
//
//
//
//

        }

    }



    /* getCameraImages - gets the path of every photo taken by the phone's camera and stores them
     *   as strings. Also writes corresponding data about the photos to the sharedPreferences file. */
    //@SuppressWarnings({"SecurityException", "MissingPermission"})
    public List<String> getCameraImages(Context context) {


        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";

        String CAMERA_IMAGE_BUCKET_ID = String.valueOf(CAMERA_IMAGE_BUCKET_NAME.toLowerCase().hashCode());
        String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        ContentResolver contentResolver = context.getContentResolver();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 258);

        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);

        List<String> pathNames = new ArrayList<String>(cursor.getCount());

        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                final String data = cursor.getString(dataColumn);
                pathNames.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("size", pathNames.size());

        int totalScore = 0; // keeps track of cumulative probability scores of photos

        // (TODO) create new album (doesn't work yet)
//        final File imageRoot = new File(Environment.getExternalStoragePublicDirectory
//                (Environment.DIRECTORY_DOWNLOADS), "DejaPhoto");
//        imageRoot.mkdirs();
//        Toast.makeText(context, "making dir: " + imageRoot.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        // Give each photo a probability score 1-100, default is 10
        for (int i = 0; i < pathNames.size(); i++) {
            String key = Integer.toString(i);
            editor.putString(key, pathNames.get(i));
            // Calculate scores based on recency
            int monthsSincePhoto = 0;
            try {
                ExifInterface exif = new ExifInterface(pathNames.get(i));
                String picTime = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
                if (picTime != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                    Date picDate = simpleDateFormat.parse(picTime);
                    Date currentDate = new Date();
                    long timediff = currentDate.getTime() - picDate.getTime();
                    monthsSincePhoto = (int) ((timediff / MILLISECONDS_IN_HOUR) / HOURS_IN_MONTH);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int score = 10 - monthsSincePhoto / 6;
            if (score <= 0)
                score = 1;
            editor.putInt(key + "score", score);
            totalScore += score;


//            OutputStream fOut = null;
//            File file = new File (imageRoot, pathNames.get(i) + ".jpg");
//            try {
//                fOut = new FileOutputStream(file);
//                Bitmap bitmap = BitmapFactory.decodeFile(pathNames.get(i));
//                bitmap.compress (Bitmap.CompressFormat.JPEG, 100, fOut);
//                fOut.flush();
//                fOut.close();
//
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.TITLE, "testing");
//                values.put(MediaStore.Images.Media.DESCRIPTION, "some picture");
//                values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
//                values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
//                values.put("_data", file.getAbsolutePath());
//
//                ContentResolver cr = getContentResolver();
//                cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
////            }
//            catch (Exception e) {
//                //Toast.makeText(context, "GET CAMERA IMAGES EXCEPTION", Toast.LENGTH_SHORT).show();
//            }
        }

        // don't mess with index, unless it's outside our new array of path names
        int index = sharedPreferences.getInt("index", Integer.MAX_VALUE);
        if (index >= pathNames.size()) {
            editor.putInt("index", 0);
        }

        // reset most recently displayed photo
        editor.putString("recentX", null);
        for (int i = 0; i < 10; i++) {
            editor.putString("recent" + i, null);
        }

        editor.putInt("totalScore", totalScore); // write total score, which determines probability
        editor.commit();

        Toast.makeText(context, "Photos Loaded From Camera", Toast.LENGTH_SHORT).show();
        finish();
        return pathNames;
    }

    /* onStop - once user exits app, if deja vu mode is on, adjust scores of each picture accordingly */
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (dejavumode.isChecked()) {
            editor.putBoolean("dejavumode", true);
            editor.commit();
            Toast.makeText(this, "Deja Vu Mode On", Toast.LENGTH_SHORT).show();
        } else {
            editor.putBoolean("dejavumode", false);
            editor.commit();
            Toast.makeText(this, "Deja Vu Mode Off", Toast.LENGTH_SHORT).show();
        }

        //getCameraImages(getApplicationContext());

        // Before closing app, save DejaPhoto data to Firebase (TODO)
        //database = FirebaseDatabase.getInstance();
        //myRef = database.getReference();
        //myRef.child("USERID").setValue(new DatabaseStorage());

    }

    // from Android developer site; ask for permission to read external storage at runtime
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    // from Android developer site; ask for permission to read external storage at runtime
    public boolean checkPermissionACCESS_FINE_LOCATION(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showDialog("External storage", context,
                            Manifest.permission.ACCESS_FINE_LOCATION);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    // from Android developer site; helper method for checkPermission
    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    //this is called by the spinner after a selection has been made and it calls doInBackground and runs
    //the intent for every cycletime
    class AsyncTaskRunner extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        private String ret;

        @Override
        protected String doInBackground(String...params) {
            publishProgress("Sleeping...");

            long starttime = 5000;
            long cycletime;
            SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
            cycletime = sharedPreferences.getLong("refresh rate",5000);


            Timer timer = new Timer();
            timer.schedule(new callNext(), starttime, cycletime);

            return ret;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();

        }

        @Override
        protected void onPreExecute() {
            time.findViewById(R.id.edittext);
            progressDialog = ProgressDialog.show(MainActivity.this,"ProgressDialog","wait for "+
            time.getText().toString()+" seconds");

        }

        @Override
        protected void onProgressUpdate(String...text) {
        }

    }

    //this class is called by the timer to do some sort of function
    //for this instance we are using it to call the nextphotoactivity intent.
    class callNext extends TimerTask {

        @Override
        public void run(){
            Intent next = new Intent(MainActivity.this, NextPhotoActivity.class);
            startActivity(next);

        }
    }
}

