package com.pixable.trackingwrap.demo;

import android.app.Application;

import com.facebook.AppEventsConstants;
import com.pixable.pixalytics.core.Config;
import com.pixable.pixalytics.core.Pixalytics;
import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.core.trace.TraceId;
import com.pixable.pixalytics.facebook.platform.FacebookPlatform;
import com.pixable.pixalytics.flurry.platform.FlurryPlatform;
import com.pixable.pixalytics.ga.platform.GoogleAnalyticsPlatform;
import com.pixable.pixalytics.mixpanel.platform.MixpanelPlatform;

import java.util.HashMap;
import java.util.Map;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final Config configuration = new Config.Builder()
                .addPlatform(new FlurryPlatform(new Platform.Config("flurry-app-key")))
                .addPlatform(new MixpanelPlatform(new Platform.Config("mixpanel-app-key")))
                .addPlatform(new GoogleAnalyticsPlatform(new GoogleAnalyticsPlatform.Config("ga-app-key", gaParametersMapping)))
                .addPlatform(new FacebookPlatform(new FacebookPlatform.Config("fb-app-key", fbEventsMapping, fbParametersMapping)))
                .addTrace(TraceId.LOGCAT)
                .addTrace(TraceId.TOAST)
                .build();
        Pixalytics.createInstance(configuration).onApplicationCreate(this);
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
