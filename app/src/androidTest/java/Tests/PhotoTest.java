package Tests;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.example.cs110sau.dejaphoto.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import java.sql.Time;

/**
 * Created by shuting on 6/9/2017.
 */

public class PhotoTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    private Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void testgetFilename() {

    }

    @Test
    public void testgetUri() {

    }

    @Test
    public void testgetLocation() {

    }

    @Test
    public void testgetTime() {

    }

    @Test
    public void testgetDayOfWeek() {

    }

    @Test
    public void testisKarmaOn() {

    }

    @Test
    public void testisReleased() {

    }

    @Test
    public void testgetScore () {

    }

    // Setters:
    @Test
    public void testsetFilename (String filename) {

    }

    @Test
    public void testsetUri (Uri uri) {

    }

    @Test
    public void testsetLocation (Location location) {

    }

    @Test
    public void testsetTime (Time time) {

    }

    @Test
    public void testsetDayOfWeek (int dayOfWeek) {

    }

    @Test
    public void testsetKarma (boolean karma) {

    }

    @Test
    public void testsetReleased (boolean released) {

    }

    @Test
    public void testsetScore (int score) {

    }
}
