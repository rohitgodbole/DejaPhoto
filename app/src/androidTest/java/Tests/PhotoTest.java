package Tests;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import static android.R.attr.popupTheme;
import static android.R.attr.x;
import static org.junit.Assert.*;

import com.example.cs110sau.dejaphoto.MainActivity;
import com.example.cs110sau.dejaphoto.Photo;

import org.junit.Rule;
import org.junit.Test;

import java.sql.Time;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by shuting on 6/9/2017.
 */

public class PhotoTest {

    Photo photo = new Photo("ff");
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    private Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void testgetFilename() {
        String name = photo.getFilename();
        assertTrue(name.equals("ff"));
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
    public void testgetUri() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("www.l.com");
        photo.setUri(builder.build());
        String uri = photo.getUri().toString();
        assertTrue(uri.equals("http://www.l.com"));
    }

    @Test
    public void testisKarmaOn() {
        photo.setKarma(true);
        assertTrue(photo.isKarmaOn());
    }

    @Test
    public void testisReleased() {
        photo.setReleased(true);
        assertTrue(photo.isReleased());
    }

    @Test
    public void testgetScore () {
        photo.setScore(10);
        assertEquals(photo.getScore(), 10);
    }


}
