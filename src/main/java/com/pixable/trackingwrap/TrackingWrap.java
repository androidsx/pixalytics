package com.pixable.trackingwrap;

import android.util.Log;

import java.util.Arrays;

/**
 * Entry point for the tracking wrap library. Build a wrap instance with your configuration and use
 * it to send events to your analytics platforms of choice.
 */
public class TrackingWrap {
    private static final String TAG = TrackingWrap.class.getSimpleName();

    private final TrackingConfiguration configuration;

    public TrackingWrap(TrackingConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Tracks the provided event in the provided destinations.
     */
    public void trackEvent(TrackingEvent event, TrackingDestination... destinations) {
        if (configuration.getDebugPrints().contains(TrackingConfiguration.DebugPrint.LOGCAT)) {
            Log.d(TAG, "Track event " + event + " to " + Arrays.asList(destinations));
        }
    }
}
