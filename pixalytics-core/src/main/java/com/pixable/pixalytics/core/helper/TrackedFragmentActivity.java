package com.pixable.pixalytics.core.helper;

import android.support.v4.app.FragmentActivity;

import com.pixable.pixalytics.core.Pixalytics;

public abstract class TrackedFragmentActivity extends FragmentActivity {

    @Override
    protected void onStart() {
        super.onStart();

        Pixalytics.get().onSessionStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Pixalytics.get().onSessionFinish(this);
    }
}
