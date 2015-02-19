package com.pixable.trackingwrap.core.platform;

import com.pixable.trackingwrap.core.proxy.PlatformProxy;

public abstract class Platform {

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

    private PlatformProxy proxy;
    private int iconId;

    public Platform(int iconId, PlatformProxy platformProxy) {
        this.iconId = iconId;
        this.proxy = platformProxy;
    }

    public abstract String getId();

    public int getIconId() {
        return iconId;
    }

    public PlatformProxy getProxy() {
        return proxy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Platform platform = (Platform) o;

        if (getId() != platform.getId()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return getId();
    }
}
