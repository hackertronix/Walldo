package com.example.hackertronix.firebaseauthtest.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.hackertronix.firebaseauthtest.Favorites;
import com.example.hackertronix.firebaseauthtest.R;

/**
 * Created by hackertronix on 20/03/17.
 */

public class FavoriteWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for ( int appWidgetId : appWidgetIds ) {
            updateAppWidget(context,appWidgetManager, appWidgetId);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        setRemoteAdapter(context,remoteViews);

        Intent launchFavorites = new Intent(context,Favorites.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,launchFavorites,0);

        remoteViews.setOnClickPendingIntent(R.id.widget,pendingIntent);

        remoteViews.setEmptyView(R.id.widget_list,R.id.widget_empty);


        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_list);
        appWidgetManager.updateAppWidget(appWidgetId,remoteViews);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, FavoriteWidgetService.class));
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,getClass()));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.widget_list);
        super.onReceive(context, intent);
    }
}
