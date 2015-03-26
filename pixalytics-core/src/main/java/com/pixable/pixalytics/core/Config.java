package com.pixable.pixalytics.core;

import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.core.trace.TraceId;

import java.util.HashSet;
import java.util.Set;

public class Config {
    private final Set<Platform> platforms;
    private final Set<TraceId> traceIds;

    public Config(Set<Platform> platforms, Set<TraceId> traceIds) {
        this.platforms = platforms;
        this.traceIds = traceIds;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public Set<TraceId> getTraceIds() {
        return traceIds;
    }

    public static class Builder {
        private Set<Platform> platforms = new HashSet<>();
        private Set<TraceId> traceIds = new HashSet<>();

        /** Constructor to create a new config from scratch. */
        public Builder() {
            // Intentionally empty
        }

        /** Constructor to create a new config on top of an existing one. */
        public Builder(Config existingConfig) {
            for (Platform platform : existingConfig.getPlatforms()) {
                addPlatform(platform);
            }
            for (TraceId traceId : existingConfig.getTraceIds()) {
                addTrace(traceId);
            }
        }

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

        /** Adds the provided trace, or overwrites it if it already exists. */
        public Builder addTrace(TraceId traceId) {
            traceIds.add(traceId);
            return this;
        }

        /** Removes the provided trace, if it exists. */
        public Builder removeTrace(TraceId traceId) {
            traceIds.remove(traceId);
            return this;
        }

        public Config build() {
            if (platforms.isEmpty()) {
                throw new IllegalStateException("You should configure at least one platform");
            }

            return new Config(platforms, traceIds);
        }
    }
}
