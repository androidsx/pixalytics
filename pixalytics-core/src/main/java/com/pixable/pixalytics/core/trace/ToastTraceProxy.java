package com.pixable.pixalytics.core.trace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
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

    final Map<Level, Integer> durationMap = new HashMap<Level, Integer>() {{
        put(Level.DEBUG, Toast.LENGTH_SHORT);
        put(Level.INFO, Toast.LENGTH_LONG);
    }};
    /**
     * Read always through {@link #getBackgroundColorMap}
     */
    final Map<Level, Integer> backgroundColorMap = new HashMap<>();

    private Map<Level, Integer> getBackgroundColorMap() {
        if (backgroundColorMap.isEmpty()) {
            backgroundColorMap.put(Level.DEBUG, R.color.pixalytics__toast_debug);
            backgroundColorMap.put(Level.INFO, R.color.pixalytics__toast_info);
        }

        return backgroundColorMap;
    }

    @Override
    public void traceMessage(final Context context, Level level, final String messageTitle, Map<String, Object> properties, final Collection<Platform> platforms) {
        final int color = context.getResources().getColor(getBackgroundColorMap().get(level));
        final Integer duration = durationMap.get(level);
        final String messageBody = mapToLinedString(properties);

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // We're in the UI thread, all fine
            displayToast(context, color, duration, messageTitle, messageBody, platforms);
        } else if (context instanceof Activity) {
            // Well, even if we were not called in the UI thread, let's try to fix it
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayToast(context, color, duration, messageTitle, messageBody, platforms);
                }
            });
        } else {
            // Nop, no way we can show a toast
            Log.w(TAG, "Can't create the toast for the message \"" + messageTitle + "\": we're not in the UI thread");
        }
    }

    /** Must be run in the UI thread. */
    private void displayToast(Context context, int color, int duration, String messageTitle, String messageBody, Collection<Platform> platforms) {
        final LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View layout = inflater.inflate(R.layout.pixalytics__tracking_debug_toast, null);

        final Toast toast = new Toast(context);
        //noinspection ResourceType
        toast.setDuration(duration);
        toast.setGravity(Gravity.START | Gravity.BOTTOM,
                context.getResources().getInteger(R.integer.pixalytics__tracking_toast_margin),
                context.getResources().getInteger(R.integer.pixalytics__tracking_toast_margin));
        layout.findViewById(R.id.pixalytics__toast_container).setBackgroundColor(
                color);

        ((TextView) layout.findViewById(R.id.pixalytics__toast_title)).setText(messageTitle);
        ((TextView) layout.findViewById(R.id.pixalytics_toast_parameters)).setText(messageBody);

        LinearLayout platformsList = (LinearLayout) layout.findViewById(R.id.pixalytics_platforms_list);
        for (Platform platform : platforms) {
            ImageView platformIcon = new ImageView(context);
            platformIcon.setPadding(8, 0, 0, 0); // A little separation between platform icons
            platformIcon.setImageResource(platform.getIconId());
            final int iconSize = (int) context.getResources().getDimension(R.dimen.pixalytics__tracking_toast_platform_icon_size);
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
