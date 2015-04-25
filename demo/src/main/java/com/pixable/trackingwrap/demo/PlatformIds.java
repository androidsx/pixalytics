package com.pixable.trackingwrap.demo;

public enum PlatformIds {
    FLURRY("flurry"),
    MIXPANEL("mixpanel"),
    GOOGLE_ANALYTICS("google_analytics"),
    FACEBOOK("facebook");

    private final String id;

    PlatformIds(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
