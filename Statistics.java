import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/** Class for Travel Blog Statistics 
 * @author Seyoung Choe
 */
public class Statistics {    

       /**
     * 
     * @param blogTable the hash table containing TravelBlog entries
     */
    public static void printAllStatistics(HashTable<TravelBlog> blogTable) {
        ArrayList<TravelBlog> blogs = blogTable.getAllElements();

        if (blogs.isEmpty()) {
            System.out.println("No blogs in the table.");
            return;
        }

        int[] priceCounts = new int[6]; // Index 1–5 used
        int totalChars = 0;
        TravelBlog longestBlog = blogs.get(0);
        TravelBlog maxPriceLongestBlog = null;
        HashMap<String, Integer> countryCounts = new HashMap<>();
        double totalPrice = 0;

        for (TravelBlog blog : blogs) {
            int price = blog.getPrice();
            if (price >= 1 && price <= 5) {
                priceCounts[price]++;
            }

            totalPrice += price;

            int contentLength = blog.getContent().length();
            totalChars += contentLength;

            if (contentLength > longestBlog.getContent().length()) {
                longestBlog = blog;
            }

            if (price == 5) {
                if (maxPriceLongestBlog == null || contentLength > maxPriceLongestBlog.getContent().length()) {
                    maxPriceLongestBlog = blog;
                }
            }

            String country = blog.getLocationCountry();
            countryCounts.put(country, countryCounts.getOrDefault(country, 0) + 1);
        }

        DecimalFormat df = new DecimalFormat("#.##");

        System.out.println("\nTotal number of blogs: " + blogs.size());

        System.out.println("\nBlogs by price level:");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Level " + i + ": " + priceCounts[i] + " blogs");
        }

        double avgPrice = totalPrice / blogs.size();
        System.out.println("\nAverage price level: " + df.format(avgPrice));

        System.out.println("\nNumber of blogs per country:");
        for (String country : countryCounts.keySet()) {
            System.out.println(country + ": " + countryCounts.get(country) + " blogs");
        }

        double avgLength = (double) totalChars / blogs.size();
        System.out.println("\nAverage blog content length: " + df.format(avgLength) + " characters");

        System.out.println("\nLongest blog by content:");
        System.out.println("Title: " + longestBlog.getTitle());
        System.out.println("Author: " + longestBlog.getAuthor());
        System.out.println("Length: " + longestBlog.getContent().length() + " characters");

        if (maxPriceLongestBlog != null) {
            System.out.println("\nLongest blog with price level 5:");
            System.out.println("Title: " + maxPriceLongestBlog.getTitle());
            System.out.println("Author: " + maxPriceLongestBlog.getAuthor());
            System.out.println("Length: " + maxPriceLongestBlog.getContent().length() + " characters");
        } else {
            System.out.println("\nNo blogs found with price level 5.");
        }

        System.out.println("\nHash table load factor: " + df.format(blogTable.getLoadFactor() * 100) + "%");

        int maxCount = 0;
        ArrayList<String> mostMentionedCountries = new ArrayList<>();
        for (String country : countryCounts.keySet()) {
            int count = countryCounts.get(country);
            if (count > maxCount) {
                maxCount = count;
                mostMentionedCountries.clear();
                mostMentionedCountries.add(country);
            } else if (count == maxCount) {
                mostMentionedCountries.add(country);
            }
        }

        System.out.print("\nMost frequently mentioned country: ");
        if (mostMentionedCountries.size() == 1) {
            System.out.println(mostMentionedCountries.get(0) + " (" + maxCount + " blogs)");
        } else {
            System.out.println(String.join(", ", mostMentionedCountries) + " (" + maxCount + " blogs each)");
        }
    }
}
