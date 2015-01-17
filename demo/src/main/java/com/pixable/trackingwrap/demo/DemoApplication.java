package com.pixable.trackingwrap.demo;

import android.app.Application;

import com.pixable.trackingwrap.TrackingConfig;
import com.pixable.trackingwrap.TrackingWrap;
import com.pixable.trackingwrap.platform.Platform;
import com.pixable.trackingwrap.trace.TraceId;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final TrackingConfig configuration = new TrackingConfig.Builder()
                .addPlatform(new Platform(Platform.Id.FLURRY, new Platform.Config("flurry-app-key")))
                .addTrace(TraceId.LOGCAT)
                .addTrace(TraceId.TOAST)
                .build();
        TrackingWrap.createInstance(configuration).onApplicationCreate(this);
    }
}
