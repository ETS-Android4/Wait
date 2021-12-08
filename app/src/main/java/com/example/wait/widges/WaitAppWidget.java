package com.example.wait.widges;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.wait.R;

/**
 * Implementation of App Widget functionality.
 */
public class WaitAppWidget extends AppWidgetProvider {

    private static final String CLICK_ACTION = "com.example.wait.widget.CLICK";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // TODO: Construct the RemoteViews object
//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wait_app_widget);
//        views.setTextViewText(R.id.appwidgetRemainedDays_text, widgetText);


        // TODO: Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wait_app_widget);
        int minWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int minHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        int maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        String dimensions = "Min width: " + minWidth + "\nMax height: " + maxHeight + "\nMax width: " + maxWidth + "\nMax height: " + minHeight;
        Toast.makeText(context, dimensions, Toast.LENGTH_SHORT).show();

        if (minWidth < 250) {
            views.setViewVisibility(R.id.appwidgetTargetDate_text, View.GONE);
//            views.setTextViewTextSize();
        } else if (minWidth > 250) {
            views.setViewVisibility(R.id.appwidgetTargetDate_text, View.VISIBLE);
            if (maxHeight > 150){
                views.setTextViewTextSize(R.id.appwidgetTitle_text, TypedValue.COMPLEX_UNIT_SP, 36);
                views.setTextViewTextSize(R.id.appwidgetTargetDate_text, TypedValue.COMPLEX_UNIT_SP, 24);
                views.setTextViewTextSize(R.id.appwidgetRemainedDays_text, TypedValue.COMPLEX_UNIT_SP, 48);
                views.setTextViewTextSize(R.id.appwidgetDay_text, TypedValue.COMPLEX_UNIT_SP, 24);
                views.setViewPadding(R.id.appwidgetTargetDate_text, 0,100,0,0);
                views.setViewPadding(R.id.appwidgetRemainedDays_LL, 0,0,0,100);
            }else {
                views.setTextViewTextSize(R.id.appwidgetTitle_text, TypedValue.COMPLEX_UNIT_SP, 20);
                views.setTextViewTextSize(R.id.appwidgetTargetDate_text, TypedValue.COMPLEX_UNIT_SP, 15);
                views.setTextViewTextSize(R.id.appwidgetRemainedDays_text, TypedValue.COMPLEX_UNIT_SP, 36);
                views.setTextViewTextSize(R.id.appwidgetDay_text, TypedValue.COMPLEX_UNIT_SP, 18);
                views.setViewPadding(R.id.appwidgetTargetDate_text, 0,0,0,0);
                views.setViewPadding(R.id.appwidgetRemainedDays_LL, 0,0,0,0);
            }
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}