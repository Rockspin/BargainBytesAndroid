package com.rockspin.bargainbits.di.modules;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.rockspin.bargainbits.data.models.cheapshark.AbbreviatedDeal;
import com.rockspin.bargainbits.data.models.cheapshark.GameInfo;
import com.rockspin.bargainbits.data.models.currency.BBCurrency;
import com.rockspin.bargainbits.data.models.currency.CurrencyExchange;
import dagger.Module;
import dagger.Provides;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;
import timber.log.Timber;

@Module public class JsonSerialisationModule {

    @Provides @Singleton JsonDeserializer<CurrencyExchange> providesCurrencyExchangeJsonDeserializer() {
        return (json, typeOfT, context) -> {
            JsonObject rootObject = json.getAsJsonObject();

            final CurrencyExchange currencyExchange = new CurrencyExchange();
            currencyExchange.setBase(rootObject.get("base")
                                               .getAsString());
            currencyExchange.setDate(rootObject.get("date")
                                               .getAsString());

            JsonObject ratesObject = rootObject.get("rates")
                                               .getAsJsonObject();
            final List<BBCurrency> BBCurrencyList = new ArrayList<>();
            for (Map.Entry<String, JsonElement> entry : ratesObject.entrySet()) {
                String isoCode = entry.getKey();
                float exchangeRate = entry.getValue()
                                          .getAsFloat();
                BBCurrencyList.add(new BBCurrency(isoCode, exchangeRate));
            }

            currencyExchange.setRates(BBCurrencyList);

            return currencyExchange;
        };
    }

    @Provides @Singleton JsonDeserializer<List<GameInfo>> providesGameInfoListDeserializer() {
        return (json, typeOfT, context) -> {

            final Map<String, GameInfo> gameInfoMap = getMapFromJson(json);

            List<GameInfo> gameInfoList = new ArrayList<>(gameInfoMap.values());

            // Since we cannot guarantee that the map will have its gameIds ordered
            // the same way we passed them, grab the keys and set each game id value
            // individually in the list of game info
            final Set<String> keys = gameInfoMap.keySet();
            final String[] gameIdKeys = keys.toArray(new String[keys.size()]);
            for (int i = 0; i < gameInfoList.size(); ++i) {
                gameInfoList.get(i)
                            .getInfo()
                            .setGameId(gameIdKeys[i]);
            }
            return sanitizeResponse(gameInfoList);
        };
    }

    private Map<String, GameInfo> getMapFromJson(JsonElement json) {
        try {
            final Gson gson = new Gson();
            final Type listType = new TypeToken<Map<String, GameInfo>>() {
            }.getType();
            return gson.fromJson(json, listType);
        } catch (JsonSyntaxException e) {
            Timber.e("error parsing json: " + json.toString(), e);
        }
        return new HashMap<>();
    }

    private List<GameInfo> sanitizeResponse(final List<GameInfo> pResponse) {
        // some shops, as of (21.11.2014) can have a higher sale then retail price.
        // fix this by manually changing the price here
        for (final GameInfo gameInfo : pResponse) {
            final List<AbbreviatedDeal> dealList = gameInfo.getDeals();
            for (final AbbreviatedDeal deal : dealList) {
                final float retailPrice = deal.getRetailPrice();
                final float salePrice = deal.getPrice();

                if (retailPrice != salePrice) {
                    if (deal.getPrice() > deal.getRetailPrice()) {
                        deal.setRetailPrice(salePrice);
                    }
                }
            }
        }

        return pResponse;
    }
}
