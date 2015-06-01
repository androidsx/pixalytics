package com.pixable.pixalytics.core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.core.proxy.PlatformProxy;
import com.pixable.pixalytics.core.trace.TraceId;
import com.pixable.pixalytics.core.trace.TraceProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Entry point for the Pixalytics. To make usage simple, it is a singleton.
 * <p/>
 * In your {@link android.app.Application#onCreate} lifecycle method, construct the singleton
 * instance with {@link #createInstance}, and then use {@link #onApplicationCreate} right there to
 * initialize the application tracking.
 * <p/>
 * After that, use {@link #get} to call the other methods as needed.
 */
public class Pixalytics {
    private static final String TAG = Pixalytics.class.getSimpleName();
    static Pixalytics INSTANCE;

    private Config configuration;

    /**
     * Map of platform IDs to proxies to be able to have the client just use IDs.
     */
    private Map<String, PlatformProxy> platformProxyMap = new HashMap<>();
    private boolean initialized = false;

    private Pixalytics(Config configuration) {
        this.configuration = configuration;
    }

    public static Pixalytics createInstance(Config configuration) {
        if (INSTANCE == null) {
            INSTANCE = new Pixalytics(configuration);
            return INSTANCE;
        } else {
            throw new IllegalStateException("The singleton was already instantiated");
        }
    }

    /**
     * Returns the singleton instance. It should typically be called {@code getInstance}, but we
     * decided to make it shorter given that it's used extensively.
     */
    public static Pixalytics get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("The singleton is not initialized");
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
                    Collections.<String, Object>emptyMap(),
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
     * @param platformIds platforms to which this event is to be sent. At least one platform must
     *                    be provided
     */
    public void onSessionStart(Context context, String... platformIds) {
        final Set<Platform> platforms = checkAndGetPlatformsFromIds(platformIds);

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    TraceProxy.Level.DEBUG,
                    "Session start",
                    Collections.<String, Object>emptyMap(),
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
     * @param platformIds platforms to which this event is to be sent. At least one platform must
     *                    be provided
     */
    public void onSessionFinish(Context context, String... platformIds) {
        final Set<Platform> platforms = checkAndGetPlatformsFromIds(platformIds);

        for (Platform platform : platforms) {
            platform.getProxy().onSessionFinish(context);
        }
    }

    /**
     * Adds a single property to specified platforms. In some platform, this is a native
     * concept, such as Mixpanel's super-properties. For others, this is managed by an
     * external mechanism or not supported.
     *
     * @param context activity context
     * @param name name of the property to add
     * @param value value of the property to add. If null, the property will be cleared
     * @param platformIds platforms to which this event is to be sent. At least one platform must
     *                    be provided
     */
    public void addCommonProperty(Context context, final String name, @NonNull final Object value,
                                  String... platformIds) {
        if (value == null) {
            clearCommonProperty(name, platformIds);
        } else {
            final Set<Platform> platforms = checkAndGetPlatformsFromIds(platformIds);

            for (TraceId traceId : configuration.getTraceIds()) {
                traceId.getProxy().traceMessage(context,
                        TraceProxy.Level.DEBUG,
                        "Register common property",
                        new HashMap<String, Object>() {{
                            put(name, value);
                        }},
                        platforms);
            }

            for (Platform platform : platforms) {
                platform.getProxy().addCommonProperty(name, value);
            }
        }
    }

    /**
     * @see #addCommonProperty
     */
    public void addCommonProperties(Context context, Map<String, Object> commonProperties,
                                    String... platformIds) {
        for (Map.Entry<String, Object> entry : commonProperties.entrySet()) {
            addCommonProperty(context, entry.getKey(), entry.getValue(), platformIds);
        }
    }

    /**
     * Clears a common property for the specified platforms, if it exists.
     *
     * @param platformIds platforms where to clear this property. At least one platform must
     *                    be provided
     */
    public void clearCommonProperty(String name, String... platformIds) {
        final Set<Platform> platforms = checkAndGetPlatformsFromIds(platformIds);

        for (Platform platform : platforms) {
            platform.getProxy().clearCommonProperty(name);
        }
    }

    /**
     * Clears all common properties for the specified platforms, if any.
     *
     * @param platformIds platforms where to clear common properties. At least one platform must
     *                    be provided
     */
    public void clearCommonProperties(String... platformIds) {
        final Set<Platform> platforms = checkAndGetPlatformsFromIds(platformIds);

        for (Platform platform : platforms) {
            platform.getProxy().clearCommonProperties();
        }
    }

    /**
     * Tracks the provided event in the provided platforms.
     *
     * @param context activity context
     * @param event event to be tracked
     * @param platformIds platforms to which this event is to be sent. At least one platform must
     *                    be provided
     */
    public void trackEvent(Context context, Event event, String... platformIds) {
        final Set<Platform> platforms = checkAndGetPlatformsFromIds(platformIds);

        // Trace
        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    TraceProxy.Level.INFO,
                    event.getName(),
                    event.getProperties(),
                    platforms);
        }

        // Track the event
        for (Platform platform : platforms) {
            platformProxyMap.get(platform.getId()).trackEvent(event);
        }
    }

    /**
     * Tracks a screen transition in the provided platforms.
     *
     * @param context activity context
     * @param screen screen to be tracked
     * @param platformIds platforms to which this event is to be sent. At least one platform must
     *                    be provided
     */
    public void trackScreen(Context context, Screen screen, String... platformIds) {
        final Set<Platform> platforms = checkAndGetPlatformsFromIds(platformIds);

        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    TraceProxy.Level.INFO,
                    "Screen " + screen.getName(),
                    screen.getProperties(),
                    platforms);
        }

        for (Platform platform : platforms) {
            platform.getProxy().trackScreen(screen);
        }
    }

    /**
     * Tracks a social interaction
     *
     * @param context activity context
     * @param network network where social interaction happened
     * @param action action happening in social interaction
     * @param target target associated to social interaction
     * @param platformIds platforms to which this event is to be sent. At least one platform must
     *                    be provided
     */
    public void trackSocial(Context context, String network, String action, String target, String... platformIds) {
        final Set<Platform> platforms = checkAndGetPlatformsFromIds(platformIds);
        Map<String, Object> properties = new HashMap<>();
        properties.put("network", network);
        properties.put("action", action);
        properties.put("target", target);
        for (TraceId traceId : configuration.getTraceIds()) {
            traceId.getProxy().traceMessage(context,
                    TraceProxy.Level.INFO,
                    "Social Interaction",
                    properties,
                    platforms);
        }

        for (Platform platform : platforms) {
            platform.getProxy().trackSocial(network, action, target);
        }
    }

    /**
     * Flushes all events, that is, forces immediate sending to the server.
     *
     * @param platformIds platforms to flush. At least one platform must
     *                    be provided
     */
    public void flush(String... platformIds) {
        final Set<Platform> platforms = checkAndGetPlatformsFromIds(platformIds);

        for (Platform platform : platforms) {
            platform.getProxy().flush();
        }
    }

    /**
     * Sets the user identifier. Not all platforms support this feature.
     *
     * @param identifier Identifier for the current user
     * @param platformIds platforms where to set the user identifier. At least one platform must
     *                    be provided
     */
    public void setIdentifier(String identifier, String... platformIds) {
        final Set<Platform> platforms = checkAndGetPlatformsFromIds(platformIds);

        for (Platform platform : platforms) {
            platform.getProxy().setIdentifier(identifier);
        }
    }

    /**
     * Gets the identifier for the current user in the provided platform.
     */
    public String getIdentifier(String platformId) {
        checkAppIsInitialized();
        checkPlatformsAreValid(new String[] {platformId});

        return getPlatformFromId(platformId).getProxy().getIdentifier();
    }

    /**
     * Returns the current configuration.
     */
    public Config getConfig() {
        return configuration;
    }

    /**
     * Updates the configuration.
     */
    public void updateConfiguration(Config config) {
        this.configuration = config;
    }

    private Set<Platform> checkAndGetPlatformsFromIds(String... platformIds) {
        checkAppIsInitialized();
        checkPlatformsAreValid(platformIds);

        return idsToPlatforms(platformIds);
    }
    private Set<Platform> idsToPlatforms(String[] platformIds) {
        final Set<Platform> platforms = new HashSet<>();
        for(String platformId : platformIds) {
            platforms.add(getPlatformFromId(platformId));
        }
        return platforms;
    }

    private void checkAppIsInitialized() {
        if (!initialized) {
            throw new IllegalStateException("Did you forget to call #onApplicationCreate?");
        }
    }

    private void checkPlatformsAreValid(String[] platformIds) {
        if (platformIds == null || platformIds.length == 0) {
            throw new IllegalArgumentException("At least one platform must be provided");
        } else {
            for (String platformId: platformIds) {
                Platform platform = getPlatformFromId(platformId);
                //noinspection StatementWithEmptyBody
                if (platform == null) {
                    throw new IllegalStateException("The platform " + platformId + " is not initialized."
                            + " Currently, only " + configuration.getPlatforms().size() + " are initialized: "
                            + configuration.getPlatforms().size());
                } else {
                    // All good, this platform is configured
                }
            }
        }
    }

    private Platform getPlatformFromId(String platformId) {
        for (Platform platformTemp : configuration.getPlatforms()) {
            if (platformId.equals(platformTemp.getId())) {
                return platformTemp;
            }
        }
        return null;
    }
}
