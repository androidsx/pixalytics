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
     * Platform session opening
     * @param context Activity context (not Application)
     */
    void onSessionStart(Context context);

    /**
     * Platform session closing
     * @param context Activity context (not Application)
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
