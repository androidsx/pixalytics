package com.pixable.trackingwrap.trace;

import android.content.Context;
import android.util.Log;

import com.pixable.trackingwrap.platform.Platform;

import java.util.Collection;
import java.util.Map;

public class LogcatTraceProxy implements TraceProxy {
    private static final String TAG = LogcatTraceProxy.class.getSimpleName();

    @Override
    public void traceMessage(Context context, Level level, String messageTitle, Map<String, String> properties, Collection<Platform.Id> platforms) {
        final String finalMessage = messageTitle + " (" + properties + ") to " + platforms;
        switch (level) {
            case DEBUG: Log.d(TAG, finalMessage); break;
            case INFO: Log.i(TAG, finalMessage); break;
            default: throw new IllegalArgumentException("What level is " + level + "?");
        }
    }
}
