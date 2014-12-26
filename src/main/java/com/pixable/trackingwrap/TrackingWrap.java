package com.pixable.trackingwrap;

import android.util.Log;

import java.util.Arrays;

public class TrackingWrap {
    private static final String TAG = TrackingWrap.class.getSimpleName();

    private final TrackingConfiguration configuration;

    public TrackingWrap(TrackingConfiguration configuration) {
        this.configuration = configuration;
    }

    public void trackEvent(TrackingEvent event, TrackingDestination... destinations) {
        if (configuration.getDebugPrints().contains(TrackingConfiguration.DebugPrint.LOGCAT)) {
            Log.d(TAG, "Track event " + event + " to " + Arrays.asList(destinations));
        }
    }
}
