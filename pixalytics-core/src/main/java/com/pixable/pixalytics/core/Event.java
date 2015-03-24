package com.pixable.pixalytics.core;

import java.util.HashMap;
import java.util.Map;

/**
 * An event that is to be tracked, that has a name and a set of properties (key-value pairs).
 */
public class Event extends Trackable {

    /**
     * Use the {@link Event.Builder}.
     *
     * @param eventName
     * @param properties
     */
    private Event(String eventName, Map<String, Object> properties) {
        super(eventName, properties, Type.EVENT);
    }

    public static class Builder {
        private String name = null;
        private final Map<String, Object> properties = new HashMap<>();

        /**
         * Sets the name of the Event object. This parameter is compulsory.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Adds a property. Add as many properties (key-value pairs) as you wish.
         */
        public Builder property(String name, String value) {
            properties.put(name, value);
            return this;
        }

        /**
         * Adds a bunch of properties
         */
        public Builder properties(Map<String, Object> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Event build() {
            if (name == null) {
                throw new IllegalStateException("No name was provided for this event");
            } else {
                return new Event(name, properties);
            }
        }
    }
}
