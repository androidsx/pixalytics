package com.pixable.trackingwrap;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * Not real tests yet.
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testSingleDestination() {
        TrackingWrap wrap = new TrackingWrap(new TrackingConfiguration.Builder().build());

        wrap.trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .withName("share")
                        .addProperty("articleId", String.valueOf(15))
                        .addProperty("screen", "full-view")
                        .build(),
                TrackingDestination.Platform.MIXPANEL);
    }

    public void testMultipleDestination() {
        TrackingWrap wrap = new TrackingWrap(new TrackingConfiguration.Builder().build());

        wrap.trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .withName("share")
                        .addProperty("articleId", String.valueOf(15))
                        .addProperty("screen", "full-view")
                        .build(),
                TrackingDestination.Platform.GOOGLE_ANALYTICS,
                TrackingDestination.Platform.MIXPANEL);
    }

    public void testDebugOutput() {
        TrackingWrap wrap = new TrackingWrap(
                new TrackingConfiguration.Builder()
                        .addDebugPrint(TrackingConfiguration.DebugPrint.LOGCAT)
                        .addDebugPrint(TrackingConfiguration.DebugPrint.TOAST)
                        .build());

        wrap.trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .withName("completed tutorial")
                        .build(),
                TrackingDestination.Platform.MIXPANEL);

        wrap.trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .withName("share")
                        .addProperty("articleId", String.valueOf(15))
                        .addProperty("screen", "full-view")
                        .build(),
                TrackingDestination.Platform.GOOGLE_ANALYTICS,
                TrackingDestination.Platform.MIXPANEL);
    }
}
