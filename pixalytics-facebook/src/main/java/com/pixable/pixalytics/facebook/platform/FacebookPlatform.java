package com.pixable.pixalytics.facebook.platform;

import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.facebook.R;
import com.pixable.pixalytics.facebook.proxy.FacebookProxy;

import java.util.Map;

public class FacebookPlatform extends Platform {

    public FacebookPlatform(String id, Config config) {
        super(id, R.drawable.tracking_toast_facebook, new FacebookProxy(config));
    }

    /**
     * Configuration parameters for a tracking platform. In this case we will need to use
     * event mapping with events described here: https://developers.facebook.com/docs/app-events/android
     */
    public static class Config extends Platform.Config {
        Map<String, String> eventMapping;
        Map<String, String> propertiesMapping;

        public Config(String appKey, Map<String, String> eventMapping, Map<String, String> propertiesMapping) {
            super(appKey);
            this.eventMapping = eventMapping;
            this.propertiesMapping = propertiesMapping;
        }

        public Map<String, String> getEventMapping() {
            return eventMapping;
        }

        public Map<String, String> getPropertiesMapping() {
            return propertiesMapping;
        }
    }
}
