package com.pixable.trackingwrap.demo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.pixable.trackingwrap.Event;
import com.pixable.trackingwrap.Screen;
import com.pixable.trackingwrap.TrackingWrap;
import com.pixable.trackingwrap.helper.TrackedActionBarActivity;
import com.pixable.trackingwrap.platform.Platform;

import java.lang.reflect.Array;
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
        trackEvent(Platform.Id.FLURRY);
    }

    public void onFooMixpanelClick(View view) {
        trackEvent(Platform.Id.MIXPANEL);
    }

    public void onFooGoogleAnalyticsClick(View view) {
        trackEvent(Platform.Id.GOOGLE_ANALYTICS);
    }

    public void onFooFacebookClick(View view) {
        trackEvent(Platform.Id.FACEBOOK);
    }

    private void trackEvent(Platform.Id platformId) {
        Set<Platform> platforms = new HashSet<>();
        platforms.add(TrackingWrap.get().checkPlatformIsConfigured(platformId));
        TrackingWrap.get()
                .trackEvent(this,
                        new Event.Builder().name("foo")
                                .property("view", "main")
                                .property("property2", "value2")
                                .property("property3", "value3")
                                .build(),
                        platforms);
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
