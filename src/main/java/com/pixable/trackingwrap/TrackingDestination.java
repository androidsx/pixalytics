package com.pixable.trackingwrap;

/**
 * Destination for an event, that is, a project inside your analytics platform of choice.
 */
public class TrackingDestination {

    public enum Platform {
        MIXPANEL,
        FLURRY;
    }

    private final Platform platform;
    private final String appKey;

    /**
     * @param platform an analytics platform
     * @param appKey app key, typically a long string that is unique for your app in the provided analytics platform
     */
    public TrackingDestination(Platform platform, String appKey) {
        this.platform = platform;
        this.appKey = appKey;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getAppKey() {
        return appKey;
    }
}
