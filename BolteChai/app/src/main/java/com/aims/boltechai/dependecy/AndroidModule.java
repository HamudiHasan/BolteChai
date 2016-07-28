package com.aims.boltechai.dependecy;

import android.content.Context;


import com.aims.boltechai.BolteChaiApp;
import com.aims.boltechai.util.annotation.ForApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Android-specific injection module for Dagger.
 */
@Module(library = true)
public class AndroidModule
{
    private final BolteChaiApp application;

    public AndroidModule(BolteChaiApp application)
    {
        this.application = application;
    }

    @Provides
    @Singleton
    @ForApplication
    public Context provideApplicationContext()
    {
        return application;
    }
}
