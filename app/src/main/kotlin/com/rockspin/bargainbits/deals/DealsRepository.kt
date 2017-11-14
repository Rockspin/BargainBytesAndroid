package com.rockspin.bargainbits.deals

import com.rockspin.bargainbits.data.models.GameDeal
import com.rockspin.bargainbits.data.rest_client.GameApiService
import io.reactivex.Observable
import io.reactivex.Single


class DealsRepository(private var api: GameApiService) {

    fun deals(): Single<List<Game>> {
        return api.getDeals()
                .map { apiDeals: List<GameDeal> -> convert(apiDeals) }
                .flatMapObservable { mutableGames: List<MutableGame> -> Observable.fromIterable(mutableGames) }
                .map { mutableGame: MutableGame -> mutableGame.toGame() }
                .toList()
    }

    private fun convert(apiDeals: List<GameDeal>): List<MutableGame> {
        val games = mutableMapOf<String, MutableGame>()
        for (apiDeal in apiDeals) {
            game(apiDeal, games)
        }

        return games.values.toList()
    }

    private fun game(apiDeal: GameDeal, games: MutableMap<String, MutableGame>) {
        if (!games.containsKey(apiDeal.gameId)) {
            games.put(apiDeal.gameId, MutableGame(
                    gameId = apiDeal.gameId,
                    title = apiDeal.title,
                    thumbUrl = apiDeal.thumbUrl))
        }

        games[apiDeal.gameId]?.let { deal(apiDeal, it) }
    }

    private fun deal(apiDeal: GameDeal, game: MutableGame) {
        if (!game.deals.containsKey(apiDeal.dealId)) {
            game.deals.put(apiDeal.dealId,
                    Deal(apiDeal.dealId, apiDeal.dealRating, apiDeal.normalPrice, apiDeal.salePrice))
        }
    }

    private class MutableGame(
            val gameId: String,
            val title: String,
            val thumbUrl: String,
            val deals: MutableMap<String, Deal> = mutableMapOf()
    ) {
        fun toGame(): Game {
            return Game(gameId, title, thumbUrl, deals.values.toList())
        }
    }
}