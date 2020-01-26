package com.ang.acb.personalpins.di;

import android.app.Application;

import androidx.room.Room;

import com.ang.acb.personalpins.data.dao.BoardDao;
import com.ang.acb.personalpins.data.dao.BoardPinDao;
import com.ang.acb.personalpins.data.dao.CommentDao;
import com.ang.acb.personalpins.data.dao.PinDao;
import com.ang.acb.personalpins.data.dao.TagDao;
import com.ang.acb.personalpins.data.db.AppDatabase;
import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    AppDatabase provideDatabase(Application app) {
        return Room.databaseBuilder(app, AppDatabase.class, "pins.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    BoardDao provideBoardDao(AppDatabase database) {
        return database.boardDao();
    }

    @Singleton
    @Provides
    PinDao providePinDao(AppDatabase database) {
        return database.pinDao();
    }

    @Singleton
    @Provides
    TagDao provideTagDao(AppDatabase database) {
        return database.tagDao();
    }

    @Singleton
    @Provides
    CommentDao provideCommentDao(AppDatabase database) {
        return database.commentDao();
    }

    @Singleton
    @Provides
    BoardPinDao provideBoardPinDao(AppDatabase database) {
        return database.boardPinDao();
    }

    @Singleton
    @Provides
    FirebaseAnalytics provideFirebaseAnalytics(Application application) {
        // https://medium.com/@cdmunoz/how-to-easily-integrate-firebase-analytics-into-your-android-app-8e7b8d69ab84
        // https://medium.com/exploring-android/exploring-firebase-on-android-ios-analytics-8484b61a21ba
        // Note: calling this method requiresINTERNET, ACCESS_NETWORK_STATE and WAKE_LOCK permissions.
        return FirebaseAnalytics.getInstance(application.getApplicationContext());
    }
}
