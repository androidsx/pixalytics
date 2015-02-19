package com.pixable.trackingwrap.core.platform;

import com.pixable.trackingwrap.core.R;
import com.pixable.trackingwrap.core.proxy.FlurryProxy;

public class FlurryPlatform extends Platform {

    public static String ID = "flurry";

    public FlurryPlatform(Config config) {
        super(R.drawable.tracking_toast_flurry, new FlurryProxy(config));
    }

    @Override
    public String getId() {
        return ID;
    }
}
