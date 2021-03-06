package com.example.android.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.provider.ContractClass;


public class MyWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    private Context mContext;
    private Cursor mCursor;

    public MyWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        if (mCursor != null) {
            mCursor.close();
        }

        SharedPreferences sharedPreferences= mContext.getApplicationContext().getSharedPreferences("ingrad_pref",0);
        String dummy= sharedPreferences.getInt("selection",4)+"";

        final long identityToken = Binder.clearCallingIdentity();
        mCursor = mContext.getContentResolver().query(ContractClass.nameClass.CONTENT_URI,
                null,
                ContractClass.nameClass.COLUMN_INGRED_KEY + " =?",
                new String[]{dummy},
                null
        );

        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        rv.setTextViewText(R.id.widgetItemTaskNameLabel, mCursor.getString(2));
        rv.setTextViewText(R.id.quanty,mCursor.getString(4));
        rv.setTextViewText(R.id.measu,mCursor.getString(3));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(WidgetList.EXTRA_LABEL, mCursor.getString(2));
        rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

