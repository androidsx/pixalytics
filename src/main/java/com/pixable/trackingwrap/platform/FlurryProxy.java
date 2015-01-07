package com.pixable.trackingwrap.platform;

import android.content.Context;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.pixable.trackingwrap.TrackingEvent;

import java.util.Map;

class FlurryProxy implements PlatformProxy {
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
    public void onActivityStart(Context context) {
        FlurryAgent.onStartSession(context);
    }

    @Override
    public void onActivityStop(Context context) {
        FlurryAgent.onEndSession(context);
    }

    @Override
    public void addCommonProperties(Context context, Map<String, String> commonProperties) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void trackEvent(Context context, TrackingEvent event) {
        FlurryAgent.logEvent(event.getName(), event.getProperties());
    }
}
