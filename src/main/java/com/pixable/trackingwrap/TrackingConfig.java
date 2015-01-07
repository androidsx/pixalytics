package com.pixable.trackingwrap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TrackingConfig {
    private final Map<PlatformId, PlatformConfig> platforms;
    private final Set<DebugPrint> debugPrints;

    public TrackingConfig(Map<PlatformId, PlatformConfig> platforms, Set<DebugPrint> debugPrints) {
        this.platforms = platforms;
        this.debugPrints = debugPrints;
    }

    public Map<PlatformId, PlatformConfig> getPlatforms() {
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
        private Map<PlatformId, PlatformConfig> platforms = new HashMap<>();
        private Set<DebugPrint> debugPrints = new HashSet<>();

        /**
         * Only one configuration is allowed for every provider. That is, you cannot send events to two
         * different Mixpanel accounts, for instance. Implementing this may be a little tricky in the
         * library, and a little cumbersome for the clients.
         */
        public Builder addPlatform(PlatformId platformId, PlatformConfig platformConfig) {
            if (platforms.containsKey(platformId)) {
                throw new IllegalArgumentException("Only one configuration is allowed per platform");
            }
            platforms.put(platformId, platformConfig);
            return this;
        }

        public Builder addDebugPrint(DebugPrint debugPrint) {
            debugPrints.add(debugPrint);
            return this;
        }

        public TrackingConfig build() {
            if (platforms.isEmpty()) {
                throw new IllegalStateException("You should configure at least one platform");
            }

            return new TrackingConfig(platforms, debugPrints);
        }
    }
}
