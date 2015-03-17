package com.pixable.pixalytics.mixpanel.proxy;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.pixable.pixalytics.core.Event;
import com.pixable.pixalytics.core.Screen;
import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.core.proxy.PlatformProxy;

import org.json.JSONObject;

import java.util.Map;

public class MixpanelProxy implements PlatformProxy {
    private final Platform.Config config;

    private MixpanelAPI mixpanelAPI;

    public MixpanelProxy(Platform.Config config) {
        this.config = config;
    }

    @Override
    public void onApplicationCreate(Context context) {
        mixpanelAPI = MixpanelAPI.getInstance(context, config.getAppKey());
    }

    @Override
    public boolean supportsSession() {
        return false;
    }

    @Override
    public void onSessionStart(Context context) {
        throw new UnsupportedOperationException("Mixpanel does not support session tracking");
    }

    @Override
    public void onSessionFinish(Context context) {
        throw new UnsupportedOperationException("Mixpanel does not support session tracking");
    }

    @Override
    public void addCommonProperties(Map<String, String> commonProperties) {
        mixpanelAPI.registerSuperProperties(new JSONObject(commonProperties));
    }

    @Override
    public void clearCommonProperties() {
        mixpanelAPI.clearSuperProperties();
    }

    @Override
    public void trackEvent(Event event) {
        mixpanelAPI.track(event.getName(), event.getPropertiesAsJson());
    }

    @Override
    public boolean supportsScreens() {
        return false;
    }

    @Override
    public void trackScreen(Screen screen) {
        throw new UnsupportedOperationException("Mixpanel does not support Screen tracking");
    }

    @Override
    public void flush() {
        mixpanelAPI.flush();
    }

    @Override
    public void setIdentifier(String identifier) {
        mixpanelAPI.identify(identifier);
    }

    @Override
    public String getIdentifier() {
        return mixpanelAPI.getDistinctId();
    }
}
