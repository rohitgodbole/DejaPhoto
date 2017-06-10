package Tests;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cs110sau.dejaphoto.MainActivity;
import com.example.cs110sau.dejaphoto.R;

import org.junit.Rule;
import org.junit.Test;

import javax.annotation.concurrent.ThreadSafe;

import static org.junit.Assert.*;

/**
 * Created by l4johnso on 6/9/2017.
 */

public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);
    private Context context = InstrumentationRegistry.getTargetContext();


    @UiThreadTest
    public void testAutoRefListener () {
        Button autoRef = (Button) mainActivity.getActivity().findViewById(R.id.auto_ref);
        autoRef.performClick();
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_name", Context.MODE_PRIVATE);
        long sleeptime = sharedPreferences.getLong("sleeptime", 5000);
        Spinner sp = (Spinner) mainActivity.getActivity().findViewById(R.id.spinner);
        String slptime = sp.getSelectedItem().toString();
        assertEquals(sleeptime, slptime);
    }

    @UiThreadTest
    public void testSpinnerListener () {
        int position = 0;
        Spinner sp = (Spinner) mainActivity.getActivity().findViewById(R.id.spinner);
        sp.setSelection(position);
        int mposition = sp.getSelectedItemPosition();

        assertEquals(mposition, position);
    }


    @Test
    public void autoRefreshRate() {
        //Spinner spinner = (Spinner) mainActivity.getActivity().findViewById(R.id.spinner);
        //String spinnerVal = spinner.getItemAtPosition().toString();
        //long changeRate = Long.valueOf(spinnerVal).longValue() * 1000;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_name", Context.MODE_PRIVATE);
        long cycletime = sharedPreferences.getLong("refresh rate", 5000);
        assertEquals(4000,cycletime);
    }

    @Test
    public void testUpdateID(){
        mainActivity.getActivity().updateID("new");
        TextView textView = (TextView) mainActivity.getActivity().findViewById(R.id.title);
        String newTitle = textView.getText().toString();
        assertTrue(newTitle.equals("new"));
    }

    @Test
    public void testsubmituserid(){

    }

    @Test void testShowDialog(){

    }

    @Test
    public void checkPermissionACCESS_FINE_LOCATION(){

    }

    @Test
    public void checkPermissionREAD_EXTERNAL_STORAGE(){

    }


}
