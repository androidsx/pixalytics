package com.pixable.pixalytics.core.trace;

import com.pixable.pixalytics.core.platform.Platform;

import java.util.Collection;
import java.util.Map;

public interface TraceProxy {

    enum Level {DEBUG, INFO;}

    String getId();

    void traceMessage(Level level,
                      String messageTitle,
                      Map<String, Object> properties, Collection<Platform> platforms);
}
