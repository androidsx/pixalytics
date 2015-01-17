package com.pixable.trackingwrap.demo;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.pixable.trackingwrap.TrackingEvent;
import com.pixable.trackingwrap.TrackingWrap;
import com.pixable.trackingwrap.platform.Platform;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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

    public void onFooClick(View view) {
        TrackingWrap.getInstance()
                .trackEvent(this,
                        new TrackingEvent.Builder().withName("foo").addProperty("view", "main").build(),
                        Platform.Id.FLURRY);
        new AlertDialog.Builder(this)
                .setMessage(R.string.foo_dialog_message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
