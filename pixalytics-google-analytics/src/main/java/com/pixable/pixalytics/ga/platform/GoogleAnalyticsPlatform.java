package com.pixable.pixalytics.ga.platform;

import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.ga.R;
import com.pixable.pixalytics.ga.proxy.GoogleAnalyticsProxy;

import java.util.Map;

public class GoogleAnalyticsPlatform extends Platform {

    public GoogleAnalyticsPlatform(String id, Config config) {
        super(id, R.drawable.pixalytics__tracking_toast_ga, new GoogleAnalyticsProxy(config));
    }

    /**
     * Configuration parameters for a tracking platform. For instance, a project in your Mixpanel
     * account.
     */
    public static class Config extends Platform.Config {
        Map<String, Integer> parameterMapping;

        public Config(String appKey, Map<String, Integer> parameterMapping) {
            super(appKey);
            this.parameterMapping = parameterMapping;
        }

        public Map<String, Integer> getParameterMapping() {
            return parameterMapping;
        }
    }
}
