package com.pixable.trackingwrap;

import android.content.Context;

import com.pixable.trackingwrap.platform.Platform;
import com.pixable.trackingwrap.proxy.PlatformProxy;
import com.pixable.trackingwrap.trace.TraceId;
import com.pixable.trackingwrap.trace.TraceProxy;

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
                    TraceProxy.Level.DEBUG,
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
     * method is used to open session. Not all tracking services support it.
     *
     * @param context activity context, not the global application context
     */
    public void onSessionStart(Context context) {
        checkAppIsInitialized();

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    TraceProxy.Level.DEBUG,
                    "Session start",
                    Collections.<String, String>emptyMap(),
                    configuration.getPlatformIds());
        }

        for (Platform platform : configuration.getPlatforms()) {
            platform.getProxy().onSessionStart(context);
        }
    }

    /**
     * To be called from the {@code onStop} of every activity in your application.
     *
     * @param context activity context, not the global application context
     */
    public void onSessionFinish(Context context) {
        checkAppIsInitialized();

        for (Platform platform : configuration.getPlatforms()) {
            platform.getProxy().onSessionFinish(context);
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
                    TraceProxy.Level.DEBUG,
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

        if (platformIds.length == 0) {
            platformIds = configuration.getPlatformIds().toArray(new Platform.Id[configuration.getPlatformIds().size()]);
        }

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    TraceProxy.Level.INFO,
                    "Event " + event.getName(),
                    event.getProperties(),
                    Arrays.asList(platformIds));
        }

        for (Platform.Id platformId : platformIds) {
            checkPlatformIsConfigured(platformId);
            platformProxyMap.get(platformId).trackEvent(context, event);
        }
    }

    /**
     * Track Screen in all platform
     * @param context
     * @param screen
     */
    public void trackScreen(Context context, Screen screen) {
        checkAppIsInitialized();

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    TraceProxy.Level.INFO,
                    "Screen " + screen.getName(),
                    screen.getProperties(),
                    configuration.getPlatformIds());
        }

        for (Platform platform : configuration.getPlatforms()) {
            platform.getProxy().trackScreen(context, screen);
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
}
