package com.rockspin.bargainbits.watch_list.job;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import com.fernandocejas.arrow.checks.Preconditions;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.ui.watch_list.WatchListActivity;
import java.util.List;

public final class WatchListNotification {
    public final static int MAX_NOTIFICATION_LINES = 5; // only display up to 5 games in a notification

    private WatchListNotification() { /* not used */ }

    /**
     * Set up a Notification for a single game.
     *
     * @param context android context.
     * @param string text to be displayed.
     * @return A notification.
     */
    public static Notification setUpNotification(Context context, String string) {
        NotificationCompat.Builder builder = setupDefaultBuilder(context);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_RECOMMENDATION);
        }

        builder.setContentTitle(context.getString(R.string.watch_list_notification_title_singular));
        builder.setContentText(string);
        return builder.build();
    }

    /**
     * Set up a Notification for a single game.
     *
     * @param gameTitlePrices must not be empty or have more that MAX_NOTIFICATION_LINES items.
     * @return A notification.
     */
    public static Notification setUpNotification(Context context, List<String> gameTitlePrices, int totalGamesOnSale) {
        Preconditions.checkArgument(gameTitlePrices.size() > 0 && gameTitlePrices.size() <= MAX_NOTIFICATION_LINES,
            "gameTitlePrices must not be empty or have more that MAX_NOTIFICATION_LINES items");
        NotificationCompat.Builder builder = setupDefaultBuilder(context);

        builder.setContentTitle(context.getString(R.string.watch_list_notification_title_plural));
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(context.getString(R.string.watch_list_notification_title_plural) + ":");

        for (String titlePrice : gameTitlePrices) {
            inboxStyle.addLine(titlePrice);
        }

        final int hiddenGames = totalGamesOnSale - MAX_NOTIFICATION_LINES;
        if (hiddenGames > 0) {
            inboxStyle.addLine(context.getString(R.string.watch_list_notification_more_items_available, hiddenGames));
        }

        builder.setStyle(inboxStyle);
        builder.setContentText(context.getString(R.string.watch_list_notification_multiple_items_description, totalGamesOnSale));
        return builder.build();
    }

    @NonNull private static NotificationCompat.Builder setupDefaultBuilder(Context context) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_watchlist_notification);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);
        builder.setColor(ContextCompat.getColor(context, R.color.primary_color));
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        final Intent watchListActivityIntent = new Intent(context, WatchListActivity.class);
        watchListActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(WatchListActivity.class);
        stackBuilder.addNextIntent(watchListActivityIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        return builder;
    }
}