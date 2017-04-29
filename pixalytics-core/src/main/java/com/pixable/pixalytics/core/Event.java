package com.pixable.pixalytics.core;

import java.util.HashMap;
import java.util.Map;

/**
 * An event that is to be tracked, that has a name and a set of properties (key-value pairs).
 */
public class Event extends KeyValueMap {

    /**
     * Use the {@link Event.Builder} to create an instance.
     */
    private Event(String eventName, Map<String, Object> properties) {
        super(eventName, properties);
    }

    public static class Builder {
        private String name = null;
        private final Map<String, Object> properties = new HashMap<>();

        /** Constructor to create a new event from scratch. */
        public Builder() {
            // Intentionally empty
        }

        /** Constructor to create an event copy.*/
        Builder(Event existingEvent) {
            this.name = existingEvent.getName();
            properties(existingEvent.getProperties());
        }

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
        public Builder property(String name, Object value) {
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
