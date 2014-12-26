package com.pixable.trackingwrap;

import java.util.HashMap;
import java.util.Map;

public class TrackingEvent {
    private final String eventName;
    private final Map<String, Object> properties;

    /**
     * Use the {@link TrackingEvent.Builder}.
     */
    private TrackingEvent(String eventName, Map<String, Object> properties) {
        this.eventName = eventName;
        this.properties = properties;
    }

    public String getEventName() {
        return eventName;
    }

    public Object getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public Map<String, Object> getAllProperties() {
        return properties;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Event \"").append(getEventName()).append("\" ");
        if (getAllProperties().isEmpty()) {
            builder.append("with no properties");
        } else {
            builder.append("with ").append(getAllProperties().size()).append(" properties (");
            for (String key : getAllProperties().keySet()) {
                builder.append(key).append(": ").append(getProperty(key)).append(", ");
            }
            builder.deleteCharAt(builder.length() - 1).deleteCharAt(builder.length() - 1);
            builder.append(")");
        }
        return builder.toString();
    }

    public static class Builder {
        private String eventName = null;
        private final Map<String, Object> properties = new HashMap<>();

        public Builder withName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public Builder addProperty(String name, Object value) {
            properties.put(name, value);
            return this;
        }

        public TrackingEvent build() {
            if (eventName == null) {
                throw new IllegalStateException("No name was provided for this event");
            } else {
                return new TrackingEvent(eventName, properties);
            }
        }
    }
}
