package com.example.cs110sau.dejaphoto;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

            int appWidgetId = appWidgetIds[0];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            Intent nextPhotoIntent = new Intent(context, NextPhotoActivity.class);
            PendingIntent nextPhotoPendingIntent = PendingIntent.getActivity(context, 0, nextPhotoIntent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.Right, nextPhotoPendingIntent);

            views.setOnClickPendingIntent(R.id.Left, pendingIntent);

            views.setOnClickPendingIntent(R.id.Kharma, pendingIntent);

            views.setOnClickPendingIntent(R.id.Release, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

    }
}
