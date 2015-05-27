package com.pixable.pixalytics.google_analytics.proxy;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pixable.pixalytics.core.Event;
import com.pixable.pixalytics.core.Screen;
import com.pixable.pixalytics.core.proxy.PlatformProxy;
import com.pixable.pixalytics.google_analytics.platform.GoogleAnalyticsPlatform;

import java.util.Iterator;
import java.util.Map;

public class GoogleAnalyticsProxy implements PlatformProxy {

    private final static String TAG = GoogleAnalyticsProxy.class.getSimpleName();

    private final static String EVENT_CATEGORY = "Tracker Events";

    private final GoogleAnalyticsPlatform.Config config;

    private Tracker tracker;

    public GoogleAnalyticsProxy(GoogleAnalyticsPlatform.Config config) {
        this.config = config;
    }

    @Override
    public void onApplicationCreate(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        tracker = analytics.newTracker(config.getAppKey());
    }

    @Override
    public void onSessionStart(Context context) {
        throw new UnsupportedOperationException("Google Analytics does not support session tracking");
    }

    @Override
    public void onSessionFinish(Context context) {
        throw new UnsupportedOperationException("Google Analytics does not support session tracking");
    }

    @Override
    public void addCommonProperty(String name, Object value) {
        throw new UnsupportedOperationException("Google Analytics does not support common properties");
    }

    @Override
    public void addCommonProperties(Map<String, Object> commonProperties) {
        throw new UnsupportedOperationException("Google Analytics does not support common properties");
    }

    @Override
    public void clearCommonProperty(String name) {
        throw new UnsupportedOperationException("Google Analytics does not support common properties");
    }

    @Override
    public void clearCommonProperties() {
        throw new UnsupportedOperationException("Google Analytics does not support common properties");
    }

    @Override
    public void trackEvent(Event event) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(EVENT_CATEGORY)
                .setAction(event.getName())
                .setLabel("");
        addDimensions(builder, event.getProperties());
        //TODO: Find a way of adding Metrics somehow
        tracker.send(builder.build());
    }

    @Override
    public void trackScreen(Screen screen) {
        tracker.setScreenName(screen.getName());
        HitBuilders.AppViewBuilder builder = new HitBuilders.AppViewBuilder();
        addDimensions(builder, screen.getProperties());
        tracker.send(builder.build());
    }

    @Override
    public void trackSocial(String network, String action, String target) {
        HitBuilders.SocialBuilder builder = new HitBuilders.SocialBuilder()
                .setNetwork(network)
                .setAction(action)
                .setTarget(target);
        tracker.send(builder.build());
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Google Analytics does not support Flush");
    }

    @Override
    public void setIdentifier(String identifier) {
        throw new UnsupportedOperationException("Google Analytics does not support identifier Management");
    }

    @Override
    public String getIdentifier() {
        throw new UnsupportedOperationException("Google Analytics does not support identifier Management");
    }

    /**
     * Add Dimensions to Screen or event according to its parameters
     *
     * @param builder
     * @param properties
     */
    private void addDimensions(Object builder, Map<String, Object> properties) {
        Iterator it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            int key = getParameterIdForKey(pairs.getKey().toString());
            if (key >= 0) {
                String value = pairs.getValue().toString();
                if (builder instanceof HitBuilders.AppViewBuilder) {
                    ((HitBuilders.AppViewBuilder) builder).setCustomDimension(key, value);
                } else {
                    ((HitBuilders.EventBuilder) builder).setCustomDimension(key, value);
                }
            }
            it.remove();
        }
    }

    private int getParameterIdForKey(String key) {
        if (config.getParameterMapping().containsKey(key)) {
            return config.getParameterMapping().get(key);
        } else {
            Log.e(TAG, "No Mapping found for parameter '" + key + "'");
            return -1;
        }
    }
}
