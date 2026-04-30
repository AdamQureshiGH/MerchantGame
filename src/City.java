public class City {
    private String name;
    private String nextCityName;
    private int distanceToNext;
    private String description;
    private Market cityMarket;

    private String boomingItemName = "";
    private double priceMultiplier = 1.0;
    private String rumorText = "";
    public City(String name, String nextCity, int dist, String desc, Market market) {
        this.name = name;
        this.nextCityName = nextCity;
        this.distanceToNext = dist;
        this.description = desc;
        this.cityMarket = market;
    }
    public Market getMarket(){
        return cityMarket;
    }
    public String getName() {
        return name;
    }
    public String getNextCityName() {
        return nextCityName;
    }
    public int getDistance() {
        return distanceToNext;
    }
    public String getDescription() {
        return description;
    }

    public void setBoomingItem(String name, double mult) {
        this.boomingItemName = name;
        this.priceMultiplier = mult;
    }
    public String getBoomingItemName() { return boomingItemName; }
    public double getPriceMultiplier() { return priceMultiplier; }

    public void setRumor(String text) { this.rumorText = text; }
    public String getRumor() { return rumorText; }
}
