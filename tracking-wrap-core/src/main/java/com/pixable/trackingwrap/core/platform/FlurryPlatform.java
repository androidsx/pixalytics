package com.pixable.trackingwrap.core.platform;

import com.pixable.trackingwrap.core.R;
import com.pixable.trackingwrap.core.proxy.FlurryProxy;

public class FlurryPlatform extends Platform {

    public FlurryPlatform(Config config) {
        super(Id.FLURRY, R.drawable.tracking_toast_flurry, new FlurryProxy(config));
    }
}
