package com.pixable.trackingwrap.trace;

import android.content.Context;

import com.pixable.trackingwrap.platform.Platform;

import java.util.Collection;
import java.util.Map;

public interface TraceProxy {

    void traceMessage(Context context, String messageTitle, Map<String, String> properties, Collection<Platform.Id> platforms);
}
