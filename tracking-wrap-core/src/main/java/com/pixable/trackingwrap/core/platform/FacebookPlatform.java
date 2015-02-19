package com.pixable.trackingwrap.core.platform;

import com.pixable.trackingwrap.core.R;
import com.pixable.trackingwrap.core.proxy.FacebookProxy;

import java.util.Map;

public class FacebookPlatform extends Platform {

    public static String ID = "facebook";

    public FacebookPlatform(Config config) {
        super(R.drawable.tracking_toast_facebook, new FacebookProxy(config));
    }

    @Override
    public String getId() {
        return ID;
    }

    /**
     * Configuration parameters for a tracking platform. In this case we will need to use
     * event mapping with events described here: https://developers.facebook.com/docs/app-events/android
     */
    public static class Config extends Platform.Config {
        Map<String, String> eventMapping;
        Map<String, String> propertiesMapping;

        /**
         * Constructor
         *
         * @param appKey
         * @param eventMapping
         * @param propertiesMapping
         */
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
