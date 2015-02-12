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
    public void onScreenStart(Context context, String screenName) {
        //According to Mixpanel Docs, we don's have a way of tracking Screens and no need to open or close session
    }

    @Override
    public void onScreenStop(Context context) {
        //According to Mixpanel Docs, we don's have a way of tracking Screens and no need to open or close session
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
