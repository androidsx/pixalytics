package com.pixable.trackingwrap;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * An event that is to be tracked, that has a name and a set of properties (key-value pairs).
 */
public class Event {
    private final String eventName;
    private final Map<String, String> properties;

    /**
     * Use the {@link Event.Builder}.
     */
    private Event(String eventName, Map<String, String> properties) {
        this.eventName = eventName;
        this.properties = properties;
    }

    public String getName() {
        return eventName;
    }

    public Object getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public JSONObject getPropertiesAsJson() {
        return new JSONObject(properties);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Event \"").append(getName()).append("\" ");
        if (getProperties().isEmpty()) {
            builder.append("with no properties");
        } else {
            builder.append("with ").append(getProperties().size()).append(" properties (");
            for (String key : getProperties().keySet()) {
                builder.append(key).append(": ").append(getProperty(key)).append(", ");
            }
            builder.deleteCharAt(builder.length() - 1).deleteCharAt(builder.length() - 1);
            builder.append(")");
        }
        return builder.toString();
    }

    public static class Builder {
        private String eventName = null;
        private final Map<String, String> properties = new HashMap<>();

        /** Sets the name of the event. This parameter is compulsory. */
        public Builder name(String eventName) {
            this.eventName = eventName;
            return this;
        }

        /** Adds a property. Add as many properties (key-value pairs) as you wish. */
        public Builder property(String name, String value) {
            properties.put(name, value);
            return this;
        }

        public Event build() {
            if (eventName == null) {
                throw new IllegalStateException("No name was provided for this event");
            } else {
                return new Event(eventName, properties);
            }
        }
    }
}
