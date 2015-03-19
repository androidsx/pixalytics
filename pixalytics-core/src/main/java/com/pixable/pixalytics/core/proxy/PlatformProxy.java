package com.pixable.pixalytics.core.proxy;

import android.content.Context;

import com.pixable.pixalytics.core.Event;
import com.pixable.pixalytics.core.Screen;

import java.util.Map;

public interface PlatformProxy {

    /**
     * Platform initialization
     *
     * @param context
     */
    void onApplicationCreate(Context context);

    /**
     * @return true/false if platform support sessions
     */
    boolean supportsSession();

    /**
     * Platform session opening
     *
     * @param context
     */
    void onSessionStart(Context context);

    /**
     * Platform session closing
     *
     * @param context
     */
    void onSessionFinish(Context context);

    /**
     * Platform Global properties
     *
     * @param commonProperties
     */
    void addCommonProperties(Map<String, String> commonProperties);

    /**
     * Clear Common properties
     */
    void clearCommonProperties();

    /**
     * Platform Tracking event
     *
     * @param event
     */
    void trackEvent(Event event);

    /**
     * @return true/false if platform support screens
     */
    boolean supportsScreens();

    /**
     * Platform Tracking Screen
     *
     * @param screen
     */
    void trackScreen(Screen screen);

    /**
     * Flush logged events and screens
     */
    void flush();

    /**
     * Set Identifier
     * @param identifier
     */
    void setIdentifier(String identifier);

    /**
     * Get Identifier
     */
    String getIdentifier();
}
