/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.data.models.cheapshark;

/**
 * Game object returned from the API.
 */
public class Game {
    private String gameID; //:"146",
    private String steamAppID; //"35140",
    private float cheapest; // "19.95",
    private String cheapestDealID; //n96QeQ9FLRDoZToO75BS2Dx22PPtc7hlpcehVxUlW2c%3D",
    private String external; //Batman: Arkham Asylum Game of the Year Edition",
    private String thumb; // http:\/\/cdn.akamai.steamstatic.com\/steam\/apps\/35140\/capsule_sm_120.jpg?t=1382035811"

    public String getGameID() {
        return gameID;
    }

    public void setGameID(final String gameID) {
        this.gameID = gameID;
    }

    public String getSteamAppID() {
        return steamAppID;
    }

    public void setSteamAppID(final String steamAppID) {
        this.steamAppID = steamAppID;
    }

    /**
     * Get the cheapest price the game has ever been
     *
     * @return float representing the cheapest price in US Dollars
     */
    public float getCheapest() {
        return cheapest;
    }

    public void setCheapest(final float cheapest) {
        this.cheapest = cheapest;
    }

    public String getCheapestDealID() {
        return cheapestDealID;
    }

    public void setCheapestDealID(final String cheapestDealID) {
        this.cheapestDealID = cheapestDealID;
    }

    /**
     * Retrieves the name of the game
     *
     * @return A String of the game name.
     */
    public String getExternal() {
        return external;
    }

    public void setExternal(final String external) {
        this.external = external;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(final String thumb) {
        this.thumb = thumb;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (Float.compare(game.cheapest, cheapest) != 0) return false;
        if (!gameID.equals(game.gameID)) return false;
        if (!steamAppID.equals(game.steamAppID)) return false;
        if (!cheapestDealID.equals(game.cheapestDealID)) return false;
        if (!external.equals(game.external)) return false;
        if (!thumb.equals(game.thumb)) return false;

        return true;
    }

    @Override public int hashCode() {
        int result = gameID.hashCode();
        result = 31 * result + steamAppID.hashCode();
        result = 31 * result + (cheapest != +0.0f ? Float.floatToIntBits(cheapest) : 0);
        result = 31 * result + cheapestDealID.hashCode();
        result = 31 * result + external.hashCode();
        result = 31 * result + thumb.hashCode();
        return result;
    }
}
