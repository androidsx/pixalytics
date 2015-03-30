package com.pixable.pixalytics.flurry.proxy;

import android.content.Context;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.pixable.pixalytics.core.Event;
import com.pixable.pixalytics.core.Screen;
import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.core.proxy.PlatformProxy;

import java.util.HashMap;
import java.util.Map;

public class FlurryProxy implements PlatformProxy {
    private final Platform.Config config;

    public FlurryProxy(Platform.Config config) {
        this.config = config;
    }

    @Override
    public void onApplicationCreate(Context context) {
        FlurryAgent.init(context, config.getAppKey());
        FlurryAgent.setLogEnabled(true); // Should be false
        FlurryAgent.setLogLevel(Log.INFO); // Not needed, given the previous one
    }

    @Override
    public void onSessionStart(Context context) {
        FlurryAgent.onStartSession(context);
    }

    @Override
    public void onSessionFinish(Context context) {
        FlurryAgent.onEndSession(context);
    }

    @Override
    public void addCommonProperty(String name, Object value) {
        throw new UnsupportedOperationException("Flurry does not support common properties");
    }

    @Override
    public void addCommonProperties(Map<String, Object> commonProperties) {
        throw new UnsupportedOperationException("Flurry does not support common properties");
    }

    @Override
    public void clearCommonProperty(String name) {
        throw new UnsupportedOperationException("Flurry does not support common properties");
    }

    @Override
    public void clearCommonProperties() {
        throw new UnsupportedOperationException("Flurry does not support common properties");
    }

    @Override
    public void trackEvent(Event event) {
        FlurryAgent.logEvent(event.getName(), safeGenericCasting(event.getProperties()));
    }

    @Override
    public void trackScreen(Screen screen) {
        //No Screens in Flurry
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Flurry does not support Flush");
    }

    @Override
    public void setIdentifier(String identifier) {
        throw new UnsupportedOperationException("Flurry does not support Identifier management");
    }

    @Override
    public String getIdentifier() {
        throw new UnsupportedOperationException("Flurry does not support Identifier management");
    }

    private static Map<String, String> safeGenericCasting(Map<String, Object> objectMap) {
        final Map<String, String> stringMap = new HashMap<>();
        for (String key : objectMap.keySet()) {
            Object value = objectMap.get(key);

            stringMap.put(key, String.valueOf(value));
        }
        return stringMap;
    }
}
