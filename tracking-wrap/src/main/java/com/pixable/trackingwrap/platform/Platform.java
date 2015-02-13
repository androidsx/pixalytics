package com.pixable.trackingwrap.platform;

import com.pixable.trackingwrap.proxy.FlurryProxy;
import com.pixable.trackingwrap.proxy.GoogleAnalyticsProxy;
import com.pixable.trackingwrap.proxy.MixpanelProxy;
import com.pixable.trackingwrap.proxy.PlatformProxy;

public class Platform {

    /**
    * Tracking platform to which events can be sent. Also known as analytics provider.
    */
    public static enum Id {
        MIXPANEL,
        FLURRY,
        GOOGLE_ANALYTICS;
    }

    /**
     * Configuration parameters for a tracking platform. For instance, a project in your Mixpanel
     * account.
     */
    public static class Config {
        private final String appKey;

        /**
         * @param appKey app key, typically a long string that is unique for your app in a tracking platform provided
         */
        public Config(String appKey) {
            this.appKey = appKey;
        }

        public String getAppKey() {
            return appKey;
        }

        @Override
        public String toString() {
            return appKey;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Config that = (Config) o;

            if (appKey != that.appKey) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return appKey.hashCode();
        }
    }

    private final Id id;
    private PlatformProxy proxy;

    public Platform(Id id, PlatformProxy platformProxy) {
        this.id = id;
        this.proxy = platformProxy;
    }

    public Id getId() {
        return id;
    }

    public PlatformProxy getProxy() {
        return proxy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Platform platform = (Platform) o;

        if (id != platform.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
