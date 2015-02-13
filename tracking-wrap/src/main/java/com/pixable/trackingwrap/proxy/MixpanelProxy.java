package com.pixable.trackingwrap.proxy;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.pixable.trackingwrap.Event;
import com.pixable.trackingwrap.platform.Platform;

import org.json.JSONObject;

import java.util.Map;

public class MixpanelProxy extends PlatformProxy {
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
    public void addCommonProperties(Context context, Map<String, String> commonProperties) {
        mixpanelAPI.registerSuperProperties(new JSONObject(commonProperties));
    }

    @Override
    public void trackEvent(Context context, Event event) {
        mixpanelAPI.track(event.getName(), event.getPropertiesAsJson());
    }
}
