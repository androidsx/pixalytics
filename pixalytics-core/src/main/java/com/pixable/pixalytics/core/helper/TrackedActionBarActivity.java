package com.pixable.pixalytics.core.helper;

import android.support.v7.app.ActionBarActivity;

import com.pixable.pixalytics.core.Pixalytics;

public abstract class TrackedActionBarActivity extends ActionBarActivity {

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
