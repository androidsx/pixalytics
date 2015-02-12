package com.pixable.trackingwrap.helper;

import android.support.v7.app.ActionBarActivity;

import com.pixable.trackingwrap.Screen;
import com.pixable.trackingwrap.TrackingWrap;

public abstract class TrackedActionBarActivity extends ActionBarActivity {

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
