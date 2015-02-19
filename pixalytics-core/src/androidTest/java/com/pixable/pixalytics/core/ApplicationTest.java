package com.pixable.trackingwrap.core;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.pixable.trackingwrap.core.platform.Platform;

public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        TrackingWrap.INSTANCE = null;
    }

    public void testSingletonMustBeInitializedExplicitly() {
        try {
            TrackingWrap.get();
            fail("Should have explicitly created the instance before");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testSingletonCantBeInitializedTwice() {
        TrackingWrap.createInstance(
                new TrackingConfig.Builder()
                        .addPlatform(new Platform(Platform.Id.FLURRY, new Platform.Config("key")))
                        .build());
        try {
            TrackingWrap.createInstance(
                    new TrackingConfig.Builder()
                            .addPlatform(new Platform(Platform.Id.MIXPANEL, new Platform.Config("key")))
                            .build());

            fail("The singleton instance was already created");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testAtLeastOnePlatformRequired() {
        try {
            TrackingWrap.createInstance(new TrackingConfig.Builder().build());
            fail("Should have provided at least one platform");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testAppMustBeInitialized() {
        TrackingWrap.createInstance(
                new TrackingConfig.Builder()
                        .addPlatform(new Platform(Platform.Id.FLURRY, new Platform.Config("key")))
                        .build());

        try {
            TrackingWrap.get().trackEvent(
                    getContext(),
                    new Event.Builder().name("share").build(),
                    Platform.Id.FLURRY);
            fail("Expected to fail: didn't initialized the application");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    public void testEveryPlatformMustHaveBeenProvidedAtInitTime() {
        TrackingWrap.createInstance(
                new TrackingConfig.Builder()
                        .addPlatform(new Platform(Platform.Id.FLURRY, new Platform.Config("key")))
                        .build());
        TrackingWrap.get().onApplicationCreate(getContext());

        try {
            TrackingWrap.get().trackEvent(
                    getContext(),
                    new Event.Builder().name("share").build(),
                    Platform.Id.MIXPANEL);

            fail("Expected to fail: this platform is not initialized");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
/*
    // TODO: not really testing anything yet
    public void testApplicationCreationIsTracked() {
        TrackingWrap.createInstance(
                new TrackingConfig.Builder()
                        .addPlatform(new Platform(Platform.Id.FLURRY, new Platform.Config("key")))
                        .build());
        TrackingWrap.get().onApplicationCreate(getContext());

        TrackingWrap.get().trackEvent(
                getContext(),
                new TrackingEvent.Builder().name("share").build(),
                Platform.Id.FLURRY);

        // Well, nothing fails, nothing exploded. Not a bad start
    }*/

    /*
    public void testEventWithProperties() {
                TrackingWrap.createInstance(
                new TrackingConfiguration.Builder()
                        .addPlatform(Platform.FLURRY, new PlatformConfig("key"))
                        .build());


        TrackingWrap.get().trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .name("share")
                        .property("articleId", String.valueOf(15))
                        .property("screen", "full-view")
                        .build(),
                Platform.MIXPANEL);
    }

    public void testMultiplePlatforms() {
                TrackingWrap.createInstance(
                new TrackingConfiguration.Builder()
                        .addPlatform(Platform.FLURRY, new PlatformConfig("key"))
                        .build());


        TrackingWrap.get().trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .name("share")
                        .property("articleId", String.valueOf(15))
                        .property("screen", "full-view")
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

        TrackingWrap.get().trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .name("completed tutorial")
                        .build(),
                Platform.MIXPANEL);

        TrackingWrap.get().trackEvent(
                getContext(),
                new TrackingEvent.Builder()
                        .name("share")
                        .property("articleId", String.valueOf(15))
                        .property("screen", "full-view")
                        .build(),
                Platform.FLURRY,
                Platform.MIXPANEL);
    }*/
}
