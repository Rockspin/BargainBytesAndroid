package com.rockspin.bargainbits.watch_list.job;

import android.content.Context;

import com.annimon.stream.Stream;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.models.GameInfo;
import com.rockspin.bargainbits.util.format.PriceFormatter;

import java.util.ArrayList;
import java.util.List;

import static com.fernandocejas.arrow.checks.Preconditions.checkNotNull;

final class GameTitlePrice {
    final List<String> notificationTextList;
    final int gamesOnSaleCount;

    GameTitlePrice(Context context, List<GameInfo> gameInfoList, PriceFormatter priceFormatter) {
        checkNotNull(gameInfoList, "game info cannot be null");
        checkNotNull(priceFormatter, "cannot be null");
        checkNotNull(context, "cannot be null");
        gamesOnSaleCount = gameInfoList.size();
        notificationTextList = Stream.of(gameInfoList)
                .limit(WatchListNotification.MAX_NOTIFICATION_LINES)
                .map(input -> {
                    String price = priceFormatter.formatPrice((float) input.getLowestSalePrice());
                    String title = input.getInfo().getGameName();
                    return context.getString(R.string.watch_list_notification_item_entry, title, price);
                })
                .reduce(new ArrayList<>(), (list, value) -> {
                    list.add(value);
                    return list;
                });
    }
}