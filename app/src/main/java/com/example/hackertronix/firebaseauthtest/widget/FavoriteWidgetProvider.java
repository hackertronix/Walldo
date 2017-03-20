package com.example.hackertronix.firebaseauthtest.widget;

import android.app.PendingIntent;
import android.support.v4.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.hackertronix.firebaseauthtest.Favorites;
import com.example.hackertronix.firebaseauthtest.R;
import com.example.hackertronix.firebaseauthtest.utils.API;

/**
 * Created by hackertronix on 20/03/17.
 */

public class FavoriteWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for(int appWidgetId : appWidgetIds)
        {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Intent intent = new Intent(context, Favorites.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

            views.setOnClickPendingIntent(R.id.widget,pendingIntent);

            views.setRemoteAdapter(R.id.widget_list,new Intent(context,Favorites.class));

            Intent clickIntentTemplate = new Intent(context, Favorites.class);

            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);

            views.setEmptyView(R.id.widget_list, R.id.widget_empty);


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(API.ACTION_DATABASE_UPDATED.equals(intent.getAction()))
        {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);

            Toast.makeText(context,"Received something",Toast.LENGTH_SHORT).show();
            Log.d("TAG","IN ON RECIEVE");
        }
    }
}
