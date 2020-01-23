package com.ang.acb.personalpins.ui.widget;

import android.content.Context;
import android.preference.PreferenceManager;

import com.ang.acb.personalpins.R;

public class PreferencesUtils {

    // Prevent direct instantiation.
    private PreferencesUtils() {}

    public static void setWidgetBoardId(Context context, long boardId) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putLong(context.getString(R.string.pref_widget_key), boardId)
                .apply();
    }

    public static long getWidgetBoardId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(context.getString(R.string.pref_widget_key), -1);
    }

    public static void setWidgetTitle(Context context, String title) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(context.getString(R.string.pref_title_key), title)
                .apply();
    }

    public static String getWidgetTitle(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_title_key),
                        context.getString(R.string.widget_text));
    }
}
