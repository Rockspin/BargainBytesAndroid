package com.rockspin.bargainbits.data.models.cheapshark;

import com.google.gson.annotations.Expose;

public class Info {

    @Expose private String title;

    @Expose private String thumb;

    @Expose private String gameId;

    public final String getGameName() {
        return title;
    }

    public final void setTitle(final String title) {
        this.title = title;
    }

    public String getThumbnailURL() {
        return thumb;
    }

    public void setThumb(final String thumb) {
        this.thumb = thumb;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(final String gameId) {
        this.gameId = gameId;
    }
}
