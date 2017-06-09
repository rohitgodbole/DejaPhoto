/* WidgetProvider.java
   Defines the onclick behavior of the DejaPhoto widget. */

package com.example.cs110sau.dejaphoto;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/* WidgetProvider: Assigns unique activities to the four buttons in the widget,
   assigning each one a different function.
   There are previous/next buttons as well as buttons to add karma and release the photo. */
public class WidgetProvider extends AppWidgetProvider {

    // onUpdate: Launch one of four activities once its corresponding button is pressed
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        Intent nextIntent = new Intent(context, NextPhotoActivity.class);
        Intent prevIntent = new Intent(context, PrevPhotoActivity.class);
        Intent karmaIntent = new Intent(context, KarmaActivity.class);
        Intent releaseIntent = new Intent(context, ReleaseActivity.class);
        Intent userLocIntent = new Intent( context, usrDefnLocActivity.class);

        PendingIntent nextPendingIntent = PendingIntent.getActivity(context, 0, nextIntent, 0);
        PendingIntent prevPendingIntent = PendingIntent.getActivity(context, 0, prevIntent, 0);
        PendingIntent karmaPendingIntent = PendingIntent.getActivity(context, 0, karmaIntent, 0);
        PendingIntent releasePendingIntent = PendingIntent.getActivity(context, 0, releaseIntent, 0);
        PendingIntent LocationPendingIntent = PendingIntent.getActivity(context, 0, userLocIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.right, nextPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.left, prevPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.karma, karmaPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.release, releasePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.userLocation, LocationPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

}