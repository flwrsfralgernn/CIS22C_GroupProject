import java.text.DecimalFormat;
import java.util.ArrayList;

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

    // TODO: Implement other statistics methods as needed
}
