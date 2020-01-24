package com.ang.acb.personalpins.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.TaskStackBuilder;
import androidx.navigation.NavController;
import androidx.navigation.NavDeepLinkBuilder;

import com.ang.acb.personalpins.R;
import com.ang.acb.personalpins.ui.boards.BoardDetailsFragmentArgs;
import com.ang.acb.personalpins.ui.common.MainActivity;


/**
 * The AppWidgetProvider class implementation. Defines the basic methods that allow
 * you to programmatically interact with the App Widget, based on broadcast events.
 * Through it, you will receive broadcasts when the App Widget is updated, enabled,
 * disabled and deleted.
 *
 * See: https://developer.android.com/guide/topics/appwidgets#AppWidgetProvider
 */
public class BoardWidgetProvider extends AppWidgetProvider {

    private static final int REQUEST_CODE_MAIN_PENDING_INTENT = 100;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them.
        updateRecipeWidget(context, appWidgetManager, appWidgetIds);
    }

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            // Set up the intent that starts the RecipeRemoteViewsService,
            // which will provide the views for this collection.
            Intent serviceIntent = new Intent(context, BoardRemoteViewsService.class);
            // Add the app widget ID to the intent extras.
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews remoteViews = new RemoteViews(
                    context.getPackageName(), R.layout.widget_board);

            // Set widget title.
            remoteViews.setTextViewText(R.id.widget_board_title_tv,
                    PreferencesUtils.getWidgetTitle(context));

            // Set up the RemoteViews object to use a RemoteViews adapter. This
            // adapter connects to a RemoteViewsService  through the specified
            // intent. This is how we populate the data.
            remoteViews.setRemoteAdapter(R.id.widget_board_pins_lv, serviceIntent);

            // Set the empty view which is displayed when the collection has no items.
            // It should be a sibling of the collection view.
            remoteViews.setEmptyView(R.id.widget_board_pins_lv, R.id.widget_empty_state_tv);

            // Use Navigation Components's deep links to navigate to BoardDetailsFragment.
            // An explicit deep link is a single instance of a deep link that uses a
            // PendingIntent to take users to a specific location within your app. You
            // might surface an explicit deep link as part of a notification, or an app widget.
            // https://developer.android.com/guide/navigation/navigation-deep-link#explicit
            // https://codelabs.developers.google.com/codelabs/android-navigation/#9
            long boardId = PreferencesUtils.getWidgetBoardId(context);
            Bundle bundle = new Bundle();
            bundle.putLong("boardId", boardId);
            PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.board_details)
                    .setArguments(bundle)
                    .createPendingIntent();

            // When using collections (ListView, StackView etc.) in widgets,
            // it is very costly to set PendingIntents on the individual items,
            // and is hence not permitted. Instead this method should be used
            // to set a single PendingIntent template on the collection, and
            // individual items can differentiate their on-click behavior using
            // RemoteViews#setOnClickFillInIntent(int, Intent).
            // https://developer.android.com/guide/topics/appwidgets#setting-up-the-pending-intent-template
            remoteViews.setPendingIntentTemplate(R.id.widget_board_pins_lv, pendingIntent);

            // Equivalent of calling View.setOnClickListener(android.view.View.OnClickListener)
            // to launch the provided RemoteResponse.
            remoteViews.setOnClickPendingIntent(R.id.widget_board_container, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget.
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created.
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled.
    }
}
