
/**
 * CustomerInterface.java
 * @author Shivamsh Sthavara
 * CIS 22C, Course Project
 */
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerInterface {
    public static void main(String[] args) throws IOException {
        final int NUM_BLOGS = 100;

        // Use ArrayLists to store data
        ArrayList<TravelBlog> blogsList = new ArrayList<>();
        HashTable<TravelBlog> blogTable = new HashTable<>(NUM_BLOGS);
        SearchEngine searchEngine = new SearchEngine();

        Scanner keyboard = new Scanner(System.in);
        String title, author, date, city, country, content;
        int price;

        // Load travel blogs from file
        try {
            File file = new File("Input.txt");
            if (file.exists()) {
                Scanner input = new Scanner(file);
                while (input.hasNextLine()) {
                    String line = input.nextLine();
                    String[] fields = line.split("\\|");
                    if (fields.length >= 7) {
                        title = fields[0];
                        author = fields[1];
                        date = fields[2];
                        city = fields[3];
                        country = fields[4];
                        price = Integer.parseInt(fields[5]);
                        content = fields[6];

                        TravelBlog blog = new TravelBlog(title, author, date, city, country, price, content);
                        blogsList.add(blog);
                        blogTable.add(blog);
                    }
                }
                input.close();

                // Initialize search engine
                searchEngine.readBlogs("Input.txt");
            }
        } catch (Exception e) {
            // Continue without blog data if file issues
        }

        // Interactive menu
        System.out.println("Welcome to Travel Blog Search Engine!\n");

        String choice = "";

        while (!choice.equals("X") && !choice.equals("x")) {
            System.out.println("\nPlease select from the following options:\n");
            System.out.println("A. Upload a New Travel Blog");
            System.out.println("B. Delete a Travel Blog");
            System.out.println("C. Search for Travel Blogs");
            System.out.println("D. Modify/Update a Travel Blog");
            System.out.println("E. View Statistics");
            System.out.println("X. Save and Exit\n");
            System.out.print("Enter your choice: ");

            choice = keyboard.next();
            keyboard.nextLine(); // consume newline

            if (choice.equals("A") || choice.equals("a")) {
                // Upload a new blog
                System.out.println("\nUpload a New Travel Blog:\n");

                System.out.print("Enter blog title: ");
                title = keyboard.nextLine();

                System.out.print("Enter author name: ");
                author = keyboard.nextLine();

                System.out.print("Enter publication date (MM/DD/YYYY): ");
                date = keyboard.nextLine();

                System.out.print("Enter city: ");
                city = keyboard.nextLine();

                System.out.print("Enter country: ");
                country = keyboard.nextLine();

                System.out.print("Enter price level (1-5): ");
                price = keyboard.nextInt();
                keyboard.nextLine(); // consume newline

                System.out.println("Enter blog content (type 'END' on a new line to finish):");
                StringBuilder contentBuilder = new StringBuilder();
                String line;
                while (!(line = keyboard.nextLine()).equals("END")) {
                    contentBuilder.append(line).append(" ");
                }
                content = contentBuilder.toString().trim();

                TravelBlog newBlog = new TravelBlog(title, author, date, city, country, price, content);

                if (blogTable.contains(newBlog)) {
                    System.out.println("\nA blog with this title and author already exists.");
                } else {
                    blogTable.add(newBlog);
                    blogsList.add(newBlog);
                    System.out.println("\nBlog successfully uploaded!");

                    // Rebuild search engine
                    rebuildSearchEngine(searchEngine, blogsList);
                }

            } else if (choice.equals("B") || choice.equals("b")) {
                // Delete a blog
                System.out.println("\nDelete a Travel Blog:\n");

                System.out.print("Enter the exact title of the blog to delete: ");
                title = keyboard.nextLine();

                System.out.print("Enter the author name: ");
                author = keyboard.nextLine();

                TravelBlog tempBlog = new TravelBlog();
                tempBlog.setTitle(title);
                tempBlog.setAuthor(author);

                TravelBlog foundBlog = blogTable.get(tempBlog);

                if (foundBlog == null) {
                    System.out.println("\nBlog not found.");
                } else {
                    System.out.println("\nFound blog:");
                    System.out.println("Title: " + foundBlog.getTitle());
                    System.out.println("Author: " + foundBlog.getAuthor());
                    System.out.println(
                            "Location: " + foundBlog.getLocationCity() + ", " + foundBlog.getLocationCountry());

                    System.out.print("\nAre you sure you want to delete this blog? (Y/N): ");
                    String confirm = keyboard.next();

                    if (confirm.equals("Y") || confirm.equals("y")) {
                        blogTable.delete(foundBlog);
                        blogsList.remove(foundBlog);
                        System.out.println("\nBlog successfully deleted.");
                        rebuildSearchEngine(searchEngine, blogsList);
                    } else {
                        System.out.println("\nDeletion cancelled.");
                    }
                }

            } else if (choice.equals("C") || choice.equals("c")) {
                // Search for blogs
                System.out.println("\nSearch for Travel Blogs:\n");
                System.out.println("1. Find blog by title and author");
                System.out.println("2. Search blogs by keywords\n");
                System.out.print("Enter your choice (1 or 2): ");

                String searchChoice = keyboard.next();
                keyboard.nextLine(); // consume newline

                if (searchChoice.equals("1")) {
                    System.out.print("\nEnter the exact title: ");
                    title = keyboard.nextLine();

                    System.out.print("Enter the author name: ");
                    author = keyboard.nextLine();

                    TravelBlog tempBlog = new TravelBlog();
                    tempBlog.setTitle(title);
                    tempBlog.setAuthor(author);

                    TravelBlog foundBlog = blogTable.get(tempBlog);

                    if (foundBlog == null) {
                        System.out.println("\nNo blog found with that title and author.");
                    } else {
                        System.out.println("\n" + foundBlog.toPrettyString());
                    }

                } else if (searchChoice.equals("2")) {
                    System.out.print("\nEnter keywords separated by spaces: ");
                    String keywordInput = keyboard.nextLine();
                    String[] keywords = keywordInput.split("\\s+");

                    BST<TravelBlog> results = searchEngine.search(keywords);

                    if (results.isEmpty()) {
                        System.out.println("\nNo blogs found matching your keywords.");
                    } else {
                        LinkedList<TravelBlog> resultList = results.toLinkedList();
                        System.out.println("\nSearch Results:\n");

                        resultList.positionIterator();
                        int index = 1;
                        while (!resultList.offEnd()) {
                            TravelBlog blog = resultList.getIterator();
                            System.out.println(index + ". " + blog.getTitle());
                            System.out.println("   Author: " + blog.getAuthor());
                            System.out.println(
                                    "   Location: " + blog.getLocationCity() + ", " + blog.getLocationCountry());
                            resultList.advanceIterator();
                            index++;
                        }

                        System.out.print("\nEnter the number of a blog to view details (or 0 to return): ");
                        int selection = keyboard.nextInt();

                        if (selection > 0 && selection < index) {
                            resultList.positionIterator();
                            for (int i = 0; i < selection - 1; i++) {
                                resultList.advanceIterator();
                            }
                            TravelBlog selectedBlog = resultList.getIterator();
                            System.out.println("\n" + selectedBlog.toPrettyString());
                        }
                    }
                } else {
                    System.out.println("\nInvalid Choice!");
                }

            } else if (choice.equals("D") || choice.equals("d")) {
                // Modify a blog
                System.out.println("\nModify/Update a Travel Blog:\n");

                System.out.print("Enter the exact title of the blog to modify: ");
                title = keyboard.nextLine();

                System.out.print("Enter the author name: ");
                author = keyboard.nextLine();

                TravelBlog tempBlog = new TravelBlog();
                tempBlog.setTitle(title);
                tempBlog.setAuthor(author);

                TravelBlog foundBlog = findBlog(blogsList, tempBlog);

                if (foundBlog == null) {
                    System.out.println("\nBlog not found.");
                } else {
                    System.out.println("\nCurrent details:");
                    System.out.println(foundBlog.toPrettyString());

                    System.out.println("\nWhat would you like to modify?");
                    System.out.println("1. Publication date");
                    System.out.println("2. Location");
                    System.out.println("3. Price level");
                    System.out.println("4. Content\n");
                    System.out.print("Enter your choice (1-4): ");

                    String modifyChoice = keyboard.next();
                    keyboard.nextLine(); // consume newline

                    if (modifyChoice.equals("1")) {
                        System.out.print("Enter new date (MM/DD/YYYY): ");
                        String newDate = keyboard.nextLine();
                        foundBlog.setDatePublished(newDate);
                        System.out.println("\nDate updated successfully.");

                    } else if (modifyChoice.equals("2")) {
                        System.out.print("Enter new city: ");
                        String newCity = keyboard.nextLine();
                        System.out.print("Enter new country: ");
                        String newCountry = keyboard.nextLine();
                        foundBlog.setLocation(newCity, newCountry);
                        System.out.println("\nLocation updated successfully.");

                    } else if (modifyChoice.equals("3")) {
                        System.out.print("Enter new price level (1-5): ");
                        int newPrice = keyboard.nextInt();
                        foundBlog.setPrice(newPrice);
                        System.out.println("\nPrice level updated successfully.");

                    } else if (modifyChoice.equals("4")) {
                        System.out.println("Enter new content (type 'END' on a new line to finish):");
                        StringBuilder newContent = new StringBuilder();
                        String line;
                        while (!(line = keyboard.nextLine()).equals("END")) {
                            newContent.append(line).append(" ");
                        }
                        foundBlog.setContent(newContent.toString().trim());
                        System.out.println("\nContent updated successfully.");

                    } else {
                        System.out.println("\nInvalid choice.");
                    }

                    rebuildSearchEngine(searchEngine, blogsList);
                }

            } else if (choice.equals("E") || choice.equals("e")) {
                // Display statistics
                if (blogsList.isEmpty()) {
                    System.out.println("\nNo blogs in the database.");
                } else {
                    System.out.println("\nTravel Blog Statistics:\n");

                    // Total blogs
                    System.out.println("Total number of blogs: " + blogsList.size());

                    // Price distribution
                    int[] priceCounts = new int[6];
                    for (TravelBlog blog : blogsList) {
                        priceCounts[blog.getPrice()]++;
                    }

                    System.out.println("\nBlogs by price level:");
                    for (int i = 1; i <= 5; i++) {
                        System.out.println("Level " + i + ": " + priceCounts[i] + " blogs");
                    }

                    // Average price
                    double totalPrice = 0;
                    for (TravelBlog blog : blogsList) {
                        totalPrice += blog.getPrice();
                    }
                    double avgPrice = totalPrice / blogsList.size();
                    DecimalFormat df = new DecimalFormat("#.##");
                    System.out.println("\nAverage price level: " + df.format(avgPrice));

                    // Hash table load factor
                    System.out.println("\nHash table load factor: " +
                            df.format(blogTable.getLoadFactor() * 100) + "%");
                }

            } else if (!choice.equals("X") && !choice.equals("x")) {
                System.out.println("\nInvalid menu option. Please enter A-E or X to exit.");
            }
        }

        // Save and exit
        System.out.print("\nEnter filename to save all blogs: ");
        String filename = keyboard.nextLine();

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename));

            for (TravelBlog blog : blogsList) {
                writer.println(blog.getTitle() + "|" +
                        blog.getAuthor() + "|" +
                        blog.getDatePublished() + "|" +
                        blog.getLocationCity() + "|" +
                        blog.getLocationCountry() + "|" +
                        blog.getPrice() + "|" +
                        blog.getContent());
            }

            writer.close();
            System.out.println("\nSuccessfully saved " + blogsList.size() + " blogs to " + filename);

        } catch (IOException e) {
            System.err.println("\nError saving to file: " + e.getMessage());
        }

        System.out.println("\nGoodbye!");
        keyboard.close();
    }

    /**
     * Helper method to find a TravelBlog in ArrayList
     * 
     * @param blogsList the list of blogs
     * @param target    the blog to find
     * @return the found blog or null
     */
    private static TravelBlog findBlog(ArrayList<TravelBlog> blogsList, TravelBlog target) {
        for (TravelBlog blog : blogsList) {
            if (blog.getTitle().equals(target.getTitle()) &&
                    blog.getAuthor().equals(target.getAuthor())) {
                return blog;
            }
        }
        return null;
    }

    /**
     * Helper method to rebuild search engine indices
     * 
     * @param searchEngine the search engine instance
     * @param blogsList    the current list of blogs
     */
    private static void rebuildSearchEngine(SearchEngine searchEngine, ArrayList<TravelBlog> blogsList) {
        try {
            // Write to temporary file
            PrintWriter tempWriter = new PrintWriter(new FileWriter("temp_blogs.txt"));
            for (TravelBlog blog : blogsList) {
                tempWriter.println(blog.getTitle() + "|" +
                        blog.getAuthor() + "|" +
                        blog.getDatePublished() + "|" +
                        blog.getLocationCity() + "|" +
                        blog.getLocationCountry() + "|" +
                        blog.getPrice() + "|" +
                        blog.getContent());
            }
            tempWriter.close();

            // Reinitialize search engine
            searchEngine.readBlogs("temp_blogs.txt");

            // Delete temporary file
            new File("temp_blogs.txt").delete();

        } catch (IOException e) {
            // Continue without updating search engine
        }
    }

    
}