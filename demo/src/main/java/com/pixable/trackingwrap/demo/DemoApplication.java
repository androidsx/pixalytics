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
        Pixalytics.createInstance(this, configuration).onApplicationCreate();
    }

    private static Map<String, Integer> gaParametersMapping = new HashMap<>();
    static {
        gaParametersMapping.put("view", 1);
        gaParametersMapping.put("Key1", 2);
        gaParametersMapping.put("Key2", 3);
        gaParametersMapping.put("Key3", 4);
        gaParametersMapping.put("Key4", 5);
        gaParametersMapping.put("Key5", 6);
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
