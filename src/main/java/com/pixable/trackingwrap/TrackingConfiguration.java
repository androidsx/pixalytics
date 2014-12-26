package com.pixable.trackingwrap;

import java.util.HashSet;
import java.util.Set;

public class TrackingConfiguration {
    private final Set<DebugPrint> debugPrints;

    public TrackingConfiguration(Set<DebugPrint> debugPrints) {
        this.debugPrints = debugPrints;
    }

    public Set<DebugPrint> getDebugPrints() {
        return debugPrints;
    }

    public enum DebugPrint {
        LOGCAT,
        TOAST;
    }

    public static class Builder {
        private Set<DebugPrint> debugPrints = new HashSet<>();

        public Builder addDebugPrint(DebugPrint debugPrint) {
            debugPrints.add(debugPrint);
            return this;
        }

        public TrackingConfiguration build() {
            return new TrackingConfiguration(debugPrints);
        }
    }
}
