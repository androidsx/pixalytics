package com.pixable.trackingwrap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Entry point for the tracking wrap library. To make usage simple, it is a singleton. Use the
 * {@link #initialize} method once, and then grab the instance with {@link #getInstance}. It is
 * strongly recommended that you perform the initialization from your
 * {@link android.app.Application#onCreate} method.
 *
 * See the lifecycle methods (named like {@code onXXX}) and call them wherever needed. Track your
 * custom events with {@link #trackEvent}.
 */
public class TrackingWrap {
    private static final String TAG = TrackingWrap.class.getSimpleName();
    private static TrackingWrap INSTANCE;

    private final TrackingConfiguration configuration;
    private final Set<TrackingDestination> initializedDestinations = new HashSet<>();

    private TrackingWrap(TrackingConfiguration configuration) {
        this.configuration = configuration;
    }

    public static void initialize(TrackingConfiguration configuration) {
        INSTANCE = new TrackingWrap(configuration);
    }

    public static TrackingWrap getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("The tracking wrap singleton is not initialized");
        } else {
            return INSTANCE;
        }
    }

    /**
     * To be called from the {@code onCreate} method of your {@link android.app.Application} instance.
     *
     * @param context application context
     * @param destinations destinations to be initialized. You will only be able to use these
     */
    public void onApplicationCreate(Context context, TrackingDestination... destinations) {
        debugPrint(context, "Initialize tracking for " + Arrays.asList(destinations));

        for (TrackingDestination destination : destinations) {
            switch (destination.getPlatform()) {
                case MIXPANEL: throw new UnsupportedOperationException("not yet");
                case FLURRY: {
                    FlurryAgent.init(context, destination.getAppKey());
                    FlurryAgent.setLogEnabled(true); // Should be false
                    FlurryAgent.setLogLevel(Log.INFO); // Not needed, given the previous one
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
        debugPrint(context, "Activity start: " + ((Activity)context).getLocalClassName());

        for (TrackingDestination destination : initializedDestinations) {
            switch (destination.getPlatform()) {
                case MIXPANEL: throw new UnsupportedOperationException("not yet");
                case FLURRY: {
                    FlurryAgent.onStartSession(context);
                    break;
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
        debugPrint(context, "Activity stop: " + ((Activity)context).getLocalClassName());

        for (TrackingDestination destination : initializedDestinations) {
            switch (destination.getPlatform()) {
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
        debugPrint(context, "Track " + event + " to " + Arrays.asList(platforms));

        for (TrackingDestination.Platform platform : platforms) {
            switch (platform) {
                case MIXPANEL: Log.e(TAG, "Not implemented yet"); break;
                case FLURRY:
                    FlurryAgent.logEvent(event.getName(), event.getProperties());
                    break;
            }
        }
    }

    private void debugPrint(Context context, String message) {
        for (TrackingConfiguration.DebugPrint debugPrint : configuration.getDebugPrints()) {
            switch (debugPrint) {
                case LOGCAT: {
                    Log.d(TAG, message);
                    break;
                }
                case TOAST: {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    }
}
