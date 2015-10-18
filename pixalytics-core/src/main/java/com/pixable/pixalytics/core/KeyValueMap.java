package com.pixable.pixalytics.core;

import org.json.JSONObject;

import java.util.Map;

/**
 * A simple key-value map storage.
 */
public class KeyValueMap {
    private final String name;
    private final Map<String, Object> properties;

    protected KeyValueMap(String name, Map<String, Object> properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public Object getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public JSONObject getPropertiesAsJson() {
        return new JSONObject(properties);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("KeyValueMap" + " \"").append(getName()).append("\" ");
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
}
