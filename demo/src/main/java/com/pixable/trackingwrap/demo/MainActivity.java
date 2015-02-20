package com.pixable.trackingwrap.demo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.pixable.pixalytics.core.Event;
import com.pixable.pixalytics.core.Pixalytics;
import com.pixable.pixalytics.core.Screen;
import com.pixable.pixalytics.core.helper.TrackedActionBarActivity;
import com.pixable.pixalytics.facebook.platform.FacebookPlatform;
import com.pixable.pixalytics.flurry.platform.FlurryPlatform;
import com.pixable.pixalytics.ga.platform.GoogleAnalyticsPlatform;
import com.pixable.pixalytics.mixpanel.platform.MixpanelPlatform;

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
        Pixalytics.get().trackEvent(this,
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
        Pixalytics.get().trackScreen(this, screen);
    }
}
