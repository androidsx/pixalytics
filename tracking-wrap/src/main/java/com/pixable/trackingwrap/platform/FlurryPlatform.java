package com.pixable.trackingwrap.platform;

import com.pixable.trackingwrap.proxy.FlurryProxy;

public class FlurryPlatform extends Platform{

    public FlurryPlatform(Config config) {
        super(Id.FLURRY,  new FlurryProxy(config));
    }
}
