package com.example.cs110sau.dejaphoto;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

// TODO below implementation works BUT what we should be writing to Firebase are just the
// filenames (paths) of the photos & their karma scores

public class DejaPhoto implements Serializable {

    // Constructor
    public DejaPhoto (Context context) {
        this.photos = null;
        this.recent = null;
        this.dejaVuMode = false;
        this.context = context;
        this.size = 120; // TODO
        this.userid = "";
    }

    // Fields
    ArrayList<Photo> photos;
    ArrayList<Photo> recent;
    boolean dejaVuMode;
    Context context;
    int size;
    String userid;
    // TODO variable for automatic refresh rate?

    // Methods:
    public void nextPhoto() {}
    public void prevPhoto() {}
    public void updateScores() {}
    public void addKarma(Photo photo) {}
    public void release(Photo photo) {}
    public void addPhoto (Photo photo) {}; // don't forget to size++ & update database
    public byte[] toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput obj_out = null;
        byte [] bytes = new byte[1];
        try {
            obj_out = new ObjectOutputStream(out);
            obj_out.writeObject(this);
            obj_out.flush();
            bytes = out.toByteArray();
        }
        catch (IOException e) {
            Toast.makeText(context, "IOException in toByteArray()", Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                obj_out.close();
                out.close();
            }
            catch (IOException e){
                Toast.makeText(context, "Exception while closing streams", Toast.LENGTH_SHORT).show();
            }
        }
        return bytes;
    }
    public DejaPhoto fromByteArray (byte[] bytes) {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInput obj_in = null;
        DejaPhoto d = new DejaPhoto(context);
        d.setSize(-1);
        try {
            obj_in = new ObjectInputStream(in);
            d = (DejaPhoto) obj_in.readObject();
        }
        catch (IOException e) {
            Toast.makeText(context, "IOException in fromByteArray()", Toast.LENGTH_SHORT).show();
        }
        catch (ClassNotFoundException e) {
            Toast.makeText(context, "ClassNotFoundException in toByteArray()", Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                if (in != null) { in.close(); }
                if (obj_in != null) { obj_in.close(); }
            }
            catch (IOException e) {
                Toast.makeText(context, "Exception while closing streams", Toast.LENGTH_SHORT).show();
            }
        }
        return d;
    }

    // Getters:
    public Photo getCurrentPhoto() {
        return photos.get(0);
    }
    public boolean isDejaVuModeOn() {
        return false;
    }
    public int getSize() { return size; }

    // Setters:
    public void setCurrentPhoto(Photo photo) {
        WallpaperManager w = WallpaperManager.getInstance(context);
        Bitmap bitmap = BitmapFactory.decodeFile(photo.getFilename());
    }
    public void setDejaVuMode(boolean dejaVuMode) {
        this.dejaVuMode = dejaVuMode;
    }
    public void setSize (int size) { this.size = size; } // TODO this is for testing purposes

}