package com.pixable.trackingwrap.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;

import com.pixable.pixalytics.core.Config;
import com.pixable.pixalytics.core.Event;
import com.pixable.pixalytics.core.Pixalytics;
import com.pixable.pixalytics.core.Screen;
import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.core.trace.TraceId;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private static Map<String, Object> commonProperties = new HashMap<>();
    static {
        commonProperties.put("Property1", "Value1");
        commonProperties.put("Common Key", null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trackScreen();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Track the session start on Flurry
        Pixalytics.get().onSessionStart(this, PlatformIds.FLURRY.getId());

        // Add a common property for all events sent to Mixpanel and Google Analytics
        Pixalytics.get().addCommonProperty(this, "Common Key", null, PlatformIds.MIXPANEL.getId(), PlatformIds.GOOGLE_ANALYTICS.getId());

        //Add some commonProperties
        Pixalytics.get().addCommonProperties(this, commonProperties, PlatformIds.MIXPANEL.getId(), PlatformIds.GOOGLE_ANALYTICS.getId());
    }

    @Override
    public void onStop() {
        super.onStop();
        Pixalytics.get().onSessionFinish(this, PlatformIds.FLURRY.getId());
    }

    public void onFlurryClick(View view) {
        Pixalytics.get().trackEvent(this, new Event.Builder().name("Foo").build(), PlatformIds.FLURRY.getId());
    }

    public void onMixpanelClick(View view) {
        Pixalytics.get().trackEvent(this,
                new Event.Builder().name("Foo")
                        .property("Key1", "Value1")
                        .build(),
                PlatformIds.MIXPANEL.getId());
    }

    public void onGoogleAnalyticsClick(View view) {
        Pixalytics.get().trackEvent(this,
                new Event.Builder().name("Foo")
                        .property("Key1", "Value1")
                        .property("Key2", "Value2")
                        .property("Metric1", "100")
                        .build(),
                PlatformIds.GOOGLE_ANALYTICS.getId());
    }

    public void onFacebookClick(View view) {
        Pixalytics.get().trackEvent(this,
                new Event.Builder().name("Foo")
                        .property("Key1", "Value1")
                        .property("Key2", "Value2")
                        .property("Key3", "Value3")
                        .build(),
                PlatformIds.FACEBOOK.getId());
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
                PlatformIds.FLURRY.getId(), PlatformIds.MIXPANEL.getId(), PlatformIds.GOOGLE_ANALYTICS.getId(), PlatformIds.FACEBOOK.getId());
    }

    private void trackScreen() {
        Screen screen = new Screen.Builder().name("Main")
                .property("Key1", "Value1")
                .build();
        Pixalytics.get().trackScreen(this, screen, PlatformIds.GOOGLE_ANALYTICS.getId());
    }

    public void onEnableDisableToasts(View view) {
        final CheckBox checkBox = (CheckBox) view;
        final Config.Builder newConfigBuilder = new Config.Builder(Pixalytics.get().getConfig());
        if (checkBox.isChecked()) {
            newConfigBuilder.removeTrace(TraceId.TOAST);
            checkBox.setText(R.string.checkbox_toasts_disabled);
        } else {
            newConfigBuilder.addTrace(TraceId.TOAST);
            checkBox.setText(R.string.checkbox_toasts_enabled);
        }
        Pixalytics.get().updateConfiguration(newConfigBuilder.build());
    }
}
