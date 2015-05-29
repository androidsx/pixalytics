package com.pixable.pixalytics.google_analytics.platform;

import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.google_analytics.proxy.GoogleAnalyticsProxy;
import com.pixable.pixalytics.google_analytics.R;

import java.util.Map;

public class GoogleAnalyticsPlatform extends Platform {

    public GoogleAnalyticsPlatform(String id, Config config) {
        super(id, R.drawable.pixalytics__tracking_toast_google_analytics, new GoogleAnalyticsProxy(config));
    }

    /**
     * Configuration parameters for a tracking platform. For instance, a project in your Google Analytics
     * account.
     * This configuration consists in:
     * - appKey: Google Analytics Project Id (like 'UA-XXXXXXXX-1')
     * - dimensionsMapping: Mapping of dimensions defined in Google Analytics Admin page to match Properties name and Dimensions Id
     *   Example: ({'view', 1},{'user', 2}...)
     * - metricsMapping: Mapping of metrics defined in Google Analytics Admin page to match Properties name and Metrics Id
     */
    public static class Config extends Platform.Config {
        Map<String, Integer> dimensionsMapping;
        Map<String, Integer>  metricsMapping;

        public Config(String appKey, Map<String, Integer> dimensionsMapping, Map<String, Integer> metricsMapping) {
            super(appKey);
            this.dimensionsMapping = dimensionsMapping;
            this.metricsMapping = metricsMapping;
        }

        public Map<String, Integer> getDimensionsMapping() {
            return dimensionsMapping;
        }

        public Map<String, Integer> getMetricsMapping() {
            return metricsMapping;
        }
    }
}
