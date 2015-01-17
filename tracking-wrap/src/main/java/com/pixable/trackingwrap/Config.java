package com.pixable.trackingwrap;

import com.pixable.trackingwrap.platform.Platform;
import com.pixable.trackingwrap.trace.TraceId;

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

    public Set<Platform.Id> getPlatformIds() {
        final Set<Platform.Id> platformIds = new HashSet<>();
        for (Platform platform : platforms) {
            platformIds.add(platform.getId());
        }
        return platformIds;
    }

    public Set<TraceId> getTraceIds() {
        return traceIds;
    }

    public static class Builder {
        private Set<Platform> platforms = new HashSet<>();
        private Set<TraceId> traceIds = new HashSet<>();

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

        public Builder addTrace(TraceId traceId) {
            traceIds.add(traceId);
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
