package com.pixable.trackingwrap.trace;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pixable.trackingwrap.R;
import com.pixable.trackingwrap.platform.Platform;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;

class ToastTraceProxy implements TraceProxy {
    private final int backgroundColor = android.R.color.black;

    @Override
    public void traceMessage(Context context, String messageTitle, Map<String, String> properties, Collection<Platform.Id> platforms) {
        final LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.tracking_debug_toast, null);

        final Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.START | Gravity.BOTTOM,
                context.getResources().getInteger(R.integer.tracking_toast_margin),
                context.getResources().getInteger(R.integer.tracking_toast_margin));
        ButterKnife.findById(layout, R.id.toast_container).setBackgroundColor(
                context.getResources().getColor(backgroundColor));

        ((TextView) ButterKnife.findById(layout, R.id.toast_title)).setText(messageTitle);
        ((TextView) ButterKnife.findById(layout, R.id.toast_parameters)).setText(
                mapToLinedString(properties));

        for (Platform.Id platform : platforms) {
            final int platformIcon;
            switch (platform) {
                case FLURRY:
                    platformIcon = R.id.toast_icon_flurry;
                    break;
                case MIXPANEL:
                    platformIcon = R.id.toast_icon_mixpanel;
                    break;
                default:
                    throw new IllegalArgumentException("There's no icon for the platform " + platform);
            }

            ButterKnife.findById(layout, platformIcon).setVisibility(View.VISIBLE);
        }

        toast.setView(layout);

        /*
        ButterKnife.findById(layout, R.id.toast_icon_mixpanel_main).setVisibility(
                showDestination && toMixpanelMain ? View.VISIBLE : View.GONE);
        ButterKnife.findById(layout, R.id.toast_icon_mixpanel_historical).setVisibility(
                showDestination && toMixpanelHistorical ? View.VISIBLE : View.GONE);
        ButterKnife.findById(layout, R.id.toast_icon_ga).setVisibility(
                showDestination && toGoogleAnalytics ? View.VISIBLE : View.GONE);
        ButterKnife.findById(layout, R.id.toast_icon_facebook).setVisibility(
                showDestination && toFacebook ? View.VISIBLE : View.GONE);
        */

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
