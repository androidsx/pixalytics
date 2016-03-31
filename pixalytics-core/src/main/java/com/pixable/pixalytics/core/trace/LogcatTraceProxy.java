package com.pixable.pixalytics.core.trace;

import android.util.Log;

import com.pixable.pixalytics.core.platform.Platform;

import java.util.Collection;
import java.util.Map;

public class LogcatTraceProxy implements TraceProxy {
    private static final String TAG = "Pixalytics-" + LogcatTraceProxy.class.getSimpleName();

    private final String id;

    public LogcatTraceProxy(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void traceMessage(Level level, String messageTitle, Map<String, Object> properties, Collection<Platform> platforms) {
        final String finalMessage = messageTitle + " (" + properties + ") to " + platforms.toString();
        switch (level) {
            case DEBUG:
                Log.d(TAG, finalMessage);
                break;
            case INFO:
                Log.i(TAG, finalMessage);
                break;
            default:
                throw new IllegalArgumentException("What level is " + level + "?");
        }
    }
}
