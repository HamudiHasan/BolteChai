package com.aims.boltechai;

import android.app.Application;
import android.content.Context;


import com.aims.boltechai.dependecy.AndroidModule;
import com.aims.boltechai.dependecy.AppModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by Zakir on 17/02/2016.
 */

public class BolteChaiApp extends Application {

    private ObjectGraph objectGraph;
    private static BolteChaiApp instance;

    public static BolteChaiApp getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(getModules().toArray());
        instance = this;
        //startServices();
    }



    public void inject(Object object) {
        objectGraph.inject(object);
    }

    private List<Object> getModules() {
        return Arrays.asList(
                new AndroidModule(this),
                new AppModule()
        );
    }

}
