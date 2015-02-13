package com.pixable.trackingwrap.proxy;

import android.content.Context;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.pixable.trackingwrap.Event;
import com.pixable.trackingwrap.Screen;
import com.pixable.trackingwrap.platform.Platform;

import java.util.Map;

public class FlurryProxy extends PlatformProxy {
    private final Platform.Config config;

    public FlurryProxy(Platform.Config config) {
        this.config = config;
    }

    @Override
    public void onApplicationCreate(Context context) {
        FlurryAgent.init(context, config.getAppKey());
        FlurryAgent.setLogEnabled(true); // Should be false
        FlurryAgent.setLogLevel(Log.INFO); // Not needed, given the previous one
    }

    @Override
    public void onSessionStart(Context context) {
        FlurryAgent.onStartSession(context);
    }

    @Override
    public void onSessionFinish(Context context) {
        FlurryAgent.onEndSession(context);
    }

    @Override
    public void addCommonProperties(Context context, Map<String, String> commonProperties) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void trackEvent(Context context, Event event) {
        FlurryAgent.logEvent(event.getName(), event.getProperties());
    }


    @Override
    public void trackScreen(Context context, Screen screen) {
        //No Screens in Flurry
    }
}
