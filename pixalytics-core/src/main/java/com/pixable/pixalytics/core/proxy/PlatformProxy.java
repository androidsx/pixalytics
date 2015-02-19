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
     * @param context
     */
    void addCommonProperties(Context context, Map<String, String> commonProperties);

    /**
     * Platform Tracking event
     *
     * @param context
     */
    void trackEvent(Context context, Event event);

    /**
     * @return true/false if platform support screens
     */
    boolean supportsScreens();

    /**
     * Platform Tracking Screen
     *
     * @param context
     */
    void trackScreen(Context context, Screen screen);
}
