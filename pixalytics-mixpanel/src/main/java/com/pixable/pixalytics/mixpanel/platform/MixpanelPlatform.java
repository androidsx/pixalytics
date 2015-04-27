package com.pixable.pixalytics.mixpanel.platform;

import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.mixpanel.R;
import com.pixable.pixalytics.mixpanel.proxy.MixpanelProxy;

public class MixpanelPlatform extends Platform {

    public static String ID = "mixpanel";

    public MixpanelPlatform(Config config) {
        super(R.drawable.pixalytics__tracking_toast_mixpanel, new MixpanelProxy(config));
    }

    @Override
    public String getId() {
        return ID;
    }
}
