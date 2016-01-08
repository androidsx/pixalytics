package com.pixable.pixalytics.core;

import com.pixable.pixalytics.core.platform.Platform;
import com.pixable.pixalytics.core.trace.TraceProxy;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Config {
    private final Collection<Platform> platforms;
    private final Collection<TraceProxy> traces;

    public Config(Set<Platform> platforms, Collection<TraceProxy> traces) {
        this.platforms = platforms;
        this.traces = traces;
    }

    public Collection<Platform> getPlatforms() {
        return platforms;
    }

    public Collection<TraceProxy> getTraces() {
        return traces;
    }

    public static class Builder {
        private Set<Platform> platforms = new HashSet<>();

        // A map because we want to enforce uniqueness on the ID. For platforms, there's no
        // need because we provide a superclass that already does that. Not so consistent :(
        private HashMap<String, TraceProxy> traces = new HashMap<>();

        /** Constructor to create a new config from scratch. */
        public Builder() {
            // Intentionally empty
        }

        /** Constructor to create a new config on top of an existing one. */
        public Builder(Config existingConfig) {
            for (Platform platform : existingConfig.getPlatforms()) {
                addPlatform(platform);
            }
            for (TraceProxy trace : existingConfig.getTraces()) {
                addTrace(trace);
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
        public Builder addTrace(TraceProxy trace) {
            traces.put(trace.getId(), trace);
            return this;
        }

        /** Removes the provided trace, if it exists. */
        public Builder removeTrace(TraceProxy trace) {
            traces.remove(trace.getId());
            return this;
        }

        public Config build() {
            if (platforms.isEmpty()) {
                throw new IllegalStateException("You should configure at least one platform");
            }

            return new Config(platforms, traces.values());
        }
    }
}
