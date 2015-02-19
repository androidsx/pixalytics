package com.pixable.pixalytics.ga.platform;

import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.ga.proxy.GoogleAnalyticsProxy;

import java.util.Map;

public class GoogleAnalyticsPlatform extends Platform {

    public static String ID = "ga";

    public GoogleAnalyticsPlatform(Config config) {
        super(R.drawable.tracking_toast_ga, new GoogleAnalyticsProxy(config));
    }

    @Override
    public String getId() {
        return ID;
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
