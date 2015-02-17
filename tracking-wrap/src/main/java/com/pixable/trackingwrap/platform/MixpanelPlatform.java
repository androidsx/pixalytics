package com.pixable.trackingwrap.platform;

import com.pixable.trackingwrap.proxy.MixpanelProxy;

public class MixpanelPlatform extends Platform{

    public MixpanelPlatform(Config config) {
        super(Id.MIXPANEL, new MixpanelProxy(config));
    }
}
