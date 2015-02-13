package com.pixable.trackingwrap.demo;

import android.app.Application;

import com.pixable.trackingwrap.TrackingConfig;
import com.pixable.trackingwrap.TrackingWrap;
import com.pixable.trackingwrap.platform.FlurryPlatform;
import com.pixable.trackingwrap.platform.GoogleAnalyticsPlatform;
import com.pixable.trackingwrap.platform.MixpanelPlatform;
import com.pixable.trackingwrap.platform.Platform;
import com.pixable.trackingwrap.trace.TraceId;

import java.util.HashMap;
import java.util.Map;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final TrackingConfig configuration = new TrackingConfig.Builder()
                .addPlatform(new FlurryPlatform(new Platform.Config("flurry-app-key")))
                .addPlatform(new MixpanelPlatform(new Platform.Config("mixpanel-app-key")))
                .addPlatform(new GoogleAnalyticsPlatform(new GoogleAnalyticsPlatform.Config("ga-app-key", getParametersMapping())))
                .addTrace(TraceId.LOGCAT)
                .addTrace(TraceId.TOAST)
                .build();
        TrackingWrap.createInstance(configuration).onApplicationCreate(this);
    }

    private Map<String, Integer> getParametersMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("view", 1);
        mapping.put("property2", 2);
        mapping.put("property3", 3);
        return mapping;
    }
}
