package com.pixable.trackingwrap;

import com.pixable.trackingwrap.platform.Platform;

import java.util.HashSet;
import java.util.Set;

public class TrackingConfig {
    private final Set<Platform> platforms;
    private final Set<Trace> traces;

    public TrackingConfig(Set<Platform> platforms, Set<Trace> traces) {
        this.platforms = platforms;
        this.traces = traces;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public Set<Trace> getTraces() {
        return traces;
    }

    public enum Trace {
        LOGCAT,
        TOAST;
    }

    public static class Builder {
        private Set<Platform> platforms = new HashSet<>();
        private Set<Trace> traces = new HashSet<>();

        /**
         * Only one configuration is allowed for every provider. That is, you cannot send events to two
         * different Mixpanel accounts, for instance. Implementing this may be a little tricky in the
         * library, and a little cumbersome for the clients.
         */
        public Builder addPlatform(Platform platform) {
            if (platforms.contains(platform)) {
                throw new IllegalArgumentException("Only one configuration is allowed per platform");
            }
            platforms.add(platform);
            return this;
        }

        public Builder addDebugPrint(Trace trace) {
            traces.add(trace);
            return this;
        }

        public TrackingConfig build() {
            if (platforms.isEmpty()) {
                throw new IllegalStateException("You should configure at least one platform");
            }

            return new TrackingConfig(platforms, traces);
        }
    }
}
