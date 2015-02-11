package com.pixable.trackingwrap.platform;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.pixable.trackingwrap.Event;

import org.json.JSONObject;

import java.util.Map;

class MixpanelProxy implements PlatformProxy {
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
    public void onActivityStart(Context context) {
        //throw new UnsupportedOperationException("Mixpanel does not support session tracking");
        // FIXME: come up with a way for the lib user to mix Flurry and Mixpanel
    }

    @Override
    public void onActivityStop(Context context) {
        //throw new UnsupportedOperationException("Mixpanel does not support session tracking");
        // FIXME: come up with a way for the lib user to mix Flurry and Mixpanel
    }

    @Override
    public void addCommonProperties(Context context, Map<String, String> commonProperties) {
        mixpanelAPI.registerSuperProperties(new JSONObject(commonProperties));
    }

    @Override
    public void trackEvent(Context context, Event event) {
        mixpanelAPI.track(event.getName(), event.getPropertiesAsJson());
    }
}
