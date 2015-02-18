package com.pixable.trackingwrap.core.helper;

import android.app.Activity;

import com.pixable.trackingwrap.core.Screen;
import com.pixable.trackingwrap.core.TrackingWrap;

public abstract class TrackedActivity extends Activity {

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

    protected abstract Screen getScreen();
}
