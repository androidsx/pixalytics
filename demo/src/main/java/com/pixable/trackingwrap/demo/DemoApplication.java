package com.pixable.trackingwrap.demo;

import android.app.Application;

import com.facebook.AppEventsConstants;
import com.pixable.trackingwrap.TrackingConfig;
import com.pixable.trackingwrap.TrackingWrap;
import com.pixable.trackingwrap.platform.FacebookPlatform;
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
                .addPlatform(new GoogleAnalyticsPlatform(new GoogleAnalyticsPlatform.Config("ga-app-key", gaParametersMapping)))
                .addPlatform(new FacebookPlatform(new FacebookPlatform.Config("fb-app-key", fbEventsMapping, fbParametersMapping)))
                .addTrace(TraceId.LOGCAT)
                .addTrace(TraceId.TOAST)
                .build();
        TrackingWrap.createInstance(configuration).onApplicationCreate(this);
    }

    private static Map<String, Integer> gaParametersMapping = new HashMap<>();
    static {
        gaParametersMapping.put("view", 1);
        gaParametersMapping.put("property2", 2);
        gaParametersMapping.put("property3", 3);
    }

    private static Map<String, String> fbEventsMapping = new HashMap<>();
    static {
        fbEventsMapping.put("foo", AppEventsConstants.EVENT_NAME_PURCHASED);
    }

    private static Map<String, String> fbParametersMapping = new HashMap<>();
    static {
        fbParametersMapping.put("view", AppEventsConstants.EVENT_PARAM_DESCRIPTION);
        fbParametersMapping.put("property2", AppEventsConstants.EVENT_PARAM_VALUE_NO);
    }
}
