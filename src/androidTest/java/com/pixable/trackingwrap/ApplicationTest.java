package com.pixable.trackingwrap;

import android.app.Application;
import android.test.ApplicationTestCase;

public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        TrackingWrap.INSTANCE = null;
    }

    public void testSingletonMustBeInitializedExplicitely() {
        try {
            TrackingWrap.getInstance();
            fail("Should have explicitly created the instance before");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testSingletonCantBeInitializedTwice() {
        TrackingWrap.createInstance(
                new TrackingConfiguration.Builder()
                        .addPlatform(Platform.FLURRY, new PlatformConfig("key"))
                        .build());
        try {
            TrackingWrap.createInstance(
                    new TrackingConfiguration.Builder()
                            .addPlatform(Platform.MIXPANEL, new PlatformConfig("key"))
                            .build());

            fail("The singleton instance was already created");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testAtLeastOnePlatformRequired() {
        try {
            TrackingWrap.createInstance(new TrackingConfiguration.Builder().build());
            fail("Should have provided at least one platform");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testAppMustBeInitialized() {
        TrackingWrap.createInstance(
                new TrackingConfiguration.Builder()
                        .addPlatform(Platform.FLURRY, new PlatformConfig("key"))
                        .build());

        try {
            TrackingWrap.getInstance().trackEvent(
                    getContext(),
                    new TrackingEvent.Builder().withName("share").build(),
                    Platform.FLURRY);
            fail("Expected to fail: didn't initialized the application");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testEveryPlatformMustHaveBeenProvidedAtInitTime() {
        TrackingWrap.createInstance(
                new TrackingConfiguration.Builder()
                        .addPlatform(Platform.FLURRY, new PlatformConfig("key"))
                        .build());
        TrackingWrap.getInstance().onApplicationCreate(getContext());

        try {
            TrackingWrap.getInstance().trackEvent(
                    getContext(),
                    new TrackingEvent.Builder().withName("share").build(),
                    Platform.MIXPANEL);

            fail("Expected to fail: this platform is not initialized");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /*
    public void testSimpleEventIsTracked() {
        TrackingWrap.createInstance(
                new TrackingConfiguration.Builder()
                        .addPlatform(Platform.FLURRY, new PlatformConfig("key"))
                        .build());

        TrackingWrap.getInstance().onApplicationCreate(getContext());
        TrackingWrap.getInstance().trackEvent(
                getContext(),
                new TrackingEvent.Builder().withName("share").build(),
                Platform.FLURRY);

        // Well, nothing fails, nothing exploded. Not a bad start
    }


    public void testEventWithProperties() {
                TrackingWrap.createInstance(
                new TrackingConfiguration.Builder()
                        .addPlatform(Platform.FLURRY, new PlatformConfig("key"))
                        .build());


        TrackingWrap.getInstance().trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .withName("share")
                        .addProperty("articleId", String.valueOf(15))
                        .addProperty("screen", "full-view")
                        .build(),
                Platform.MIXPANEL);
    }

    public void testMultiplePlatforms() {
                TrackingWrap.createInstance(
                new TrackingConfiguration.Builder()
                        .addPlatform(Platform.FLURRY, new PlatformConfig("key"))
                        .build());


        TrackingWrap.getInstance().trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .withName("share")
                        .addProperty("articleId", String.valueOf(15))
                        .addProperty("screen", "full-view")
                        .build(),
                Platform.FLURRY,
                Platform.MIXPANEL);
    }

    public void testDebugOutput() {
        TrackingWrap.initialize(
                new TrackingConfiguration.Builder()
                        .addDebugPrint(TrackingConfiguration.DebugPrint.LOGCAT)
                        .addDebugPrint(TrackingConfiguration.DebugPrint.TOAST)
                        .build());

        TrackingWrap.getInstance().trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .withName("completed tutorial")
                        .build(),
                Platform.MIXPANEL);

        TrackingWrap.getInstance().trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .withName("share")
                        .addProperty("articleId", String.valueOf(15))
                        .addProperty("screen", "full-view")
                        .build(),
                Platform.FLURRY,
                Platform.MIXPANEL);
    }*/
}
