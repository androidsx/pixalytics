package com.pixable.trackingwrap.trace;

import android.content.Context;
import android.util.Log;

import com.pixable.trackingwrap.platform.Platform;

import java.util.Collection;
import java.util.Map;

class LogcatTraceProxy implements TraceProxy {
    private static final String TAG = LogcatTraceProxy.class.getSimpleName();

    @Override
    public void traceMessage(Context context, String messageTitle, Map<String, String> properties, Collection<Platform.Id> platforms) {
        Log.d(TAG, messageTitle + " (" + properties + ") to " + platforms);
    }
}
