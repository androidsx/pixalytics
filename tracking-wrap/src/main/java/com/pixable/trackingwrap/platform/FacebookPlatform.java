package com.pixable.trackingwrap.platform;

import com.pixable.trackingwrap.proxy.FacebookProxy;
import com.pixable.trackingwrap.proxy.MixpanelProxy;

import java.util.Map;

public class FacebookPlatform extends Platform{

    public FacebookPlatform(Config config) {
        super(Id.FACEBOOK, new FacebookProxy(config));
    }

    /**
     * Configuration parameters for a tracking platform. In this case we will need to use
     * event mapping with events described here: https://developers.facebook.com/docs/app-events/android
     */
    public static class Config extends Platform.Config{
        Map<String, String> eventMapping;
        Map<String, String> propertiesMapping;

        /**
         * Constructor
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
