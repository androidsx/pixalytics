package com.pixable.trackingwrap;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
    public void trackEvent(Context context, TrackingEvent event, TrackingDestination... destinations) {
        // First some logging
        for (TrackingConfiguration.DebugPrint debugPrint : configuration.getDebugPrints()) {
            switch (debugPrint) {
                case LOGCAT: {
                    Log.d(TAG, "Track " + event + " to " + Arrays.asList(destinations));
                    break;
                }
                case TOAST: {
                    Toast.makeText(context, "Track " + event, Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }

        // And here's the actual tracking
        for (TrackingDestination destination : destinations) {
            switch (destination) {
                case GOOGLE_ANALYTICS: Log.e(TAG, "Not implemented yet"); break;
                case MIXPANEL: Log.e(TAG, "Not implemented yet"); break;
                case FLURRY: Log.e(TAG, "Not implemented yet"); break;
            }
        }
    }
}
