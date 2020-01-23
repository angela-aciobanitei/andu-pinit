package com.ang.acb.personalpins.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.ang.acb.personalpins.R;
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
            Intent intent = new Intent(context, BoardRemoteViewsService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews remoteViews = new RemoteViews(
                    context.getPackageName(), R.layout.widget_board);

            // Set widget title.
            remoteViews.setTextViewText(R.id.widget_ingredients_list_title,
                    PreferencesUtils.getWidgetTitle(context));

            // Set up the RemoteViews object to use a RemoteViews adapter. This
            // adapter connects to a RemoteViewsService  through the specified
            // intent. This is how we populate the data.
            remoteViews.setRemoteAdapter(R.id.widget_pin_list_items, intent);

            // Set the empty view which is displayed when the collection has no items.
            // It should be a sibling of the collection view.
            remoteViews.setEmptyView(R.id.widget_pin_list_items, R.id.empty_view);

            // Create an pending intent to launch MainActivity.
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId,
                    new Intent(context, MainActivity.class), 0);

            // When using collections (ListView, StackView etc.) in widgets,
            // it is very costly to set PendingIntents on the individual items,
            // and is hence not permitted. Instead this method should be used
            // to set a single PendingIntent template on the collection, and
            // individual items can differentiate their on-click behavior using
            // RemoteViews#setOnClickFillInIntent(int, Intent).
            // See: https://developer.android.com/guide/topics/appwidgets#setting-up-the-pending-intent-template
            remoteViews.setPendingIntentTemplate(
                    R.id.widget_pin_list_items, pendingIntent);

            // Equivalent of calling View.setOnClickListener(android.view.View.OnClickListener)
            // to launch the provided RemoteResponse.
            remoteViews.setOnClickPendingIntent(
                    R.id.widget_board_container, pendingIntent);

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
