package qsbk.app.remix.demo;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import qsbk.app.remao.sdk.RemaoSDK;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RemaoSDK.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

}
