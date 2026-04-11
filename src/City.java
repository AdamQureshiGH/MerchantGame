public class City {
    private String name;
    private String nextCityName;
    private int distanceToNext;
    private String description;

    public City(String name, String nextCity, int dist, String desc) {
        this.name = name;
        this.nextCityName = nextCity;
        this.distanceToNext = dist;
        this.description = desc;
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
}
