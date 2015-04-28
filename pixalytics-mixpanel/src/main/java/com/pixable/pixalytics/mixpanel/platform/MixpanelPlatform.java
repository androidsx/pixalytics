package com.pixable.pixalytics.mixpanel.platform;

import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.mixpanel.R;
import com.pixable.pixalytics.mixpanel.proxy.MixpanelProxy;

public class MixpanelPlatform extends Platform {

    public MixpanelPlatform(String id, Config config) {
        super(id, R.drawable.pixalytics__tracking_toast_mixpanel, new MixpanelProxy(config));
    }
}
