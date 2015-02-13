package com.pixable.trackingwrap.proxy;

import android.content.Context;

import com.pixable.trackingwrap.Event;
import com.pixable.trackingwrap.Screen;

import java.util.Map;

public abstract class PlatformProxy {

    /**
     * Platform should be initialized. Mandatory implementation
     * @param context
     */
    public abstract void onApplicationCreate(Context context);

    /**
     * Platform session opening. Optional implementation
     * @param context
     */
    public void onSessionStart(Context context){}

    /**
     * Platform session closing. Optional implementation
     * @param context
     */
    public void onSessionFinish(Context context){}

    /**
     * Platform Global properties. Optional implementation
     * @param context
     */
    public void addCommonProperties(Context context, Map<String, String> commonProperties){}

    /**
     * Platform Tracking event. Optional implementation
     * @param context
     */
    public void trackEvent(Context context, Event event){}

    /**
     * Platform Tracking Screen. Optional implementation
     * @param context
     */
    public void trackScreen(Context context, Screen screen){}
}
