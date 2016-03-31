package com.pixable.pixalytics.core.trace;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixable.pixalytics.core.R;
import com.pixable.pixalytics.core.platform.Platform;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ToastTraceProxy implements TraceProxy {
    private static final String TAG = "Pixalytics-" + ToastTraceProxy.class.getSimpleName();

    private final String id;

    private final Context applicationContext;

    final Map<Level, Integer> durationMap = new HashMap<Level, Integer>() {{
        put(Level.DEBUG, Toast.LENGTH_SHORT);
        put(Level.INFO, Toast.LENGTH_LONG);
    }};

    /**
     * Read always through {@link #getBackgroundColorMap}
     */
    final Map<Level, Integer> backgroundColorMap = new HashMap<>();

    public ToastTraceProxy(Context context, String id) {
        this.applicationContext = context.getApplicationContext();
        this.id = id;
    }

    private Map<Level, Integer> getBackgroundColorMap() {
        if (backgroundColorMap.isEmpty()) {
            backgroundColorMap.put(Level.DEBUG, R.color.pixalytics__toast_debug);
            backgroundColorMap.put(Level.INFO, R.color.pixalytics__toast_info);
        }

        return backgroundColorMap;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void traceMessage(Level level, final String messageTitle, Map<String, Object> properties, final Collection<Platform> platforms) {
        final int color = applicationContext.getResources().getColor(getBackgroundColorMap().get(level));
        final Integer duration = durationMap.get(level);
        final String messageBody = mapToLinedString(properties);

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // We're in the UI thread, all fine
            displayToast(color, duration, messageTitle, messageBody, platforms);
        } else {
            // Well, even if we were not called in the UI thread, let's fix it
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    displayToast(color, duration, messageTitle, messageBody, platforms);
                }
            });
        }
    }

    /** Must be run in the UI thread. */
    private void displayToast(int color, int duration, String messageTitle, String messageBody, Collection<Platform> platforms) {
        final LayoutInflater inflater =
                (LayoutInflater) applicationContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View layout = inflater.inflate(R.layout.pixalytics__tracking_debug_toast, null);

        final Toast toast = new Toast(applicationContext);
        //noinspection ResourceType
        toast.setDuration(duration);
        toast.setGravity(Gravity.START | Gravity.BOTTOM,
                applicationContext.getResources().getInteger(R.integer.pixalytics__tracking_toast_margin),
                applicationContext.getResources().getInteger(R.integer.pixalytics__tracking_toast_margin));
        layout.findViewById(R.id.pixalytics__toast_container).setBackgroundColor(
                color);

        ((TextView) layout.findViewById(R.id.pixalytics__toast_title)).setText(messageTitle);
        ((TextView) layout.findViewById(R.id.pixalytics_toast_parameters)).setText(messageBody);

        LinearLayout platformsList = (LinearLayout) layout.findViewById(R.id.pixalytics_platforms_list);
        for (Platform platform : platforms) {
            ImageView platformIcon = new ImageView(applicationContext);
            platformIcon.setPadding(8, 0, 0, 0); // A little separation between platform icons
            platformIcon.setImageResource(platform.getIconId());
            final int iconSize = (int) applicationContext.getResources().getDimension(R.dimen.pixalytics__tracking_toast_platform_icon_size);
            platformIcon.setLayoutParams(new LinearLayout.LayoutParams(iconSize, iconSize));
            platformsList.addView(platformIcon);
        }

        toast.setView(layout);

        toast.show();
    }

    private static String mapToLinedString(Map<String, Object> map) {
        final StringBuilder builder = new StringBuilder();
        final Map<String, Object> sortedMap = new TreeMap<>(map);
        for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
            builder.append("- ");
            builder.append(entry.getKey());
            builder.append(": ");
            builder.append(entry.getValue() == null ? "<null>" : entry.getValue().toString());
            builder.append('\n');
        }
        return (builder.length() > 0) ? builder.substring(0, builder.length() - 1) : "";
    }
}
