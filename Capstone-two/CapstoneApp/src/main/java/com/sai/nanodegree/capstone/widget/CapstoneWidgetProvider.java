package com.sai.nanodegree.capstone.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sai.nanodegree.capstone.R;

/**
 * Implementation of App Widget functionality.
 */
public class CapstoneWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, WidgetUpdateService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String intentAction = intent.getAction();
        String actionStr = context.getString(R.string.widget_data_updated_action);
        if (intentAction.equals(actionStr)) {
            context.startService(new Intent(context, WidgetUpdateService.class));
        }
    }
}

