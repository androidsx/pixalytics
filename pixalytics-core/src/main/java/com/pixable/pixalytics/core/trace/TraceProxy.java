package com.pixable.pixalytics.core.trace;

import android.content.Context;

import com.pixable.pixalytics.core.platform.Platform;

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
                      Map<String, Object> properties, Collection<Platform> platforms);
}
