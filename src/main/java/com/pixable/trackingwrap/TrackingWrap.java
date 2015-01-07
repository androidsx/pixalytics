package com.pixable.trackingwrap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

/**
 * Entry point for the tracking wrap library. To make usage simple, it is a singleton.
 *
 * In your {@link android.app.Application#onCreate} lifecycle method, construct the singleton
 * instance with {@link #createInstance}, and then use {@link #onApplicationCreate} to initialize
 * the application tracking.
 *
 * After that, use {@link #getInstance} and the other methods as needed.
 */
public class TrackingWrap {
    private static final String TAG = TrackingWrap.class.getSimpleName();
    static TrackingWrap INSTANCE;

    private final TrackingConfig configuration;

    private boolean initialized = false;
    private MixpanelAPI mixpanelAPI = null;

    private TrackingWrap(TrackingConfig configuration) {
        this.configuration = configuration;
    }

    public static TrackingWrap createInstance(TrackingConfig configuration) {
        if (INSTANCE == null) {
            INSTANCE = new TrackingWrap(configuration);
            return INSTANCE;
        } else {
            throw new IllegalStateException("The tracking wrap singleton was already instantiated");
        }
    }

    public static TrackingWrap getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("The tracking wrap singleton is not initialized");
        } else {
            return INSTANCE;
        }
    }

    /**
     * To be called from the {@code onCreate} method of your {@link android.app.Application}
     * instance.
     *
     * This is required.
     *
     * @param context application context
     */
    public void onApplicationCreate(Context context) {
        debugPrint(context, "Initialize tracking for " + configuration.getPlatforms().keySet());

        for (PlatformId platformId : configuration.getPlatforms().keySet()) {
            final PlatformConfig config = configuration.getPlatforms().get(platformId);

            switch (platformId) {
                case MIXPANEL: {
                    mixpanelAPI = MixpanelAPI.getInstance(context, config.getAppKey());
                }
                case FLURRY: {
                    FlurryAgent.init(context, config.getAppKey());
                    FlurryAgent.setLogEnabled(true); // Should be false
                    FlurryAgent.setLogLevel(Log.INFO); // Not needed, given the previous one
                }
            }
        }

        initialized = true;
    }

    /**
     * To be called from the {@code onStart} of every activity in your application. This lifecycle
     * method is used to track sessions. Not all tracking services support it.
     *
     * @param context activity context, not the global application context
     */
    public void onActivityStart(Context context) {
        checkAppIsInitialized();
        debugPrint(context, "Activity start: " + ((Activity)context).getLocalClassName());

        for (PlatformId platformId : configuration.getPlatforms().keySet()) {
            switch (platformId) {
                case MIXPANEL: {
                    throw new UnsupportedOperationException("Mixpanel does not support session tracking");
                    // FIXME: come up with a way for the lib user to mix Flurry and Mixpanel
                }
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
        checkAppIsInitialized();
        debugPrint(context, "Activity stop: " + ((Activity)context).getLocalClassName());

        for (PlatformId platformId : configuration.getPlatforms().keySet()) {
            switch (platformId) {
                case MIXPANEL: {
                    throw new UnsupportedOperationException("Mixpanel does not support session tracking");
                    // FIXME: come up with a way for the lib user to mix Flurry and Mixpanel
                }
                case FLURRY: {
                    FlurryAgent.onEndSession(context);
                }
            }
        }
    }

    /**
     * Adds a set of properties that are common to all events. Some providers manage this
     * automatically, such as Mixpanel's super-properties. For others, this library will add this
     * properties explicitly to every event.
     */
    public void addCommonProperties(Context context, Map<String, String> commonProperties) {
        checkAppIsInitialized();
        debugPrint(context, "Register " + commonProperties.size() + " common properties in "
                + configuration.getPlatforms().keySet());

        for (PlatformId platformId : configuration.getPlatforms().keySet()) {
            switch (platformId) {
                case MIXPANEL: {
                    mixpanelAPI.registerSuperProperties(new JSONObject(commonProperties));
                    break;
                }
                case FLURRY: {
                    throw new UnsupportedOperationException("Not implemented yet");
                }
            }
        }
    }

    /**
     * Tracks the provided event in the provided platforms.
     */
    public void trackEvent(Context context, TrackingEvent event, PlatformId... platformIds) {
        checkAppIsInitialized();
        debugPrint(context, "Track " + event + " to " + Arrays.asList(platformIds));

        for (PlatformId platformId : platformIds) {
            checkPlatformIsConfigured(platformId);
            switch (platformId) {
                case MIXPANEL: {
                    mixpanelAPI.track(event.getName(), event.getPropertiesAsJson());
                    break;
                }
                case FLURRY: {
                    FlurryAgent.logEvent(event.getName(), event.getProperties());
                    break;
                }
            }
        }
    }

    private void checkAppIsInitialized() {
        if (!initialized) {
            throw new IllegalStateException("Did you forget to call #onApplicationCreate?");
        }
    }

    private void checkPlatformIsConfigured(PlatformId platformId) {
        if (!configuration.getPlatforms().containsKey(platformId)) {
            throw new IllegalStateException("The platform " + platformId + " is not initialized."
                    + " Currently, only " + configuration.getPlatforms().size() + " are initialized: "
                    + configuration.getPlatforms().keySet());
        }
    }

    private void debugPrint(Context context, String message) {
        for (TrackingConfig.DebugPrint debugPrint : configuration.getDebugPrints()) {
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
