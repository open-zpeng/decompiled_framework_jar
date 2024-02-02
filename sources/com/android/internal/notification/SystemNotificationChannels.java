package com.android.internal.notification;

import android.app.INotificationManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.media.AudioAttributes;
import android.os.RemoteException;
import android.provider.Settings;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes3.dex */
public class SystemNotificationChannels {
    public static String VIRTUAL_KEYBOARD = "VIRTUAL_KEYBOARD";
    public static String PHYSICAL_KEYBOARD = "PHYSICAL_KEYBOARD";
    public static String SECURITY = "SECURITY";
    public static String CAR_MODE = "CAR_MODE";
    public static String ACCOUNT = "ACCOUNT";
    public static String DEVELOPER = "DEVELOPER";
    public static String UPDATES = "UPDATES";
    public static String NETWORK_STATUS = "NETWORK_STATUS";
    public static String NETWORK_ALERTS = "NETWORK_ALERTS";
    public static String NETWORK_AVAILABLE = "NETWORK_AVAILABLE";
    public static String VPN = "VPN";
    public static String DEVICE_ADMIN = "DEVICE_ADMIN";
    public static String ALERTS = "ALERTS";
    public static String RETAIL_MODE = "RETAIL_MODE";
    public static String USB = "USB";
    public static String FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    public static String HEAVY_WEIGHT_APP = "HEAVY_WEIGHT_APP";
    public static String SYSTEM_CHANGES = "SYSTEM_CHANGES";
    public static String DO_NOT_DISTURB = "DO_NOT_DISTURB";

    public static void createAll(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NotificationManager.class);
        List<NotificationChannel> channelsList = new ArrayList<>();
        NotificationChannel keyboard = new NotificationChannel(VIRTUAL_KEYBOARD, context.getString(R.string.notification_channel_virtual_keyboard), 2);
        keyboard.setBlockableSystem(true);
        channelsList.add(keyboard);
        NotificationChannel physicalKeyboardChannel = new NotificationChannel(PHYSICAL_KEYBOARD, context.getString(R.string.notification_channel_physical_keyboard), 3);
        physicalKeyboardChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, Notification.AUDIO_ATTRIBUTES_DEFAULT);
        physicalKeyboardChannel.setBlockableSystem(true);
        channelsList.add(physicalKeyboardChannel);
        NotificationChannel security = new NotificationChannel(SECURITY, context.getString(R.string.notification_channel_security), 2);
        channelsList.add(security);
        NotificationChannel car = new NotificationChannel(CAR_MODE, context.getString(R.string.notification_channel_car_mode), 2);
        car.setBlockableSystem(true);
        channelsList.add(car);
        channelsList.add(newAccountChannel(context));
        NotificationChannel developer = new NotificationChannel(DEVELOPER, context.getString(R.string.notification_channel_developer), 2);
        developer.setBlockableSystem(true);
        channelsList.add(developer);
        NotificationChannel updates = new NotificationChannel(UPDATES, context.getString(R.string.notification_channel_updates), 2);
        channelsList.add(updates);
        NotificationChannel network = new NotificationChannel(NETWORK_STATUS, context.getString(R.string.notification_channel_network_status), 2);
        channelsList.add(network);
        NotificationChannel networkAlertsChannel = new NotificationChannel(NETWORK_ALERTS, context.getString(R.string.notification_channel_network_alerts), 4);
        networkAlertsChannel.setBlockableSystem(true);
        channelsList.add(networkAlertsChannel);
        NotificationChannel networkAvailable = new NotificationChannel(NETWORK_AVAILABLE, context.getString(R.string.notification_channel_network_available), 2);
        networkAvailable.setBlockableSystem(true);
        channelsList.add(networkAvailable);
        NotificationChannel vpn = new NotificationChannel(VPN, context.getString(R.string.notification_channel_vpn), 2);
        channelsList.add(vpn);
        NotificationChannel deviceAdmin = new NotificationChannel(DEVICE_ADMIN, context.getString(R.string.notification_channel_device_admin), 2);
        channelsList.add(deviceAdmin);
        NotificationChannel alertsChannel = new NotificationChannel(ALERTS, context.getString(R.string.notification_channel_alerts), 3);
        channelsList.add(alertsChannel);
        NotificationChannel retail = new NotificationChannel(RETAIL_MODE, context.getString(R.string.notification_channel_retail_mode), 2);
        channelsList.add(retail);
        NotificationChannel usb = new NotificationChannel(USB, context.getString(R.string.notification_channel_usb), 1);
        channelsList.add(usb);
        NotificationChannel foregroundChannel = new NotificationChannel(FOREGROUND_SERVICE, context.getString(R.string.notification_channel_foreground_service), 2);
        foregroundChannel.setBlockableSystem(true);
        channelsList.add(foregroundChannel);
        NotificationChannel heavyWeightChannel = new NotificationChannel(HEAVY_WEIGHT_APP, context.getString(R.string.notification_channel_heavy_weight_app), 3);
        heavyWeightChannel.setShowBadge(false);
        heavyWeightChannel.setSound(null, new AudioAttributes.Builder().setContentType(4).setUsage(10).build());
        channelsList.add(heavyWeightChannel);
        NotificationChannel systemChanges = new NotificationChannel(SYSTEM_CHANGES, context.getString(R.string.notification_channel_system_changes), 2);
        channelsList.add(systemChanges);
        NotificationChannel dndChanges = new NotificationChannel(DO_NOT_DISTURB, context.getString(R.string.notification_channel_do_not_disturb), 2);
        channelsList.add(dndChanges);
        nm.createNotificationChannels(channelsList);
    }

    public static void createAccountChannelForPackage(String pkg, int uid, Context context) {
        INotificationManager iNotificationManager = NotificationManager.getService();
        try {
            iNotificationManager.createNotificationChannelsForPackage(pkg, uid, new ParceledListSlice(Arrays.asList(newAccountChannel(context))));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static NotificationChannel newAccountChannel(Context context) {
        return new NotificationChannel(ACCOUNT, context.getString(R.string.notification_channel_account), 2);
    }

    private SystemNotificationChannels() {
    }
}
