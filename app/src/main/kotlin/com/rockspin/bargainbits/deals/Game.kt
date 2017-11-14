package com.rockspin.bargainbits.deals

data class Game(var gameId: String, var title: String, var imageUrl: String, var deals: List<Deal>)