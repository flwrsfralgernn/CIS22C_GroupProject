import java.util.*;
import java.io.*;

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
        }
        scanner.close();
    }

    /**
     * Creates a hash table of words from the travel blogs, excluding stop words.
     */
    public void createHashTable() {
        for (TravelBlog blog : blogList) {
            String allWords = blog.toString();
            allWords = removeNonAlphaNumeric(allWords);

            String[] words = allWords.split("\\s+");
            for (String word : words) {
                if (!STOP_WORDS.contains(word.toLowerCase())) {
                    WordId id = new WordId(word);
                    id.assignId();
                    if (!wordIdTable.contains(id)) {
                        wordIdTable.add(id);
                        wordIdList.add(id);
                    }
                }
            }
        }
    }

    /**
     * Creates a list of binary search trees (BSTs) for each word, containing blogs that mention the word.
     */
    public void createBSTList() {
        bstArray = new ArrayList<>(wordIdTable.getNumElements());

        for (WordId wordId : wordIdList) {
            BST<TravelBlog> bst = new BST<>();
            for (TravelBlog blog : blogList) {
                if (blog.toString().toLowerCase().contains(wordId.getWord().toLowerCase())) {
                    bst.insert(blog, new TitleComparator());
                }
            }
            bstArray.set(wordId.getId(), bst);
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

            if (STOP_WORDS.contains(word.toLowerCase())) {
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
                        if (blog.toString().toLowerCase().contains(word)) {
                            resultBST.insert(blog, titleComparator);
                        }
                        blogs.advanceIterator();
                    }
                }
            }
        }
        return resultBST;
    }
}

