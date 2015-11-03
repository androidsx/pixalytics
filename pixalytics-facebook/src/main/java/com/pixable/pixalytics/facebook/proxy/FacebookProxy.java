package com.pixable.pixalytics.facebook.proxy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;


import com.facebook.AppEventsLogger;
import com.pixable.pixalytics.core.Event;
import com.pixable.pixalytics.core.Screen;
import com.pixable.pixalytics.core.proxy.PlatformProxy;
import com.pixable.pixalytics.facebook.platform.FacebookPlatform;

import java.util.Iterator;
import java.util.Map;

public class FacebookProxy implements PlatformProxy {
    private final FacebookPlatform.Config config;

    private AppEventsLogger facebookLogger;

    public FacebookProxy(FacebookPlatform.Config config) {
        this.config = config;
    }

    @Override
    public void onApplicationCreate(Context context) {
        facebookLogger = AppEventsLogger.newLogger(context, config.getAppKey());
    }

    @Override
    public void onSessionStart(Context context) {
        throw new UnsupportedOperationException("Facebook does not support session tracking");
    }

    @Override
    public void onSessionFinish(Context context) {
        throw new UnsupportedOperationException("Facebook does not support session tracking");
    }

    @Override
    public void addCommonProperty(String name, @NonNull Object value) {
        throw new UnsupportedOperationException("Facebook does not support common properties");
    }

    @Override
    public void addCommonProperties(Map<String, Object> commonProperties) {
        throw new UnsupportedOperationException("Facebook does not support common properties");
    }

    @Override
    public void clearCommonProperty(String name) {
        throw new UnsupportedOperationException("Facebook does not support common properties");
    }

    @Override
    public void clearCommonProperties() {
        throw new UnsupportedOperationException("Facebook does not support common properties");
    }

    @Override
    public void addUserProperty(@NonNull String name, @NonNull Object value) {
        throw new UnsupportedOperationException("Facebook does not support user properties");
    }

    @Override
    public void trackEvent(Event event) {
        if (this.config.getEventMapping().containsKey(event.getName())) {
            Bundle properties = getProperties(event.getProperties());
            facebookLogger.logEvent(event.getName(), properties);
        }
    }

    private Bundle getProperties(Map<String, Object> propertiesMap) {
        Bundle properties = new Bundle();
        Iterator it = propertiesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            if (config.getPropertiesMapping().containsKey(pairs.getKey())) {
                properties.putString(config.getPropertiesMapping().get(pairs.getKey()), String.valueOf(pairs.getValue()));
            }
            it.remove();
        }
        return properties;
    }

    @Override
    public void trackScreen(Screen screen) {
        throw new UnsupportedOperationException("Facebook does not support Screen tracking");
    }

    @Override
    public void trackSocial(String network, String action, String target) {
        throw new UnsupportedOperationException("Facebook does not support Social Interactions tracking");
    }

    @Override
    public void trackRevenue(String product, double revenue) {
        throw new UnsupportedOperationException("Facebook does not support revenue tracking");
    }

    @Override
    public void flush() {
        facebookLogger.flush();
    }

    @Override
    public void setIdentifier(String identifier) {
        throw new UnsupportedOperationException("Facebook does not support Identifier management");
    }

    @Override
    public String getIdentifier() {
        throw new UnsupportedOperationException("Facebook does not support Identifier management");
    }
}
