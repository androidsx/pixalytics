package com.pixable.trackingwrap.helper;

import android.support.v4.app.FragmentActivity;

import com.pixable.trackingwrap.TrackingWrap;

public class TrackedFragmentActivity extends FragmentActivity {

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
