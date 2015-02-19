package com.example.ruben.tracking_wrap_mixpanel.platform;

import com.example.ruben.tracking_wrap_mixpanel.R;
import com.example.ruben.tracking_wrap_mixpanel.proxy.MixpanelProxy;
import com.pixable.trackingwrap.core.platform.Platform;

public class MixpanelPlatform extends Platform {

    public static String ID = "mixpanel";

    public MixpanelPlatform(Config config) {
        super(R.drawable.tracking_toast_mixpanel, new MixpanelProxy(config));
    }

    @Override
    public String getId() {
        return ID;
    }
}
