package com.pixable.pixalytics.core.helper;

import android.support.v4.app.FragmentActivity;

import com.pixable.pixalytics.core.TrackingWrap;

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
