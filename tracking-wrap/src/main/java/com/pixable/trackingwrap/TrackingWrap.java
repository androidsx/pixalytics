package com.pixable.trackingwrap;

import android.app.Activity;
import android.content.Context;

import com.pixable.trackingwrap.platform.Platform;
import com.pixable.trackingwrap.platform.PlatformProxy;
import com.pixable.trackingwrap.trace.TraceId;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Entry point for the tracking wrap library. To make usage simple, it is a singleton.
 *
 * In your {@link android.app.Application#onCreate} lifecycle method, construct the singleton
 * instance with {@link #createInstance}, and then use {@link #onApplicationCreate} right there to
 * initialize the application tracking.
 *
 * After that, use {@link #get} to call the other methods as needed.
 *
 * Don't forget to track the activity start/stop. You can do it manually with the {@link #onActivityStart}
 * and {@link #onActivityStop} methods, or check the helper classes in {@link com.pixable.trackingwrap.helper}.
 */
public class TrackingWrap {
    private static final String TAG = TrackingWrap.class.getSimpleName();
    static TrackingWrap INSTANCE;

    private final TrackingConfig configuration;

    /** Map of platform IDs to proxies to be able to have the client just use IDs. */
    private Map<Platform.Id, PlatformProxy> platformProxyMap = new HashMap<>();
    private boolean initialized = false;

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

    /**
     * Returns the singleton instance. It should typically be called {@code getInstance}, but we
     * decided to make it shorter given that it's used extensively.
     */
    public static TrackingWrap get() {
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
        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    "On application create",
                    Collections.<String, String>emptyMap(),
                    configuration.getPlatformIds());
        }

        for (Platform platform : configuration.getPlatforms()) {
            platform.getProxy().onApplicationCreate(context);

            platformProxyMap.put(platform.getId(), platform.getProxy());
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

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    "Activity start: " + ((Activity) context).getClass().getSimpleName(),
                    Collections.<String, String>emptyMap(),
                    configuration.getPlatformIds());
        }

        for (Platform platform : configuration.getPlatforms()) {
            platform.getProxy().onActivityStart(context);
        }
    }

    /**
     * To be called from the {@code onStop} of every activity in your application.
     *
     * @param context activity context, not the global application context
     */
    public void onActivityStop(Context context) {
        checkAppIsInitialized();

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    "Activity stop: " + ((Activity)context).getClass().getSimpleName(),
                    Collections.<String, String>emptyMap(),
                    configuration.getPlatformIds());
        }

        for (Platform platform : configuration.getPlatforms()) {
            platform.getProxy().onActivityStop(context);
        }
    }

    /**
     * Adds a set of properties that are common to all events. Some providers manage this
     * automatically, such as Mixpanel's super-properties. For others, this library will add this
     * properties explicitly to every event.
     */
    public void addCommonProperties(Context context, Map<String, String> commonProperties) {
        checkAppIsInitialized();

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    "Register " + commonProperties.size() + " common properties",
                    commonProperties,
                    configuration.getPlatformIds());
        }

        for (Platform platform : configuration.getPlatforms()) {
            platform.getProxy().addCommonProperties(context, commonProperties);
        }
    }

    /** @see #addCommonProperties */
    public void addCommonProperty(Context context, String name, String value) {
        final Map<String, String> propertyAsMap = new HashMap<>();
        propertyAsMap.put(name, value);

        addCommonProperties(context, propertyAsMap);
    }

    /**
     * Tracks the provided event in the provided platforms.
     */
    public void trackEvent(Context context, Event event, Platform.Id... platformIds) {
        checkAppIsInitialized();
        checkAtLeastOnePlatform(event, platformIds);

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    "Event " + event.getName(),
                    event.getProperties(),
                    Arrays.asList(platformIds));
        }

        for (Platform.Id platformId : platformIds) {
            checkPlatformIsConfigured(platformId);
            platformProxyMap.get(platformId).trackEvent(context, event);
        }
    }

    private void checkAppIsInitialized() {
        if (!initialized) {
            throw new IllegalStateException("Did you forget to call #onApplicationCreate?");
        }
    }

    private void checkPlatformIsConfigured(Platform.Id platformId) {
        for (Platform platform : configuration.getPlatforms()) {
            if (platform.getId().equals(platformId)) {
                return;
            }
        }

        throw new IllegalStateException("The platform " + platformId + " is not initialized."
                + " Currently, only " + configuration.getPlatforms().size() + " are initialized: "
                + configuration.getPlatforms().size());
    }

    private void checkAtLeastOnePlatform(Event event, Platform.Id... platformIds) {
        if (platformIds.length == 0) {
            throw new IllegalArgumentException("Did you forget to add the platforms for the event "
                    + event.getName() + "?");
        }
    }
}
