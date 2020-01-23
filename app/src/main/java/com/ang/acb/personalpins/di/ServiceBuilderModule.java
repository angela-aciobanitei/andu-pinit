package com.ang.acb.personalpins.di;

import com.ang.acb.personalpins.ui.widget.BoardRemoteViewsService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ServiceBuilderModule {
    @ContributesAndroidInjector
    abstract BoardRemoteViewsService contributeRemoteViewsService();
}
