package com.pixable.pixalytics.flurry.platform;

import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.flurry.R;
import com.pixable.pixalytics.flurry.proxy.FlurryProxy;

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
