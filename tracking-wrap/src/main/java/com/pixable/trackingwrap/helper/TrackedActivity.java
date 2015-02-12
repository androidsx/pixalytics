package com.pixable.trackingwrap.helper;

import android.app.Activity;

import com.pixable.trackingwrap.TrackingWrap;

public class TrackedActivity extends Activity {

    @Override
    protected void onStart() {
        super.onStart();

        TrackingWrap.get().onScreenStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        TrackingWrap.get().onScreenStop(this);
    }
}
