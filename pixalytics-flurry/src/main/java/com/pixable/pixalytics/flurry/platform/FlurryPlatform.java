package com.pixable.pixalytics.flurry.platform;

import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.flurry.R;
import com.pixable.pixalytics.flurry.proxy.FlurryProxy;

public class FlurryPlatform extends Platform {

    public FlurryPlatform(String id, Config config) {
        super(id, R.drawable.pixalytics__tracking_toast_flurry, new FlurryProxy(config));
    }
}
