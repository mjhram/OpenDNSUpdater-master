package net.dgistudio.guillaume.opendnsupdater;

import android.app.Application;

/**
 * Created by hp on 4/16/2018.
 */

public class MyApplication extends Application
{
    private static MyApplication instance;

    public static synchronized MyApplication getAppInstance() { return instance; }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }
}