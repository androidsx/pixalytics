package com.pixable.pixalytics.core.helper;

import android.app.Activity;

import com.pixable.pixalytics.core.Pixalytics;
import com.pixable.pixalytics.core.Screen;

public abstract class TrackedActivity extends Activity {

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

    protected abstract Screen getScreen();
}
