package com.ang.acb.personalpins.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.ang.acb.personalpins.data.dao.BoardPinDao;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * A service to be connected to for a remote adapter to request RemoteViews.
 * It provides the RemoteViewsFactory used to populate the remote collection view.
 * RemoteViewsFactory is an interface for an adapter between a remote collection
 * view (such as ListView or GridView) and the underlying data for that view.
 *
 * https://developer.android.com/guide/topics/appwidgets#remoteviewsservice-class
 * https://stackoverflow.com/questions/51017541/how-to-provide-database-to-myfirebasemessagingservice-using-dagger-2-so-that-i-c
 */
public class BoardRemoteViewsService extends RemoteViewsService {

    @Inject
    BoardPinDao boardPinDao;

    @Override
    public void onCreate() {
        // Note: when using Dagger for injecting Service
        // objects, inject as early as possible.
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BoardRemoteViewsFactory(getApplicationContext(), boardPinDao);
    }
}
