package Tests;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.example.cs110sau.dejaphoto.MainActivity;

import org.junit.Rule;
import org.junit.Test;

/**
 * Created by shuting on 6/9/2017.
 */

public class NextPhotoActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    private Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void testUpdateRecentPhotos () {

    }

    @Test
    public void testDrawTextToBitmap () {

    }

    @Test
    public void testGetPicLocation () {

    }

    @Test
    public void testAdjustPicScores () {

    }

    @Test
    public void testMatchLocation () {

    }

    @Test
    public void testMatchTime () {

    }

    @Test
    public void testMatchDay () {

    }
}
