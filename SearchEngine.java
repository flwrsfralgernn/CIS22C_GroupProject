import java.util.*;
import java.io.*;

/** 
 * Class for Search Engine that manages travel blogs and allows searching through them.
 * @author Prajwal Agrawao
 * @author Rahul John
 */
public class SearchEngine {

    // List of common stop words to ignore in search queries
    private static final ArrayList<String> STOP_WORDS = new ArrayList<>(Arrays.asList(
            "a", "an", "the", "and", "is", "in", "on", "at", "by", "from", "with", "as", "of", "for", "to", "this",
            "that", "these", "those", "i", "you", "he", "she", "it", "we", "they", "me", "us", "my", "your", "his",
            "her", "its", "our", "their"));

    /**
     * Removes non-alphanumeric characters from a string.
     *
     * @param s the input string
     * @return the cleaned string with only alphanumeric characters and spaces
     */
    private static String removeNonAlphaNumeric(String s) {
        s = s.replaceAll("[—–-]", " ");
        return s.replaceAll("[^a-zA-Z0-9 ]", "");
    }

    private ArrayList<BST<TravelBlog>> bstArray = new ArrayList<>();
    private HashTable<WordId> wordIdTable = new HashTable<>(1000);
    private ArrayList<TravelBlog> blogList = new ArrayList<>();
    private ArrayList<WordId> wordIdList = new ArrayList<>();
    
    /**
     * Comparator for comparing TravelBlogs based on their titles.
     */
    private class TitleComparator implements Comparator<TravelBlog> {
        @Override
        public int compare(TravelBlog t1, TravelBlog t2) {
            return t1.getTitle().compareTo(t2.getTitle());
        }
    }

    /**
     * Reads travel blogs from a file and stores them in a list.
     *
     * @param fileName the name of the file containing travel blog data
     * @throws IOException if an I/O error occurs
     * @postcondition Search engine attributes are updated according to blog
     */
    public void readBlogs(String fileName) throws IOException {
        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] fields = line.split("\\|");
            String title = fields[0];
            String author = fields[1];
            String date = fields[2];
            String city = fields[3];
            String country = fields[4];
            int price = Integer.parseInt(fields[5]);
            String content = fields[6];

            TravelBlog blog = new TravelBlog(title, author, date, city, country, price, content);
            blogList.add(blog);
            add(blog);
        }
        scanner.close();
    }

    /**
     * Adds a blog to the search engine
     * @param blog to be added
     * @postcondition blog is added from search engine attributes
     */
    public void add(TravelBlog blog) {
        TitleComparator titleComparator = new TitleComparator();
        String allWords = blog.toString();
        allWords = removeNonAlphaNumeric(allWords);

        String[] words = allWords.split("\\s+");
        for (String word : words) {
            word = word.toLowerCase();
            if (!STOP_WORDS.contains(word)) {
                WordId id = new WordId(word);
                if (!wordIdTable.contains(id)) {
                    id.assignId();
                    wordIdTable.add(id);
                    wordIdList.add(id);
                    //creates word array
                    bstArray.add(new BST<>());

                }
                //insert blog into word bst
                else {
                    int index = wordIdTable.get(id).getId();
                    bstArray.get(index).insert(blog, titleComparator);
                }
            }
        }
    }
    
    /**
     * Deletes a blog from the search engine
     * @param blog to be deleted
     * @postcondition blog is removed from search engine, nothing changes if blog does not exist.
     */
    public void remove(TravelBlog blog) {
    	TitleComparator titleComparator = new TitleComparator();
    	blogList.remove(blog); //removes from list of blogs.
    	
    	// loops through each word and removes blog (Remove does not affect array if element not present).
    	for (int i = 0; i < bstArray.size(); i++) {
    		bstArray.get(i).remove(blog, titleComparator); 
    	}	
    }

    /**
     * Searches for blogs containing any of the words in the query.
     *
     * @param query the array of words representing the search query
     * @return a BST containing the TravelBlogs that match the query
     */
    public BST<TravelBlog> search(String[] query) {
        TitleComparator titleComparator = new TitleComparator();
        BST<TravelBlog> resultBST = new BST<>();

        for (String word : query) {
            word = removeNonAlphaNumeric(word).toLowerCase();

            if (STOP_WORDS.contains(word)) {
                continue; // Skip stop words
            }

            WordId queryWordId = new WordId(word);
            if (wordIdTable.contains(queryWordId)) {
                int id = wordIdTable.get(queryWordId).getId();
                if (id < bstArray.size()) {
                    LinkedList<TravelBlog> blogs = bstArray.get(id).toLinkedList();
                    blogs.positionIterator();
                    while (!blogs.offEnd()) {
                        TravelBlog blog = blogs.getIterator();
                        resultBST.insert(blog, titleComparator);
                        blogs.advanceIterator();
                    }
                }
            }
        }
        return resultBST;
    }

    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();
        try {
            searchEngine.readBlogs("Input.txt");
            //searchEngine.createHashTable();

            String[] query = {"Hiroshima", "kyoto", "paris", "lyon"};
            
            //searchEngine.remove(new TravelBlog("Art Cities Beyond Paris: Lyon's Murals and Marseille's Street Art","","",new Location(),5));
            searchEngine.add(new TravelBlog("Abcd Paris test article","Paris","",new Location(),5));
            
            BST<TravelBlog> results = searchEngine.search(query);
            System.out.println("Search Results:");
            LinkedList<TravelBlog> resultList = results.toLinkedList();
            resultList.positionIterator();
            int idx = 1;
            while (!resultList.offEnd()) {
                TravelBlog blog = resultList.getIterator();
                System.out.println(idx + ". " + blog.getTitle());
                idx++;
                resultList.advanceIterator();
            }
        } catch (IOException e) {
            System.err.println("Error reading blogs: " + e.getMessage());
        }
    }
}

