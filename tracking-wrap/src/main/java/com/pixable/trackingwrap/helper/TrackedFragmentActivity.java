package com.pixable.trackingwrap.helper;

import android.support.v4.app.FragmentActivity;

import com.pixable.trackingwrap.Screen;
import com.pixable.trackingwrap.TrackingWrap;

public abstract class TrackedFragmentActivity extends FragmentActivity {

    @Override
    protected void onStart() {
        super.onStart();

        TrackingWrap.get().onSessionStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        TrackingWrap.get().onSessionFinish(this);
    }
}
