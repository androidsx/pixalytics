package com.pixable.trackingwrap.core;

import android.content.Context;

import com.pixable.trackingwrap.core.platform.Platform;
import com.pixable.trackingwrap.core.proxy.PlatformProxy;
import com.pixable.trackingwrap.core.trace.TraceId;
import com.pixable.trackingwrap.core.trace.TraceProxy;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Entry point for the tracking wrap library. To make usage simple, it is a singleton.
 * <p/>
 * In your {@link android.app.Application#onCreate} lifecycle method, construct the singleton
 * instance with {@link #createInstance}, and then use {@link #onApplicationCreate} right there to
 * initialize the application tracking.
 * <p/>
 * After that, use {@link #get} to call the other methods as needed.
 */
public class TrackingWrap {
    private static final String TAG = TrackingWrap.class.getSimpleName();
    static TrackingWrap INSTANCE;

    private final TrackingConfig configuration;

    /**
     * Map of platform IDs to proxies to be able to have the client just use IDs.
     */
    private Map<String, PlatformProxy> platformProxyMap = new HashMap<>();
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
     * <p/>
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
                    configuration.getPlatforms());
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

        Set<Platform> platforms = getSessionPlatforms();

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    TraceProxy.Level.DEBUG,
                    "Session start",
                    Collections.<String, String>emptyMap(),
                    platforms);
        }

        for (Platform platform : platforms) {
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

        Set<Platform> platforms = getSessionPlatforms();

        for (Platform platform : platforms) {
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
                    configuration.getPlatforms());
        }

        for (Platform platform : configuration.getPlatforms()) {
            platform.getProxy().addCommonProperties(context, commonProperties);
        }
    }

    /**
     * @see #addCommonProperties
     */
    public void addCommonProperty(Context context, String name, String value) {
        final Map<String, String> propertyAsMap = new HashMap<>();
        propertyAsMap.put(name, value);

        addCommonProperties(context, propertyAsMap);
    }

    /**
     * Tracks the provided event in the provided platforms.
     */
    public void trackEvent(Context context, Event event, Set<String> platformIds) {
        checkAppIsInitialized();

        Set<Platform> platforms = new HashSet<Platform>();
        if (platformIds.size() == 0) {
            platforms = configuration.getPlatforms();
        } else {
            for(String platformId : platformIds) {
                Platform platform = getPlatformFromId(platformId);
                if(platform != null) {
                    platforms.add(platform);
                }
            }
        }

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    TraceProxy.Level.INFO,
                    "Event " + event.getName(),
                    event.getProperties(),
                    platforms);
        }

        for (Platform platform : platforms) {
            checkPlatformIsConfigured(platform.getId());
            platformProxyMap.get(platform.getId()).trackEvent(context, event);
        }
    }

    /**
     * Track Screen in all platform
     *
     * @param context
     * @param screen
     */
    public void trackScreen(Context context, Screen screen) {
        checkAppIsInitialized();

        Set<Platform> platforms = getScreenPlatforms();

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    TraceProxy.Level.INFO,
                    "Screen " + screen.getName(),
                    screen.getProperties(),
                    platforms);
        }

        for (Platform platform : platforms) {
            platform.getProxy().trackScreen(context, screen);
        }
    }

    private void checkAppIsInitialized() {
        if (!initialized) {
            throw new IllegalStateException("Did you forget to call #onApplicationCreate?");
        }
    }

    public Platform checkPlatformIsConfigured(String platformId) {
        Platform platform = getPlatformFromId(platformId);
        if(platform != null) {
            return platform;
        }

        throw new IllegalStateException("The platform " + platformId + " is not initialized."
                + " Currently, only " + configuration.getPlatforms().size() + " are initialized: "
                + configuration.getPlatforms().size());
    }

    private Platform getPlatformFromId(String platformId) {
        for (Platform platformTemp : configuration.getPlatforms()) {
            if (platformId.equals(platformTemp.getId())) {
                return platformTemp;
            }
        }
        return null;
    }

    private Set<Platform> getSessionPlatforms() {
        Set<Platform> filteredPlatforms = new HashSet<Platform>();
        for (Platform platform : configuration.getPlatforms()) {
            if (platform.getProxy().supportsSession()) {
                filteredPlatforms.add(platform);
            }
        }
        return filteredPlatforms;
    }

    private Set<Platform> getScreenPlatforms() {
        Set<Platform> filteredPlatforms = new HashSet<Platform>();
        for (Platform platform : configuration.getPlatforms()) {
            if (platform.getProxy().supportsScreens()) {
                filteredPlatforms.add(platform);
            }
        }
        return filteredPlatforms;
    }
}
