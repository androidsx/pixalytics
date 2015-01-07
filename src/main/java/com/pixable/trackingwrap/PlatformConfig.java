package com.pixable.trackingwrap;

/**
 * Configuration parameters for a tracking platform. For instance, a project in your Mixpanel
 * account.
 */
public class PlatformConfig {
    private final String appKey;

    /**
     * @param appKey app key, typically a long string that is unique for your app in a tracking platform provided
     */
    public PlatformConfig(String appKey) {
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

        PlatformConfig that = (PlatformConfig) o;

        if (appKey != that.appKey) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return appKey.hashCode();
    }
}
