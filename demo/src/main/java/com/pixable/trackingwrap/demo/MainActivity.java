package com.pixable.trackingwrap.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;

import com.pixable.pixalytics.core.Config;
import com.pixable.pixalytics.core.Event;
import com.pixable.pixalytics.core.Pixalytics;
import com.pixable.pixalytics.core.Screen;
import com.pixable.pixalytics.core.trace.ToastTraceProxy;
import com.pixable.pixalytics.core.trace.TraceProxy;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private static Map<String, Object> commonProperties = new HashMap<>();
    static {
        commonProperties.put("Property1", "Value1");
        commonProperties.put("Common Key", "Value2");
    }

    private TraceProxy toastTrace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toastTrace = new ToastTraceProxy(this, Constants.Traces.TOASTS.name());
        trackScreen();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Track the session start on Flurry
        Pixalytics.get().onSessionStart(this, Constants.PlatformIds.FLURRY.name());

        //Add some commonProperties
        Pixalytics.get().addCommonProperties(commonProperties, Constants.PlatformIds.MIXPANEL.name(), Constants.PlatformIds.GOOGLE_ANALYTICS.name());
    }

    @Override
    public void onStop() {
        super.onStop();
        Pixalytics.get().onSessionFinish(this, Constants.PlatformIds.FLURRY.name());
    }

    public void onFlurryClick(View view) {
        Pixalytics.get().trackEvent(new Event.Builder().name("Foo").build(), Constants.PlatformIds.FLURRY.name());
    }

    public void onMixpanelClick(View view) {
        Pixalytics.get().trackEvent(
                new Event.Builder().name("Foo")
                        .property("Key1", "Value1")
                        .build(),
                Constants.PlatformIds.MIXPANEL.name());
    }

    public void onGoogleAnalyticsClick(View view) {
        Pixalytics.get().trackEvent(
                new Event.Builder().name("Foo")
                        .property("Key1", "Value1")
                        .property("Key2", "Value2")
                        .property("Metric1", "100")
                        .build(),
                Constants.PlatformIds.GOOGLE_ANALYTICS.name());
    }

    public void onFacebookClick(View view) {
        Pixalytics.get().trackEvent(
                new Event.Builder().name("Foo")
                        .property("Key1", "Value1")
                        .property("Key2", "Value2")
                        .property("Key3", "Value3")
                        .build(),
                Constants.PlatformIds.FACEBOOK.name());
    }

    public void onAllPlatformsClick(View view) {
        Pixalytics.get().trackEvent(
                new Event.Builder().name("Bar")
                        .property("Key1", "Value1")
                        .property("Key2", "Value2")
                        .property("Key3", "Value3")
                        .property("Key4", "Value4")
                        .property("Key5", "Value5")
                        .build(),
                Constants.PlatformIds.FLURRY.name(), Constants.PlatformIds.MIXPANEL.name(), Constants.PlatformIds.GOOGLE_ANALYTICS.name(), Constants.PlatformIds.FACEBOOK.name());
    }

    private void trackScreen() {
        Screen screen = new Screen.Builder().name("Main")
                .property("Key1", "Value1")
                .build();
        Pixalytics.get().trackScreen(this, screen, Constants.PlatformIds.GOOGLE_ANALYTICS.name());
    }

    public void onEnableDisableToasts(View view) {
        final CheckBox checkBox = (CheckBox) view;
        final Config.Builder newConfigBuilder = new Config.Builder(Pixalytics.get().getConfig());
        if (checkBox.isChecked()) {
            newConfigBuilder.addTrace(toastTrace);
            checkBox.setText(R.string.checkbox_toasts_enabled);
        } else {
            newConfigBuilder.removeTrace(toastTrace);
            checkBox.setText(R.string.checkbox_toasts_disabled);
        }
        Pixalytics.get().updateConfiguration(newConfigBuilder.build());
    }
}
