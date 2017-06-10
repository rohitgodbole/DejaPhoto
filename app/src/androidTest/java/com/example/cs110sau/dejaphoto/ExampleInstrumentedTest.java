package com.example.cs110sau.dejaphoto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<KarmaActivity> karmaActivity = new ActivityTestRule<KarmaActivity>(KarmaActivity.class);

    @Rule
    public ActivityTestRule<NextPhotoActivity> nextActivity = new ActivityTestRule<NextPhotoActivity>(NextPhotoActivity.class);

    @Rule
    public ActivityTestRule<PrevPhotoActivity> prevA = new ActivityTestRule<PrevPhotoActivity>(PrevPhotoActivity.class);

    @Rule
    public ActivityTestRule<ReleaseActivity> releaseA = new ActivityTestRule<ReleaseActivity>(ReleaseActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.cs110sau.dejaphoto", appContext.getPackageName());
    }

    @Test
    public void testKarma() throws Exception {
        Context context = InstrumentationRegistry.getContext();
        Intent intent = new Intent(context, KarmaActivity.class);
        karmaActivity.getActivity().startActivity(intent);


        int test_index = Integer.MAX_VALUE;
        // TODO: how to get context from main application or karma activity

        //SharedPreferences sharedPreferences = getSharedPreferences ("user_name", MODE_PRIVATE);


    }

    @Test
    public void testNext() throws Exception {
        Context context = InstrumentationRegistry.getContext();
        Intent intent = new Intent(context, NextPhotoActivity.class);
        nextActivity.getActivity().startActivity(intent);

        assertEquals("next",context.getPackageName());

    }

    @Test
    public void testPrev() throws Exception {
        Context context = InstrumentationRegistry.getContext();
        Intent intent = new Intent(context, PrevPhotoActivity.class);
        prevA.getActivity().startActivity(intent);

        assertEquals("prev",context.getPackageName());

    }

    @Test
    public void testRelease() throws Exception {
        Context context = InstrumentationRegistry.getContext();
        Intent intent = new Intent(context, ReleaseActivity.class);
        releaseA.getActivity().startActivity(intent);

        assertEquals("release",context.getPackageName());

    }
}