package com.pixable.trackingwrap.platform;

import android.content.Context;

import com.pixable.trackingwrap.Event;
import com.pixable.trackingwrap.Screen;

import java.util.Map;

public interface PlatformProxy {

    void onApplicationCreate(Context context);

    void onScreenStart(Context context, Screen screen);

    void onScreenStop(Context context);

    void addCommonProperties(Context context, Map<String, String> commonProperties);

    void trackEvent(Context context, Event event);
}
