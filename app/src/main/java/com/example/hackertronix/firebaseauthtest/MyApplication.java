package com.example.hackertronix.firebaseauthtest;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by hackertronix on 20/03/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());    }
}
