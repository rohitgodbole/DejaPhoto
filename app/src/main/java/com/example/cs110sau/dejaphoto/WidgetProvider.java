package com.example.cs110sau.dejaphoto;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

    // onUpdate: Launch one of four activities once its corresponding button is pressed
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        Intent nextIntent = new Intent(context, NextPhotoActivity.class);
        Intent prevIntent = new Intent(context, PrevPhotoActivity.class);
        Intent karmaIntent = new Intent(context, KarmaActivity.class);
        Intent releaseIntent = new Intent(context, ReleaseActivity.class);

        PendingIntent nextPendingIntent = PendingIntent.getActivity(context, 0, nextIntent, 0);
        PendingIntent prevPendingIntent = PendingIntent.getActivity(context, 0, prevIntent, 0);
        PendingIntent karmaPendingIntent = PendingIntent.getActivity(context, 0, karmaIntent, 0);
        PendingIntent releasePendingIntent = PendingIntent.getActivity(context, 0, releaseIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.right, nextPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.left, prevPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.karma, karmaPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.release, releasePendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

}

/*
package com.example.cs110sau.dejaphoto;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.List;

public class WidgetProvider extends AppWidgetProvider {
    private static final String MyOnClick = "myOnClickTag";
    Uri picUri1;

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


    public void onReceive(Context context, Intent intent) {
*/
/*
        if (MyOnClick.equals(intent.getAction())) {
            test2.uriToWallpaper(picUri1);// Perform action on click
            Toast.makeText(context, "orry", Toast.LENGTH_SHORT).show();//your onClick action is here
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        int appWidgetId = appWidgetIds[0];

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setOnClickPendingIntent(R.id.Right, getPendingSelfIntent(context, MyOnClick));

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


        Intent nextPhotoIntent = new Intent(context, NextPhotoActivity.class);
        PendingIntent nextPhotoPendingIntent = PendingIntent.getActivity(context, 0, nextPhotoIntent, 0);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // views.setOnClickPendingIntent(R.id.Right, getPendingSelfIntent(context,"what"));
        views.setOnClickPendingIntent(R.id.Right, nextPhotoPendingIntent);

        views.setOnClickPendingIntent(R.id.Left, getPendingSelfIntent(context, "what"));

        views.setOnClickPendingIntent(R.id.Kharma, getPendingSelfIntent(context, "what"));

        views.setOnClickPendingIntent(R.id.Release, getPendingSelfIntent(context, "what"));

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            class MyActivity extends Activity {
                    protected void onCreate(Bundle icicle) {
                            super.onCreate(icicle);

                            setContentView(R.layout.activity_main);

                            final ImageButton button = (ImageButton) findViewById(R.id.Right);
                            button.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                            test2.uriToWallpaper(picUri1);// Perform action on click
                                            Toast.makeText(getApplicationContext(), "sorry", Toast.LENGTH_SHORT).show();
                                    }
                            });
                    }
            }
            Intent nextPhotoIntent = new Intent(context, MyActivity.class);
            PendingIntent nextPhotoPendingIntent = PendingIntent.getActivity(context, 0, nextPhotoIntent, 0);
            views.setOnClickPendingIntent(R.id.Right, nextPhotoPendingIntent);

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }
}
*/