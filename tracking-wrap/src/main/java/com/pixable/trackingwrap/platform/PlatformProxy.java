package com.pixable.trackingwrap.platform;

import android.content.Context;

import com.pixable.trackingwrap.Event;
import com.pixable.trackingwrap.Screen;

import java.util.Map;

public interface PlatformProxy {

    void onApplicationCreate(Context context);

    void onSessionStart(Context context);

    void onSessionFinish(Context context);

    void addCommonProperties(Context context, Map<String, String> commonProperties);

    void trackEvent(Context context, Event event);

    void trackScreen(Context context, Screen screen);
}
