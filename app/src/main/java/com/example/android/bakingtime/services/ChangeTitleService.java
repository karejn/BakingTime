package com.example.android.bakingtime.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.bakingtime.widget.WidgetList;



public class ChangeTitleService extends IntentService {

    public static final String ACTION_CHANGE_TITLE = "CHANGE TITLE";

    public ChangeTitleService() {
        super("ChangeTitleService");
    }

    public static void startChanging(Context context){
        Intent intent = new Intent(context, ChangeTitleService.class);
        intent.setAction(ACTION_CHANGE_TITLE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CHANGE_TITLE.equals(action)) {
                updateWidget();
            }
        }
    }

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetList.class));
        WidgetList.updateWidgetTitle(this,appWidgetManager,appWidgetIds);
    }
}
