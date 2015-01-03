package com.pixable.trackingwrap;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Entry point for the tracking wrap library. Build a wrap instance with your configuration and use
 * it to send events to your analytics platforms of choice.
 * <p>
 * It is strongly recommended that you initialize a singleton in your <em>Application</em> instance.
 * See the lifecycle methods (named like {@code onXXX}) and call them wherever needed.
 */
public class TrackingWrap {
    private static final String TAG = TrackingWrap.class.getSimpleName();

    private final TrackingConfiguration configuration;
    private final Set<TrackingDestination> initializedDestinations = new HashSet<>();

    public TrackingWrap(TrackingConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * To be called from the {@code onCreate} method of your {@link android.app.Application} instance.
     *
     * @param context application context
     * @param destinations destinations to be initialized. You will only be able to use these
     */
    public void onApplicationCreate(Context context, TrackingDestination... destinations) {
        for (TrackingDestination destination : destinations) {
            switch (destination.getPlatform()) {
                case GOOGLE_ANALYTICS: throw new UnsupportedOperationException("not yet");
                case MIXPANEL: throw new UnsupportedOperationException("not yet");
                case FLURRY: {
                    FlurryAgent.init(context, destination.getAppKey());
                    initializedDestinations.add(destination);
                }
            }
        }
    }

    /**
     * To be called from the {@code onStart} of every activity in your application.
     *
     * @param context activity context, not the global application context
     */
    public void onActivityStart(Context context) {
        for (TrackingDestination destination : initializedDestinations) {
            switch (destination.getPlatform()) {
                case GOOGLE_ANALYTICS: throw new UnsupportedOperationException("not yet");
                case MIXPANEL: throw new UnsupportedOperationException("not yet");
                case FLURRY: {
                    FlurryAgent.onStartSession(context);
                }
            }
        }
    }

    /**
     * To be called from the {@code onStop} of every activity in your application.
     *
     * @param context activity context, not the global application context
     */
    public void onActivityStop(Context context) {
        for (TrackingDestination destination : initializedDestinations) {
            switch (destination.getPlatform()) {
                case GOOGLE_ANALYTICS: throw new UnsupportedOperationException("not yet");
                case MIXPANEL: throw new UnsupportedOperationException("not yet");
                case FLURRY: {
                    FlurryAgent.onEndSession(context);
                }
            }
        }
    }

    /**
     * Tracks the provided event in the provided destinations.
     */
    public void trackEvent(Context context, TrackingEvent event,
                           TrackingDestination.Platform... platforms) {
        // First some logging
        for (TrackingConfiguration.DebugPrint debugPrint : configuration.getDebugPrints()) {
            switch (debugPrint) {
                case LOGCAT: {
                    Log.d(TAG, "Track " + event + " to " + Arrays.asList(platforms));
                    break;
                }
                case TOAST: {
                    Toast.makeText(context, "Track " + event, Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }

        // And here's the actual tracking
        for (TrackingDestination.Platform platform : platforms) {
            switch (platform) {
                case GOOGLE_ANALYTICS: Log.e(TAG, "Not implemented yet"); break;
                case MIXPANEL: Log.e(TAG, "Not implemented yet"); break;
                case FLURRY:
                    FlurryAgent.logEvent(event.getEventName(), event.getAllProperties());
                    break;
            }
        }
    }
}
