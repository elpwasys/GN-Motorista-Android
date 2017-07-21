package br.com.wasys.library;

import android.content.Context;

/**
 * Created by pascke on 03/08/16.
 */
public class Application extends android.app.Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Application.instance = this;
    }

    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/

    public static Application getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Set " + Application.class.getName() + " in AndroidManifest.xml");
        }
        return instance;
    }

    public static Context getContext() {
        return getInstance().getBaseContext();
    }
}