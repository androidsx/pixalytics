package com.pixable.trackingwrap.proxy;

import android.content.Context;
import android.os.Bundle;

import com.facebook.AppEventsConstants;
import com.facebook.AppEventsLogger;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.pixable.trackingwrap.Event;
import com.pixable.trackingwrap.Screen;
import com.pixable.trackingwrap.platform.FacebookPlatform;
import com.pixable.trackingwrap.platform.Platform;

import org.json.JSONObject;

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
    public boolean supportsSession() {
        return false;
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
    public void addCommonProperties(Context context, Map<String, String> commonProperties) {
        throw new UnsupportedOperationException("Facebook does not support common properties");
    }

    @Override
    public void trackEvent(Context context, Event event) {
        if(this.config.getEventMapping().containsKey(event.getName())) {
            Bundle properties = getProperties(event.getProperties());
            facebookLogger.logEvent(event.getName(), properties);
        }
    }

    private Bundle getProperties(Map<String, String> propertiesMap) {
        Bundle properties = new Bundle();
        Iterator it = propertiesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            if(config.getPropertiesMapping().containsKey(pairs.getKey())) {
                properties.putString(config.getPropertiesMapping().get(pairs.getKey()), String.valueOf(pairs.getValue()));
            }
            it.remove();
        }
        return properties;
    }

    @Override
    public boolean supportsScreens() {
        return false;
    }

    @Override
    public void trackScreen(Context context, Screen screen) {
        throw new UnsupportedOperationException("Facebook does not support Screen tracking");
    }
}
