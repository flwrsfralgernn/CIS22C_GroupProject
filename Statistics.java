import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/** Class for Travel Blog Statistics 
 * @author Seyoung (Selene) Choe
 */
public class Statistics {    

    /** 
     * Calculates and prints the average price level of all blogs.
     * @param blogsList The list of travel blogs
     */
    public static void printAveragePrice(ArrayList<TravelBlog> blogsList) {
        if (blogsList.isEmpty()) {
            System.out.println("No blogs available to calculate average price.");
            return;
        }

        double totalPrice = 0;
        for (TravelBlog blog : blogsList) {
            totalPrice += blog.getPrice();
        }

        double avgPrice = totalPrice / blogsList.size();
        DecimalFormat df = new DecimalFormat("#.##");

        System.out.println("Average price level: " + df.format(avgPrice));
    }

    /** 
     * Prints the number of blogs grouped by country.
     * @param blogsList The list of travel blogs
     */
    public static void printBlogCountByCountry(ArrayList<TravelBlog> blogsList) {
        if (blogsList.isEmpty()) {
            System.out.println("No blogs available to count by country.");
            return;
        }

        HashMap<String, Integer> countryCounts = new HashMap<>();

        for (TravelBlog blog : blogsList) {
            String country = blog.getLocationCountry();
            countryCounts.put(country, countryCounts.getOrDefault(country, 0) + 1);
        }

        System.out.println("\nNumber of blogs per country:");
        for (String country : countryCounts.keySet()) {
            System.out.println(country + ": " + countryCounts.get(country) + " blogs");
        }
    }

    /** 
     * Prints the country or countries with the highest number of blogs.
     * @param blogsList The list of travel blogs
     */
    public static void printMostFrequentCountry(ArrayList<TravelBlog> blogsList) {
        if (blogsList.isEmpty()) {
            System.out.println("No blogs available to analyze country frequency.");
            return;
        }

        HashMap<String, Integer> countryCounts = new HashMap<>();

        for (TravelBlog blog : blogsList) {
            String country = blog.getLocationCountry();
            countryCounts.put(country, countryCounts.getOrDefault(country, 0) + 1);
        }

        int maxCount = 0;
        ArrayList<String> mostMentioned = new ArrayList<>();

        for (String country : countryCounts.keySet()) {
            int count = countryCounts.get(country);
            if (count > maxCount) {
                maxCount = count;
                mostMentioned.clear();
                mostMentioned.add(country);
            } else if (count == maxCount) {
                mostMentioned.add(country);
            }
        }

        System.out.print("\nMost frequently mentioned country: ");
        if (mostMentioned.size() == 1) {
            System.out.println(mostMentioned.get(0) + " (" + maxCount + " blogs)");
        } else {
            System.out.println(String.join(", ", mostMentioned) + " (" + maxCount + " blogs each)");
        }
    }
}
