package Tests;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.example.cs110sau.dejaphoto.MainActivity;

import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Oscar on 6/9/17.
 */

public class FriendsActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    private Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void testFinish() throws Exception {


    }
}
