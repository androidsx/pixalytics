package com.pixable.trackingwrap.demo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.example.ruben.tracking_wrap_mixpanel.platform.MixpanelPlatform;
import com.pixable.trackingwrap.core.Event;
import com.pixable.trackingwrap.core.Screen;
import com.pixable.trackingwrap.core.TrackingWrap;
import com.pixable.trackingwrap.core.helper.TrackedActionBarActivity;
import com.pixable.trackingwrap.core.platform.FacebookPlatform;
import com.pixable.trackingwrap.core.platform.FlurryPlatform;
import com.pixable.trackingwrap.core.platform.GoogleAnalyticsPlatform;
import com.pixable.trackingwrap.core.platform.Platform;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends TrackedActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trackScreen();
    }

    public void onFooFlurryClick(View view) {
        trackEvent(FlurryPlatform.ID);
    }

    public void onFooMixpanelClick(View view) {
        trackEvent(MixpanelPlatform.ID);
    }

    public void onFooGoogleAnalyticsClick(View view) {
        trackEvent(GoogleAnalyticsPlatform.ID);
    }

    public void onFooFacebookClick(View view) {
        trackEvent(FacebookPlatform.ID);
    }

    private void trackEvent(String platformId) {
        Set<String> platformIds = new HashSet<>();
        platformIds.add(platformId);
        TrackingWrap.get()
                .trackEvent(this,
                        new Event.Builder().name("foo")
                                .property("view", "main")
                                .property("property2", "value2")
                                .property("property3", "value3")
                                .build(),
                        platformIds);
        new AlertDialog.Builder(this)
                .setMessage(R.string.foo_dialog_message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void trackScreen() {
        Screen screen = new Screen.Builder().name("Main")
                .property("1", "Value1")
                .build();
        TrackingWrap.get().trackScreen(this, screen);
    }
}
