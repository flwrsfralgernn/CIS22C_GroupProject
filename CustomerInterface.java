/**
 * CustomerInterface.java
 * @author Shivamsh Sthavara
 * CIS 22C, Course Project
 * Team Member 5: Screen Output Expert
 */

 import java.io.*;
 import java.util.*;
 
 public class CustomerInterface {
     private HashTable<TravelBlog> blogTable;
     private SearchEngine searchEngine;
     private Scanner scanner;
     private TitleComparator titleComparator;
     private PriceComparator priceComparator;
     private DateComparator dateComparator;
     private ArrayList<TravelBlog> allBlogs; 
     
     // Comparators for sorting
     private class TitleComparator implements Comparator<TravelBlog> {
         @Override
         public int compare(TravelBlog t1, TravelBlog t2) {
             return t1.getTitle().compareTo(t2.getTitle());
         }
     }
     
     private class PriceComparator implements Comparator<TravelBlog> {
         @Override
         public int compare(TravelBlog t1, TravelBlog t2) {
             return Integer.compare(t1.getPrice(), t2.getPrice());
         }
     }
     
     private class DateComparator implements Comparator<TravelBlog> {
         @Override
         public int compare(TravelBlog t1, TravelBlog t2) {
             // Compare years first
             int year1 = Integer.parseInt(t1.getYearPublished());
             int year2 = Integer.parseInt(t2.getYearPublished());
             if (year1 != year2) {
                 return Integer.compare(year2, year1); // Most recent first
             }
             
             // Then months
             int month1 = Integer.parseInt(t1.getMonthPublished());
             int month2 = Integer.parseInt(t2.getMonthPublished());
             if (month1 != month2) {
                 return Integer.compare(month2, month1);
             }
             
             // Then days
             int day1 = Integer.parseInt(t1.getDayPublished());
             int day2 = Integer.parseInt(t2.getDayPublished());
             return Integer.compare(day2, day1);
         }
     }
     
     /**
      * Constructor initializes the data structures and comparators
      */
     public CustomerInterface() {
         this.blogTable = new HashTable<>(100);
         this.searchEngine = new SearchEngine();
         this.scanner = new Scanner(System.in);
         this.titleComparator = new TitleComparator();
         this.priceComparator = new PriceComparator();
         this.dateComparator = new DateComparator();
         this.allBlogs = new ArrayList<>();
     }
     
     /**
      * Starts the Travel Blog Search Engine interface
      */
     public void start() {
         System.out.println("================================================");
         System.out.println("    Welcome to Travel Blog Search Engine!");
         System.out.println("    Your Gateway to Travel Inspiration");
         System.out.println("================================================\n");
         
         // Load initial data
         loadInitialData();
         
         // Main menu loop
         boolean running = true;
         while (running) {
             displayMainMenu();
             String choice = scanner.nextLine().trim().toUpperCase();
             
             switch (choice) {
                 case "A":
                     uploadNewRecord();
                     break;
                 case "B":
                     deleteRecord();
                     break;
                 case "C":
                     searchSubMenu();
                     break;
                 case "D":
                     modifyRecord();
                     break;
                 case "E":
                     displayStatistics();
                     break;
                 case "X":
                     running = false;
                     saveAndExit();
                     break;
                 default:
                     System.out.println("\nInvalid choice. Please try again.\n");
             }
         }
     }
     
     /**
      * Displays the main menu options
      */
     private void displayMainMenu() {
         System.out.println("\n========== MAIN MENU ==========");
         System.out.println("A. Upload a New Travel Blog");
         System.out.println("B. Delete a Travel Blog");
         System.out.println("C. Search for Travel Blogs");
         System.out.println("D. Modify/Update a Travel Blog");
         System.out.println("E. View Statistics");
         System.out.println("X. Save and Exit");
         System.out.println("==============================");
         System.out.print("Enter your choice: ");
     }
     
     /**
      * Loads initial data from Input.txt
      */
     private void loadInitialData() {
         try {
             // First load into search engine
             searchEngine.readBlogs("Input.txt");
             searchEngine.createHashTable();
             searchEngine.createBSTList();
             
             // Also load into our hash table and ArrayList for direct access
             Scanner fileScanner = new Scanner(new File("Input.txt"));
             while (fileScanner.hasNextLine()) {
                 String line = fileScanner.nextLine();
                 String[] fields = line.split("\\|");
                 if (fields.length >= 7) {
                     String title = fields[0];
                     String author = fields[1];
                     String date = fields[2];
                     String city = fields[3];
                     String country = fields[4];
                     int price = Integer.parseInt(fields[5]);
                     String content = fields[6];
                     
                     TravelBlog blog = new TravelBlog(title, author, date, city, country, price, content);
                     blogTable.add(blog);
                     allBlogs.add(blog);
                 }
             }
             fileScanner.close();
             
             System.out.println("Successfully loaded " + blogTable.getNumElements() + " travel blogs.\n");
             
         } catch (IOException e) {
             System.err.println("Warning: Could not load initial data from Input.txt");
             System.err.println("Starting with empty database.\n");
         }
     }
     
     /**
      * Option A: Upload a new travel blog record
      */
     private void uploadNewRecord() {
         System.out.println("\n========== UPLOAD NEW TRAVEL BLOG ==========");
         
         System.out.print("Enter blog title: ");
         String title = scanner.nextLine().trim();
         
         System.out.print("Enter author name: ");
         String author = scanner.nextLine().trim();
         
         System.out.print("Enter publication date (MM/DD/YYYY): ");
         String date = scanner.nextLine().trim();
         
         System.out.print("Enter city: ");
         String city = scanner.nextLine().trim();
         
         System.out.print("Enter country: ");
         String country = scanner.nextLine().trim();
         
         System.out.print("Enter price level (1-5, 1=cheapest, 5=most expensive): ");
         int price = 0;
         while (price < 1 || price > 5) {
             try {
                 price = Integer.parseInt(scanner.nextLine().trim());
                 if (price < 1 || price > 5) {
                     System.out.print("Please enter a number between 1 and 5: ");
                 }
             } catch (NumberFormatException e) {
                 System.out.print("Invalid input. Please enter a number between 1 and 5: ");
             }
         }
         
         System.out.println("Enter blog content (type 'END' on a new line to finish):");
         StringBuilder content = new StringBuilder();
         String line;
         while (!(line = scanner.nextLine()).equals("END")) {
             content.append(line).append(" ");
         }
         
         // Create and add the new blog
         TravelBlog newBlog = new TravelBlog(title, author, date, city, country, price, content.toString().trim());
         
         // Check if blog with same title+author already exists
         if (blogTable.contains(newBlog)) {
             System.out.println("\nA blog with this title and author already exists!");
         } else {
             blogTable.add(newBlog);
             allBlogs.add(newBlog);
             System.out.println("\nTravel blog successfully uploaded!");
             
             // Rebuild search engine indices
             rebuildSearchEngine();
         }
     }
     
     /**
      * Option B: Delete a travel blog record
      */
     private void deleteRecord() {
         System.out.println("\n========== DELETE TRAVEL BLOG ==========");
         
         System.out.print("Enter the exact title of the blog to delete: ");
         String title = scanner.nextLine().trim();
         
         System.out.print("Enter the author name: ");
         String author = scanner.nextLine().trim();
         
         // Create a temporary blog object for searching
         TravelBlog searchBlog = new TravelBlog();
         searchBlog.setTitle(title);
         searchBlog.setAuthor(author);
         
         // Find the actual blog in the table
         TravelBlog foundBlog = blogTable.get(searchBlog);
         
         if (foundBlog != null) {
             System.out.println("\nFound blog:");
             System.out.println("Title: " + foundBlog.getTitle());
             System.out.println("Author: " + foundBlog.getAuthor());
             System.out.println("Location: " + foundBlog.getLocationCity() + ", " + foundBlog.getLocationCountry());
             
             System.out.print("\nAre you sure you want to delete this blog? (Y/N): ");
             String confirm = scanner.nextLine().trim().toUpperCase();
             
             if (confirm.equals("Y")) {
                 if (blogTable.delete(foundBlog)) {
                     // Also remove from our ArrayList
                     for (int i = 0; i < allBlogs.size(); i++) {
                         TravelBlog blog = allBlogs.get(i);
                         if (blog.getTitle().equals(foundBlog.getTitle()) && 
                             blog.getAuthor().equals(foundBlog.getAuthor())) {
                             allBlogs.remove(i);
                             break;
                         }
                     }
                     System.out.println("\nBlog successfully deleted!");
                     rebuildSearchEngine();
                 } else {
                     System.out.println("\nError deleting blog.");
                 }
             } else {
                 System.out.println("\nDeletion cancelled.");
             }
         } else {
             System.out.println("\nBlog not found!");
         }
     }
     
     /**
      * Option C: Search submenu
      */
     private void searchSubMenu() {
         boolean inSearchMenu = true;
         
         while (inSearchMenu) {
             System.out.println("\n========== SEARCH MENU ==========");
             System.out.println("1. Find blog by title and author (primary key)");
             System.out.println("2. Search blogs by keywords");
             System.out.println("3. Return to main menu");
             System.out.println("=================================");
             System.out.print("Enter your choice: ");
             
             String choice = scanner.nextLine().trim();
             
             switch (choice) {
                 case "1":
                     searchByPrimaryKey();
                     break;
                 case "2":
                     searchByKeywords();
                     break;
                 case "3":
                     inSearchMenu = false;
                     break;
                 default:
                     System.out.println("\nInvalid choice. Please try again.");
             }
         }
     }
     
     /**
      * Search by primary key (title + author)
      */
     private void searchByPrimaryKey() {
         System.out.println("\n========== SEARCH BY TITLE AND AUTHOR ==========");
         
         System.out.print("Enter the exact title: ");
         String title = scanner.nextLine().trim();
         
         System.out.print("Enter the author name: ");
         String author = scanner.nextLine().trim();
         
         // Create a temporary blog object for searching
         TravelBlog searchBlog = new TravelBlog();
         searchBlog.setTitle(title);
         searchBlog.setAuthor(author);
         
         TravelBlog foundBlog = blogTable.get(searchBlog);
         
         if (foundBlog != null) {
             System.out.println("\nBlog found!");
             System.out.println(foundBlog.toPrettyString());
         } else {
             System.out.println("\nNo blog found with that title and author.");
         }
     }
     
     /**
      * Search by keywords using the search engine
      */
     private void searchByKeywords() {
         System.out.println("\n========== SEARCH BY KEYWORDS ==========");
         System.out.println("Enter keywords separated by spaces:");
         System.out.print("Keywords: ");
         
         String input = scanner.nextLine().trim();
         if (input.isEmpty()) {
             System.out.println("\nNo keywords entered.");
             return;
         }
         
         String[] keywords = input.split("\\s+");
         
         // Use the search engine to find matching blogs
         BST<TravelBlog> results = searchEngine.search(keywords);
         
         if (results.isEmpty()) {
             System.out.println("\nNo blogs found matching your keywords.");
         } else {
             LinkedList<TravelBlog> resultList = results.toLinkedList();
             System.out.println("\nFound " + resultList.getLength() + " matching blog(s):\n");
             
             resultList.positionIterator();
             int index = 1;
             
             // Display numbered list of results
             while (!resultList.offEnd()) {
                 TravelBlog blog = resultList.getIterator();
                 System.out.println(index + ". " + blog.getTitle() + " by " + blog.getAuthor());
                 System.out.println("   Location: " + blog.getLocationCity() + ", " + blog.getLocationCountry());
                 System.out.println("   Price Level: " + getPriceString(blog.getPrice()));
                 resultList.advanceIterator();
                 index++;
             }
             
             // Allow user to view full details
             System.out.print("\nEnter the number of a blog to view full details (or 0 to return): ");
             try {
                 int selection = Integer.parseInt(scanner.nextLine().trim());
                 if (selection > 0 && selection < index) {
                     resultList.advanceIteratorToIndex(selection - 1);
                     TravelBlog selectedBlog = resultList.getIterator();
                     System.out.println("\n" + selectedBlog.toPrettyString());
                 }
             } catch (NumberFormatException e) {
                 // User entered non-number, just return
             }
         }
     }
     
     /**
      * Option D: Modify/update a record
      */
     private void modifyRecord() {
         System.out.println("\n========== MODIFY TRAVEL BLOG ==========");
         
         // First find the blog
         System.out.print("Enter the exact title of the blog to modify: ");
         String title = scanner.nextLine().trim();
         
         System.out.print("Enter the author name: ");
         String author = scanner.nextLine().trim();
         
         TravelBlog searchBlog = new TravelBlog();
         searchBlog.setTitle(title);
         searchBlog.setAuthor(author);
         
         TravelBlog foundBlog = blogTable.get(searchBlog);
         
         if (foundBlog == null) {
             System.out.println("\nBlog not found!");
             return;
         }
         
         // Also find it in our ArrayList
         TravelBlog blogInList = null;
         for (TravelBlog blog : allBlogs) {
             if (blog.getTitle().equals(title) && blog.getAuthor().equals(author)) {
                 blogInList = blog;
                 break;
             }
         }
         
         System.out.println("\nBlog found! Current details:");
         System.out.println(foundBlog.toPrettyString());
         
         // Modification menu
         boolean modifying = true;
         while (modifying) {
             System.out.println("\nWhat would you like to modify?");
             System.out.println("1. Publication date");
             System.out.println("2. Location (city/country)");
             System.out.println("3. Price level");
             System.out.println("4. Content");
             System.out.println("5. Done with modifications");
             System.out.print("Enter your choice: ");
             
             String choice = scanner.nextLine().trim();
             
             switch (choice) {
                 case "1":
                     System.out.print("Enter new date (MM/DD/YYYY): ");
                     String newDate = scanner.nextLine().trim();
                     try {
                         foundBlog.setDatePublished(newDate);
                         if (blogInList != null) blogInList.setDatePublished(newDate);
                         System.out.println("Date updated!");
                     } catch (IllegalArgumentException e) {
                         System.out.println("Invalid date format!");
                     }
                     break;
                     
                 case "2":
                     System.out.print("Enter new city: ");
                     String newCity = scanner.nextLine().trim();
                     System.out.print("Enter new country: ");
                     String newCountry = scanner.nextLine().trim();
                     foundBlog.setLocation(newCity, newCountry);
                     if (blogInList != null) blogInList.setLocation(newCity, newCountry);
                     System.out.println("Location updated!");
                     break;
                     
                 case "3":
                     System.out.print("Enter new price level (1-5): ");
                     try {
                         int newPrice = Integer.parseInt(scanner.nextLine().trim());
                         foundBlog.setPrice(newPrice);
                         if (blogInList != null) blogInList.setPrice(newPrice);
                         System.out.println("Price level updated!");
                     } catch (Exception e) {
                         System.out.println("Invalid price level!");
                     }
                     break;
                     
                 case "4":
                     System.out.println("Enter new content (type 'END' on a new line to finish):");
                     StringBuilder newContent = new StringBuilder();
                     String line;
                     while (!(line = scanner.nextLine()).equals("END")) {
                         newContent.append(line).append(" ");
                     }
                     foundBlog.setContent(newContent.toString().trim());
                     if (blogInList != null) blogInList.setContent(newContent.toString().trim());
                     System.out.println("Content updated!");
                     break;
                     
                 case "5":
                     modifying = false;
                     System.out.println("\nModifications saved!");
                     rebuildSearchEngine();
                     break;
                     
                 default:
                     System.out.println("Invalid choice!");
             }
         }
     }
     
     /**
      * Option E: Display statistics
      */
     private void displayStatistics() {
         System.out.println("\n========== TRAVEL BLOG STATISTICS ==========");
         
         if (allBlogs.isEmpty()) {
             System.out.println("No blogs in the database!");
             return;
         }
         
         // Statistic 1: Total number of blogs
         System.out.println("\n1. Total Travel Blogs: " + allBlogs.size());
         
         // Statistic 2: Blogs by price level
         int[] priceCounts = new int[6]; // Index 0 unused, 1-5 for price levels
         double totalPrice = 0;
         
         // Statistic 3: Most popular destinations (countries)
         HashMap<String, Integer> countryCount = new HashMap<>();
         
         // Statistic 4: Most recent blog
         TravelBlog mostRecent = null;
         
         // Process all blogs for statistics
         for (TravelBlog blog : allBlogs) {
             // Update price statistics
             priceCounts[blog.getPrice()]++;
             totalPrice += blog.getPrice();
             
             // Update country statistics
             String country = blog.getLocationCountry();
             countryCount.put(country, countryCount.getOrDefault(country, 0) + 1);
             
             // Update most recent
             if (mostRecent == null || dateComparator.compare(blog, mostRecent) < 0) {
                 mostRecent = blog;
             }
         }
         
         // Display price distribution
         System.out.println("\n2. Blogs by Price Level:");
         for (int i = 1; i <= 5; i++) {
             System.out.println("   Level " + i + " (" + getPriceString(i) + "): " + 
                              priceCounts[i] + " blogs");
         }
         
         // Display average price
         double avgPrice = totalPrice / allBlogs.size();
         System.out.printf("\n3. Average Price Level: %.2f\n", avgPrice);
         
         // Display top 3 countries
         System.out.println("\n4. Top Destinations by Country:");
         ArrayList<Map.Entry<String, Integer>> countryList = new ArrayList<>(countryCount.entrySet());
         countryList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
         
         int count = 0;
         for (Map.Entry<String, Integer> entry : countryList) {
             System.out.println("   " + entry.getKey() + ": " + entry.getValue() + " blogs");
             count++;
             if (count >= 3) break;
         }
         
         // Display most recent blog
         if (mostRecent != null) {
             System.out.println("\n5. Most Recent Blog:");
             System.out.println("   \"" + mostRecent.getTitle() + "\" by " + 
                              mostRecent.getAuthor());
             System.out.println("   Published: " + mostRecent.getDatePublished());
         }
         
         // Display hash table load factor
         System.out.printf("\n6. Hash Table Load Factor: %.2f%%\n", 
                         blogTable.getLoadFactor() * 100);
     }
     
     /**
      * Option X: Save all records to a file and exit
      */
     private void saveAndExit() {
         System.out.println("\n========== SAVE AND EXIT ==========");
         System.out.print("Enter filename to save all blogs (e.g., output.txt): ");
         String filename = scanner.nextLine().trim();
         
         if (filename.isEmpty()) {
             filename = "output.txt";
         }
         
         try {
             PrintWriter writer = new PrintWriter(new FileWriter(filename));
             
             // Write all blogs in the same format as Input.txt
             for (TravelBlog blog : allBlogs) {
                 writer.println(blog.getTitle() + "|" + 
                              blog.getAuthor() + "|" + 
                              blog.getDatePublished() + "|" + 
                              blog.getLocationCity() + "|" + 
                              blog.getLocationCountry() + "|" + 
                              blog.getPrice() + "|" + 
                              blog.getContent());
             }
             
             writer.close();
             
             System.out.println("Successfully saved " + allBlogs.size() + " blogs to " + filename);
             System.out.println("\nThank you for using Travel Blog Search Engine!");
             System.out.println("Safe travels!");
             
         } catch (IOException e) {
             System.err.println("Error saving to file: " + e.getMessage());
             System.out.println("Exiting without saving.");
         }
         
         scanner.close();
     }
     
     /**
      * Rebuilds the search engine indices after modifications
      */
     private void rebuildSearchEngine() {
         // Write current blogs to temporary file and reload
         try {
             PrintWriter tempWriter = new PrintWriter(new FileWriter("temp_blogs.txt"));
             for (TravelBlog blog : allBlogs) {
                 tempWriter.println(blog.getTitle() + "|" + 
                                  blog.getAuthor() + "|" + 
                                  blog.getDatePublished() + "|" + 
                                  blog.getLocationCity() + "|" + 
                                  blog.getLocationCountry() + "|" + 
                                  blog.getPrice() + "|" + 
                                  blog.getContent());
             }
             tempWriter.close();
             
             // Reinitialize search engine with updated data
             searchEngine = new SearchEngine();
             searchEngine.readBlogs("temp_blogs.txt");
             searchEngine.createHashTable();
             searchEngine.createBSTList();
             
             // Delete temporary file
             new File("temp_blogs.txt").delete();
             
             System.out.println("Search indices updated.");
             
         } catch (IOException e) {
             System.err.println("Warning: Could not update search indices.");
         }
     }
     
     /**
      * Converts price level to descriptive string
      */
     private String getPriceString(int price) {
         switch (price) {
             case 1: return "Budget-friendly";
             case 2: return "Affordable";
             case 3: return "Moderate";
             case 4: return "Expensive";
             case 5: return "Luxury";
             default: return "Unknown";
         }
     }
     
     /**
      * Main method to run the Travel Blog Search Engine
      */
     public static void main(String[] args) {
         CustomerInterface ui = new CustomerInterface();
         ui.start();
     }
 }