package Tests;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.example.cs110sau.dejaphoto.DejaPhoto;
import com.example.cs110sau.dejaphoto.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertArrayEquals;

/**
 * Created by shuting on 6/9/2017.
 */

public class DejaPhotoTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    private Context context = InstrumentationRegistry.getTargetContext();


    @Test
    public void testToByteArray () {
        DejaPhoto dejaPhoto = new DejaPhoto(mainActivity.getActivity().getApplicationContext());
        byte[] bytes = dejaPhoto.toByteArray();
        assertArrayEquals(bytes, null);
        Log.i(TAG, dejaPhoto.fromByteArray(bytes).toString());

    }

    @Test
    public void testFromByteArray () {

    }

    @Test
    public void testGetCurrentPhoto () {

    }

    @Test
    public void testIsDejaVuModeOn () {

    }

    @Test
    public void testGetSize () {

    }

    @Test
    public  void testSetCurrentPhoto () {

    }

    @Test
    public void testSetDejaVuMode () {

    }

    @Test
    public void testSetSize () {

    }


}
