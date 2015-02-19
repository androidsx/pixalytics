package com.pixable.pixalytics.core.trace;

import android.app.Service;
import android.content.Context;
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

import butterknife.ButterKnife;

public class ToastTraceProxy implements TraceProxy {

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
            backgroundColorMap.put(Level.DEBUG, R.color.toast_debug);
            backgroundColorMap.put(Level.INFO, R.color.toast_info);
        }

        return backgroundColorMap;
    }

    @Override
    public void traceMessage(Context context, Level level, String messageTitle, Map<String, String> properties, Collection<Platform> platforms) {
        final LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.tracking_debug_toast, null);

        final Toast toast = new Toast(context);
        //noinspection ResourceType
        toast.setDuration(durationMap.get(level));
        toast.setGravity(Gravity.START | Gravity.BOTTOM,
                context.getResources().getInteger(R.integer.tracking_toast_margin),
                context.getResources().getInteger(R.integer.tracking_toast_margin));
        ButterKnife.findById(layout, R.id.toast_container).setBackgroundColor(
                context.getResources().getColor(getBackgroundColorMap().get(level)));

        ((TextView) ButterKnife.findById(layout, R.id.toast_title)).setText(messageTitle);
        ((TextView) ButterKnife.findById(layout, R.id.toast_parameters)).setText(
                mapToLinedString(properties));

        LinearLayout platformsList = ButterKnife.findById(layout, R.id.platforms_list);
        for (Platform platform : platforms) {
            ImageView platformIcon = new ImageView(context);
            platformIcon.setPadding(1, 1, 1, 1);
            platformIcon.setImageResource(platform.getIconId());
            platformsList.addView(platformIcon);
            // ButterKnife.findById(layout, platform.getIconId()).setVisibility(View.VISIBLE);
        }

        toast.setView(layout);

        toast.show();
    }

    private static String mapToLinedString(Map<String, String> map) {
        final StringBuilder builder = new StringBuilder();
        final Map<String, String> sortedMap = new TreeMap<>(map);
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            builder.append("- ");
            builder.append(entry.getKey());
            builder.append(": ");
            builder.append(entry.getValue());
            builder.append('\n');
        }
        return (builder.length() > 0) ? builder.substring(0, builder.length() - 1) : "";
    }
}
