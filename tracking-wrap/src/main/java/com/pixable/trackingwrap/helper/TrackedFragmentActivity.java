package com.pixable.trackingwrap.helper;

import android.support.v4.app.FragmentActivity;

import com.pixable.trackingwrap.Screen;
import com.pixable.trackingwrap.TrackingWrap;

public abstract class TrackedFragmentActivity extends FragmentActivity {

    @Override
    protected void onStart() {
        super.onStart();

        TrackingWrap.get().onScreenStart(this, getScreen());
    }

    @Override
    protected void onStop() {
        super.onStop();

        TrackingWrap.get().onScreenStop(this);
    }

    protected abstract Screen getScreen();
}
