package com.pixable.trackingwrap.demo;

import android.app.Application;

import com.facebook.AppEventsConstants;
import com.pixable.pixalytics.core.Config;
import com.pixable.pixalytics.core.Pixalytics;
import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.core.trace.LogcatTraceProxy;
import com.pixable.pixalytics.core.trace.ToastTraceProxy;
import com.pixable.pixalytics.facebook.platform.FacebookPlatform;
import com.pixable.pixalytics.flurry.platform.FlurryPlatform;
import com.pixable.pixalytics.google_analytics.platform.GoogleAnalyticsPlatform;
import com.pixable.pixalytics.mixpanel.platform.MixpanelPlatform;

import java.util.HashMap;
import java.util.Map;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final Config configuration = new Config.Builder()
                .addPlatform(new FlurryPlatform(Constants.PlatformIds.FLURRY.name(), new Platform.Config("flurry-app-key")))
                .addPlatform(new MixpanelPlatform(Constants.PlatformIds.MIXPANEL.name(), new Platform.Config("mixpanel-app-key")))
                .addPlatform(new GoogleAnalyticsPlatform(Constants.PlatformIds.GOOGLE_ANALYTICS.name(), new GoogleAnalyticsPlatform.Config("ga-app-key", gaDimensionsMapping, gaMetricsMapping)))
                .addPlatform(new FacebookPlatform(Constants.PlatformIds.FACEBOOK.name(), new FacebookPlatform.Config("fb-app-key", fbEventsMapping, fbParametersMapping)))
                .addTrace(new LogcatTraceProxy(Constants.Traces.LOGCAT.name()))
                .addTrace(new ToastTraceProxy(this, Constants.Traces.TOASTS.name()))
                .build();
        Pixalytics.createInstance(configuration).onApplicationCreate(this);
    }

    private static Map<String, Integer> gaDimensionsMapping = new HashMap<>();
    static {
        gaDimensionsMapping.put("view", 1);
        gaDimensionsMapping.put("Key1", 2);
        gaDimensionsMapping.put("Key2", 3);
        gaDimensionsMapping.put("Key3", 4);
        gaDimensionsMapping.put("Key4", 5);
        gaDimensionsMapping.put("Key5", 6);
    }

    private static Map<String, Integer> gaMetricsMapping = new HashMap<>();
    static {
        gaMetricsMapping.put("Metric1", 1);
        gaMetricsMapping.put("Metric2", 2);
    }

    private static Map<String, String> fbEventsMapping = new HashMap<>();
    static {
        fbEventsMapping.put("foo", AppEventsConstants.EVENT_NAME_PURCHASED);
    }

    private static Map<String, String> fbParametersMapping = new HashMap<>();
    static {
        fbParametersMapping.put("view", AppEventsConstants.EVENT_PARAM_DESCRIPTION);
        fbParametersMapping.put("Key1", AppEventsConstants.EVENT_PARAM_VALUE_NO);
        fbParametersMapping.put("Key2", AppEventsConstants.EVENT_PARAM_VALUE_YES);
    }
}
