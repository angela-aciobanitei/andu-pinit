package com.ang.acb.personalpins.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ang.acb.personalpins.R;
import com.ang.acb.personalpins.data.dao.BoardPinDao;
import com.ang.acb.personalpins.data.entity.Pin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * A custom class that implements the RemoteViewsFactory interface and provides
 * the app widget with the data for the items in its collection.
 *
 * See: https://developer.android.com/guide/topics/appwidgets#remoteviewsfactory-interface
 */
public class BoardRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    @Inject
    BoardPinDao boardPinDao;
    private Context context;
    private List<Pin> pins;

    @Inject
    BoardRemoteViewsFactory(Context context, BoardPinDao boardPinDao) {
        this.context = context;
        this.boardPinDao = boardPinDao;
    }

    @Override
    public void onCreate() {}


    /**
     * Called when notifyDataSetChanged() is triggered on the remote adapter.
     * This allows a RemoteViewsFactory to respond to data changes by updating
     * any internal references. Note: expensive tasks can be safely performed
     * synchronously within this method. In the interim, the old data will be
     * displayed within the widget.
     */
    @Override
    public void onDataSetChanged() {
        long boardId = PreferencesUtils.getWidgetBoardId(context);

        if (boardId != -1) {
            pins = boardPinDao.getPinsForBoardSync(boardId);
        }
    }

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        return pins == null ? 0 : pins.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || pins == null) {
            return null;
        }

        // Construct a remote views item based on the app widget item XML file.
        RemoteViews remoteViews =  new RemoteViews(
                context.getPackageName(),
                R.layout.widget_pin_item);

        // Set the remote views item text based on the position.
        remoteViews.setTextViewText(
                R.id.widget_pin_item,
                pins.get(position).getTitle());

        // When using collections (eg. ListView, StackView etc.) in widgets,
        // it is very costly to set PendingIntents on the individual items,
        // and is hence not permitted. Instead this method should be used
        // to set a single PendingIntent template on the collection, and
        // individual items can differentiate their on-click behavior using
        // RemoteViews#setOnClickFillInIntent(int, Intent).
        // See: https://developer.android.com/guide/topics/appwidgets#setting-the-fill-in-intent
        remoteViews.setOnClickFillInIntent(R.id.widget_pin_item, new Intent());

        return remoteViews;
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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
