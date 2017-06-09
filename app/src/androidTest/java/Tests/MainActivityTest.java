package Tests;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.widget.Spinner;

import com.example.cs110sau.dejaphoto.MainActivity;
import com.example.cs110sau.dejaphoto.R;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by l4johnso on 6/9/2017.
 */

public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    private Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void autoRefreshRate() {
        //Spinner spinner = (Spinner) mainActivity.getActivity().findViewById(R.id.spinner);
        //String spinnerVal = spinner.getItemAtPosition().toString();
        //long changeRate = Long.valueOf(spinnerVal).longValue() * 1000;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_name", Context.MODE_PRIVATE);
        long cycletime = sharedPreferences.getLong("refresh rate", 5000);
        assertEquals(4000,cycletime);
    }

}
