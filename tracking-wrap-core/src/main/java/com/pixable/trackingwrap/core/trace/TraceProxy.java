package com.pixable.trackingwrap.core.trace;

import android.content.Context;

import com.pixable.trackingwrap.core.platform.Platform;

import java.util.Collection;
import java.util.Map;

public interface TraceProxy {

    enum Level {DEBUG, INFO;}

    /**
     * Must be run in the UI thread.
     */
    void traceMessage(Context context,
                      Level level,
                      String messageTitle,
                      Map<String, String> properties, Collection<Platform> platforms);
}
