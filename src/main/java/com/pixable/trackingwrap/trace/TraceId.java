package com.pixable.trackingwrap.trace;

public enum TraceId {
    LOGCAT,
    TOAST;

    public TraceProxy getProxy() {
        switch (this) {
            case LOGCAT: return new LogcatTraceProxy();
            case TOAST: return new ToastTraceProxy();
        }
        throw new IllegalStateException("What kind of trace is " + this + "?");
    }
}
