package com.pixable.trackingwrap.platform;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.pixable.trackingwrap.Event;
import com.pixable.trackingwrap.Screen;

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
    public void onSessionStart(Context context) {
        //No need to open session in Mixpanel
    }

    @Override
    public void onSessionFinish(Context context) {
        //No need to close session in Mixpanel
    }

    @Override
    public void addCommonProperties(Context context, Map<String, String> commonProperties) {
        mixpanelAPI.registerSuperProperties(new JSONObject(commonProperties));
    }

    @Override
    public void trackEvent(Context context, Event event) {
        mixpanelAPI.track(event.getName(), event.getPropertiesAsJson());
    }

    @Override
    public void trackScreen(Context context, Screen screen) {
        //No Screens in Mixpanel
    }
}
