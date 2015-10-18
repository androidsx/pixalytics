package com.pixable.pixalytics.core.proxy;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pixable.pixalytics.core.Event;
import com.pixable.pixalytics.core.Screen;

import java.util.Map;

public interface PlatformProxy {

    /**
     * Registers the application creation. Usually, this loads the analytics library, and is to be
     * done when the application is loaded into memory.
     */
    void onApplicationCreate(Context context);

    /**
     * Tracks the start of a session. Usually, when the application comes into the foreground,
     * both the first time (right after {@link #onApplicationCreate} and on resume, after
     * being put in the background (e.g. home key, an incoming call).
     */
    void onSessionStart(Context context);

    /**
     * Tracks the finish of a session. Usually, when the application goes into the background. We
     * can't tell at this point whether the user will come back to the app or not.
     */
    void onSessionFinish(Context context);

    /**
     * Adds a common property, that is, a property that will be added in all events.
     *
     * @throws java.lang.UnsupportedOperationException if common properties are not supported by
     *                                                 this platform
     */
    void addCommonProperty(String name, @NonNull Object value);

    /**
     * Adds a common property, that is, a property that will be added in all events.
     *
     * @throws java.lang.UnsupportedOperationException if common properties are not supported by
     *                                                 this platform
     */
    void addCommonProperties(Map<String, Object> commonProperties);

    /**
     * Clears a common property, if it exists.
     *
     * @see #addCommonProperty
     */
    void clearCommonProperty(String name);

    /**
     * Clears all common properties, if any.
     *
     * @see #addCommonProperty
     */
    void clearCommonProperties();

    /**
     * Adds a user property, that is, a property that is linked to this user. Also known
     * as identification properties or traits.
     *
     * @throws java.lang.UnsupportedOperationException if user properties are not supported by
     *                                                 this platform
     */
    void addUserProperty(@NonNull String name, @NonNull Object value);

    /**
     * Tracks an event.
     */
    void trackEvent(Event event);

    /**
     * Tracks a screen transition.
     */
    void trackScreen(Screen screen);

    /**
     * Tracks a Social interaction.
     */
    void trackSocial(String network, String action, String target);

    /**
     * Flushes all events and screens, to force their sending to the platform. Useful while debugging.
     */
    void flush();

    /**
     * Gets the identifier for the current user.
     */
    String getIdentifier();

    /**
     * Sets a identifier to the current user. Usually, this ID should be unique for this user.
     */
    void setIdentifier(String identifier);
}
