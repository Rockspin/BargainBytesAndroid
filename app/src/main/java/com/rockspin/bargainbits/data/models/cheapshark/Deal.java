package com.rockspin.bargainbits.data.models.cheapshark;

/**
 * Deal object returned from API.
 */
public class Deal {

    private String internalName; // : "SIMULATORS2WEEKLYBUNDLE",
    private String title; // iticLink: null,
    private String dealID; // "qjpZeJozG%2FSs00ottaLEJ976XNsAAFMBmY7IqWYh78g%3D",
    private String storeID; // "11",
    private String gameID; // "119348",
    private float salePrice; // "14.99",
    private float normalPrice; // "95.99",
    private float savings; // "84.383790",
    private String metacriticScore; //"0",
    private long releaseDate; //: 0, // NB Unix time stamp. should be long
    private long lastChange; //1406225316,
    private String dealRating; // "10.0",
    private String thumb; // "https://humblebundle-a.akamaihd.net/misc/files/hashed/9b75afb3fff4323d8af81ec3a4ee58e65f77fc40.jpg"

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(final String internalName) {
        this.internalName = internalName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDealID() {
        return dealID;
    }

    public final void setDealID(final String dealID) {
        this.dealID = dealID;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(final String storeID) {
        this.storeID = storeID;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(final String gameID) {
        this.gameID = gameID;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(final float salePrice) {
        this.salePrice = salePrice;
    }

    public float getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(final float normalPrice) {
        this.normalPrice = normalPrice;
    }

    public float getSavings() {
        return savings;
    }

    public void setSavings(final float savings) {
        this.savings = savings;
    }

    public String getMetacriticScore() {
        return metacriticScore;
    }

    public void setMetacriticScore(final String metacriticScore) {
        this.metacriticScore = metacriticScore;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(final int releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(final long lastChange) {
        this.lastChange = lastChange;
    }

    public String getDealRating() {
        return dealRating;
    }

    public void setDealRating(final String dealRating) {
        this.dealRating = dealRating;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(final String thumb) {
        this.thumb = thumb;
    }

    @Override public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Deal deal = (Deal) o;

        if (!gameID.equals(deal.gameID)) return false;
        //if (!salePrice.equals(deal.salePrice)) return false;
        //if (!savings.equals(deal.savings)) return false;
        return title.equals(deal.title);
    }

    @Override public int hashCode() {
        int result = internalName.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + dealID.hashCode();
        result = 31 * result + storeID.hashCode();
        result = 31 * result + gameID.hashCode();
        result = 31 * result + (salePrice != +0.0f ? Float.floatToIntBits(salePrice) : 0);
        result = 31 * result + (normalPrice != +0.0f ? Float.floatToIntBits(normalPrice) : 0);
        result = 31 * result + (savings != +0.0f ? Float.floatToIntBits(savings) : 0);
        result = 31 * result + metacriticScore.hashCode();
        result = 31 * result + (int) (releaseDate ^ (releaseDate >>> 32));
        result = 31 * result + (int) (lastChange ^ (lastChange >>> 32));
        result = 31 * result + dealRating.hashCode();
        result = 31 * result + thumb.hashCode();
        return result;
    }
}
