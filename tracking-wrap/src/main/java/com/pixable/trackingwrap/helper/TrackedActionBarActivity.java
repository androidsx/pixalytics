package com.pixable.trackingwrap.helper;

import android.support.v7.app.ActionBarActivity;

import com.pixable.trackingwrap.TrackingWrap;

public class TrackedActionBarActivity extends ActionBarActivity {

    @Override
    protected void onStart() {
        super.onStart();

        TrackingWrap.getInstance().onActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        TrackingWrap.getInstance().onActivityStop(this);
    }
}
