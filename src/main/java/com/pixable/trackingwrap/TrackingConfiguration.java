package com.pixable.trackingwrap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TrackingConfiguration {
    private final Map<Platform, PlatformConfig> platforms;
    private final Set<DebugPrint> debugPrints;

    public TrackingConfiguration(Map<Platform, PlatformConfig> platforms, Set<DebugPrint> debugPrints) {
        this.platforms = platforms;
        this.debugPrints = debugPrints;
    }

    public Map<Platform, PlatformConfig> getPlatforms() {
        return platforms;
    }

    public Set<DebugPrint> getDebugPrints() {
        return debugPrints;
    }

    public enum DebugPrint {
        LOGCAT,
        TOAST;
    }

    public static class Builder {
        private Map<Platform, PlatformConfig> platforms = new HashMap<>();
        private Set<DebugPrint> debugPrints = new HashSet<>();

        /**
         * Only one configuration is allowed for every provider. That is, you cannot send events to two
         * different Mixpanel accounts, for instance. Implementing this may be a little tricky in the
         * library, and a little cumbersome for the clients.
         */
        public Builder addPlatform(Platform platform, PlatformConfig platformConfig) {
            if (platforms.containsKey(platform)) {
                throw new IllegalArgumentException("Only one configuration is allowed per platform");
            }
            platforms.put(platform, platformConfig);
            return this;
        }

        public Builder addDebugPrint(DebugPrint debugPrint) {
            debugPrints.add(debugPrint);
            return this;
        }

        public TrackingConfiguration build() {
            if (platforms.isEmpty()) {
                throw new IllegalStateException("You should configure at least one platform");
            }

            return new TrackingConfiguration(platforms, debugPrints);
        }
    }
}
