package com.pixable.trackingwrap.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.pixable.pixalytics.core.Event;
import com.pixable.pixalytics.core.Pixalytics;
import com.pixable.pixalytics.core.Screen;
import com.pixable.pixalytics.facebook.platform.FacebookPlatform;
import com.pixable.pixalytics.flurry.platform.FlurryPlatform;
import com.pixable.pixalytics.ga.platform.GoogleAnalyticsPlatform;
import com.pixable.pixalytics.mixpanel.platform.MixpanelPlatform;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trackScreen();
    }

    @Override
    public void onStart() {
        super.onStart();
        Pixalytics.get().onSessionStart(this, FlurryPlatform.ID);
    }

    @Override
    public void onStop() {
        super.onStop();
        Pixalytics.get().onSessionFinish(this, FlurryPlatform.ID);
    }

    public void onFlurryClick(View view) {
        Pixalytics.get().trackEvent(this, new Event.Builder().name("Foo").build(), FlurryPlatform.ID);
    }

    public void onMixpanelClick(View view) {
        Pixalytics.get().trackEvent(this,
                new Event.Builder().name("Foo")
                        .property("Key1", "Value1")
                        .build(),
                MixpanelPlatform.ID);
    }

    public void onGoogleAnalyticsClick(View view) {
        Pixalytics.get().trackEvent(this,
                new Event.Builder().name("Foo")
                        .property("Key1", "Value1")
                        .property("Key2", "Value2")
                        .build(),
                GoogleAnalyticsPlatform.ID);
    }

    public void onFacebookClick(View view) {
        Pixalytics.get().trackEvent(this,
                new Event.Builder().name("Foo")
                        .property("Key1", "Value1")
                        .property("Key2", "Value2")
                        .property("Key3", "Value3")
                        .build(),
                FacebookPlatform.ID);
    }

    public void onAllPlatformsClick(View view) {
        Pixalytics.get().trackEvent(this,
                new Event.Builder().name("Bar")
                        .property("Key1", "Value1")
                        .property("Key2", "Value2")
                        .property("Key3", "Value3")
                        .property("Key4", "Value4")
                        .property("Key5", "Value5")
                        .build(),
                new String[]{FlurryPlatform.ID, MixpanelPlatform.ID, GoogleAnalyticsPlatform.ID, FacebookPlatform.ID});
    }

    private void trackScreen() {
        Screen screen = new Screen.Builder().name("Main")
                .property("Key1", "Value1")
                .build();
        Pixalytics.get().trackScreen(this, screen, GoogleAnalyticsPlatform.ID);
    }
}
