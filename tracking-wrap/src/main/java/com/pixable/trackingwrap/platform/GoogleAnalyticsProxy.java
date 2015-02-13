package com.pixable.trackingwrap.platform;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pixable.trackingwrap.Event;
import com.pixable.trackingwrap.Screen;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

class GoogleAnalyticsProxy implements PlatformProxy {

    private final static String TAG = GoogleAnalyticsProxy.class.getSimpleName();

    private final static String EVENT_CATEGORY = "Tracker Events";

    private final Platform.Config config;

    private Tracker tracker;

    public GoogleAnalyticsProxy(Platform.Config config) {
        this.config = config;
    }

    @Override
    public void onApplicationCreate(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        tracker = analytics.newTracker(config.getAppKey());
    }

    @Override
    public void onSessionStart(Context context) {
        //No need to open Session in GA
    }

    @Override
    public void onSessionFinish(Context context) {
        //No need to close Session in GA
    }

    @Override
    public void addCommonProperties(Context context, Map<String, String> commonProperties) {
        //GA doesn't support global properties
    }

    @Override
    public void trackEvent(Context context, Event event) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(EVENT_CATEGORY)
                .setAction(event.getName())
                .setLabel("");
        addDimensions(builder, event.getProperties());
        //TODO: Find a way of adding Metrics somehow
        tracker.send(builder.build());
    }

    @Override
    public void trackScreen(Context context, Screen screen) {
        tracker.setScreenName(screen.getName());
        HitBuilders.AppViewBuilder builder = new HitBuilders.AppViewBuilder();
        addDimensions(builder, screen.getProperties());
        tracker.send(builder.build());
    }

    /**
     * Add Dimensions to Screen or event according to its parameters
     * @param builder
     * @param properties
     */
    private void addDimensions(Object builder, Map<String, String> properties) {
        Iterator it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            try {
                int key = Integer.parseInt(pairs.getKey().toString());
                String value = pairs.getValue().toString();
                if (builder instanceof HitBuilders.AppViewBuilder) {
                    ((HitBuilders.AppViewBuilder) builder).setCustomDimension(key, value);
                } else {
                    ((HitBuilders.EventBuilder) builder).setCustomDimension(key, value);
                }
            }  catch(NumberFormatException nfe) {
                Log.e(TAG, "Parameter Key passed should be an Int. Cannot parse '" + pairs.getKey().toString() + "' as Integer");
            }
            it.remove();
        }
    }
}
