package com.pixable.trackingwrap.core.platform;

import com.pixable.trackingwrap.core.R;
import com.pixable.trackingwrap.core.proxy.GoogleAnalyticsProxy;

import java.util.Map;

public class GoogleAnalyticsPlatform extends Platform {

    public GoogleAnalyticsPlatform(Config config) {
        super(Platform.Id.GOOGLE_ANALYTICS, R.drawable.tracking_toast_ga, new GoogleAnalyticsProxy(config));
    }

    /**
     * Configuration parameters for a tracking platform. For instance, a project in your Mixpanel
     * account.
     */
    public static class Config extends Platform.Config {
        Map<String, Integer> parameterMapping;

        /**
         * Constructor
         *
         * @param appKey
         * @param parameterMapping
         */
        public Config(String appKey, Map<String, Integer> parameterMapping) {
            super(appKey);
            this.parameterMapping = parameterMapping;
        }

        public Map<String, Integer> getParameterMapping() {
            return parameterMapping;
        }
    }
}
