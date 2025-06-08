public class TravelBlog {
    /** Variables **/
    private String title;
    private String author;
    // Dates will be formatted like "mm/dd/yyyy" and we can use string.split to
    // access month, day, and year published
    private String datePublished;
    private Location location;
    // price will be on a scale of 1-5, with 1 being least expensive and 5 being
    // most expensive
    private int price;

    private String content;

    /** Constructors **/
    public TravelBlog() {
        this.title = "";
        this.author = "";
        this.datePublished = "01/01/2000";
        this.location = new Location();
        this.price = 1;
        this.content = "";
    }

    public TravelBlog(String title, String author, String datePublished, Location location, int price) {
        this.title = title;
        this.author = author;
        this.datePublished = datePublished;
        this.location = location;
        this.price = price;
    }

    public TravelBlog(String title, String author, String datePublished, String city, String country, int price,
            String content) {
        this.title = title;
        this.author = author;
        this.datePublished = datePublished;
        this.location = new Location(city, country);
        this.price = price;
        this.content = content;
    }

    public TravelBlog(String title, String author, int month, int day, int year, Location location, int price) {
        this.title = title;
        this.author = author;
        this.datePublished = String.format("%02d/%02d/%04d", month, day, year);
        this.location = location;
        this.price = price;
    }

    public TravelBlog(String title, String author, int month, int day, int year, String city, String country,
            int price) {
        this.title = title;
        this.author = author;
        this.datePublished = String.format("%02d/%02d/%04d", month, day, year);
        this.location = new Location(city, country);
        this.price = price;
    }

    /** Accessors **/
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getMonthPublished() {
        String[] date = datePublished.split("/");
        return date[0];
    }

    public String getDayPublished() {
        String[] date = datePublished.split("/");
        return date[1];
    }

    public String getYearPublished() {
        String[] date = datePublished.split("/");
        return date[2];
    }

    public Location getLocation() {
        return location;
    }

    public String getLocationCity() {
        return location.getCityName();
    }

    public String getLocationCountry() {
        return location.getCountryName();
    }

    public int getPrice() {
        return price;
    }

    public String getContent() {
        return content;
    }

    /** Setters **/
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDatePublished(String datePublished) {
        for (int i = 0; i < datePublished.length(); i++) {
            char c = datePublished.charAt(i);
            if ((c < '0' || c > '9') && c != '/') {
                throw new IllegalArgumentException("Date is not constituted of numbers");
            }
        }
        this.datePublished = datePublished;
    }

    public void setMonthPublished(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12.");
        }
        String[] date = datePublished.split("/");
        this.datePublished = String.format("%02d/%s/%s", month, date[1], date[2]);
    }

    public void setDayPublished(int day) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Day must be between 1 and 31.");
        }
        String[] date = datePublished.split("/");
        this.datePublished = String.format("%s/%02d/%s", date[0], day, date[2]);
    }

    public void setYearPublished(int year) {
        String[] date = datePublished.split("/");
        this.datePublished = String.format("%s/%s/%04d", date[0], date[1], year);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocation(String city, String country) {
        this.location = new Location(city, country);
    }

    public void setLocationCity(String city) {
        if (this.location == null) {
            this.location = new Location(city, "America");
        } else {
            this.location.setCityName(city);
        }
    }

    public void setLocationCountry(String country) {
        if (this.location == null) {
            this.location = new Location("San Jose", country);
        } else {
            this.location.setCountryName(country);
        }
    }

    public void setPrice(int price) {
        if (price < 1 || price > 5) {
            throw new IllegalArgumentException("Price must be between 1 and 5.");
        }
        this.price = price;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /** Hash */
    @Override
    public int hashCode() {
        return title.hashCode();
    }

    /**
     * Returns a string representation of the TravelBlog in the format:
     * <pre>
     * title | author | datePublished | location | price | content
     * </pre>
     * 
     * @return A string representation of this TravelBlog
     */
    @Override
    public String toString() {
        return title + " | " + author + " | " + datePublished + " | " + location + " | " + price + " | " + content;
    }


    /**
     * Returns a pretty string representation of the travel blog.
     * 
     * @return A formatted string with title, author, date published, location,
     *         price, and content.
     */
    public String toPrettyString() {
        return String.format("%s%nAuthor: %s%nDate Published: %s%nLocation: %s%nPrice: %d%n%n%s%n", title, author, datePublished, location, price, content);
    }
}

class Location {
    /** Variables **/
    private String cityName;
    private String countryName;

    /** Constructors **/
    public Location() {
        this.cityName = "";
        this.countryName = "";
    }

    public Location(String cityName, String countryName) {
        this.cityName = cityName;
        this.countryName = countryName;
    }

    /** Accessors **/
    public String getCityName() {
        return cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    /** Setters **/
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return cityName + ", " + countryName;
    }

}
