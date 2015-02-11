package com.pixable.trackingwrap.demo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.pixable.trackingwrap.Event;
import com.pixable.trackingwrap.TrackingWrap;
import com.pixable.trackingwrap.helper.TrackedActionBarActivity;
import com.pixable.trackingwrap.platform.Platform;

public class MainActivity extends TrackedActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onFooFlurryClick(View view) {
        trackEvent(Platform.Id.FLURRY);
    }

    public void onFooMixpanelClick(View view) {
        trackEvent(Platform.Id.MIXPANEL);
    }

    private void trackEvent(Platform.Id platformId) {
        TrackingWrap.get()
                .trackEvent(this,
                        new Event.Builder().name("foo")
                                .property("view", "main")
                                .property("property2", "value2")
                                .property("property3", "value3")
                                .build(),
                        platformId);
        new AlertDialog.Builder(this)
                .setMessage(R.string.foo_dialog_message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
