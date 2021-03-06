package com.pixable.pixalytics.mixpanel.proxy;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.pixable.pixalytics.core.Event;
import com.pixable.pixalytics.core.Screen;
import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.core.proxy.PlatformProxy;

import org.json.JSONObject;

import java.util.HashMap;
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
    public void onSessionStart(Context context) {
        throw new UnsupportedOperationException("Mixpanel does not support session tracking");
    }

    @Override
    public void onSessionFinish(Context context) {
        throw new UnsupportedOperationException("Mixpanel does not support session tracking");
    }

    @Override
    public void addCommonProperty(final String name, @NonNull final Object value) {
        mixpanelAPI.registerSuperProperties(new JSONObject(
                new HashMap<String, Object>() {{ put(name, value); }}));
    }

    @Override
    public void addCommonProperties(Map<String, Object> commonProperties) {
        mixpanelAPI.registerSuperProperties(new JSONObject(commonProperties));
    }

    @Override
    public void clearCommonProperty(String name) {
        mixpanelAPI.unregisterSuperProperty(name);
    }

    @Override
    public void clearCommonProperties() {
        mixpanelAPI.clearSuperProperties();
    }

    @Override
    public void addUserProperty(@NonNull String name, @NonNull Object value) {
        mixpanelAPI.getPeople().set(name, value);
    }

    @Override
    public void trackEvent(Event event) {
        mixpanelAPI.track(event.getName(), event.getPropertiesAsJson());
    }

    @Override
    public void trackScreen(Screen screen) {
        throw new UnsupportedOperationException("Mixpanel does not support Screen tracking");
    }

    @Override
    public void trackSocial(String network, String action, String target) {
        throw new UnsupportedOperationException("Mixpanel does not support Social Interactions tracking");
    }

    @Override
    public void trackRevenue(String product, double revenue) {
        throw new UnsupportedOperationException("Mixpanel does not support revenue tracking");
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
