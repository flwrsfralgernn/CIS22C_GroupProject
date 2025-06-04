package lab;

public class WordId {
	private String word;
	private int id;
	private static int assignedID = 0; //tracks amount of created objects with assigned Ids. Incremented after new word is assigned Id.
	/**
	* Constructs new WordID object
	* @param String word that is stored in object
	* @postcondition new WordID object is created with attribute word. ID is -1 until assigned.
	*/
	public WordId(String word) {
		this.word = word;
		id = -1;
	}
	
	//Accessors
	 /**
	* Getter method for ID attribute
	* @return ID of word object, -1 if not assigned yet.
	*/
	public int getId() { //Use after getting wordId object from hashMap to get index in arraylist of bst.
		return id;
	}
	 /**
	* Getter method for Word attribute
	* @return word stored in object
	*/
	public String getWord() {
		return word;
	}
	
	//Mutators
	 /**
	* Assigns unique ID/index based on static int assignedID
	* @postcondition WordID object has new ID assigned to it
	*/
	public void assignId() {
		//The id of the word is its index in the BST arraylist. Do not use if word already has or had id.
		this.id = assignedID;
		assignedID++;
	}
	 /**
	  * returns hashCode of word attribute in object
	  * @return hashCode based on word
	  */
	public int hashCode() {
		return word.hashCode();
	}
   /**
    * Checks equality based on comparison of both objects' word attribute.
    * Can be used with word with no ID to search for original using wordID hashtable.
    * @param obj other object used in comparison
    * @return True if equal.
    */
	public boolean equals(Object obj) {
		   if (obj == this){ //same memory address case
			   return true;
		   }
		   else if (!(obj instanceof WordId)){ //check if obj is WordID object
			   return false;
		   }
		   //Main equality case. Compares words.
		   else{
		     WordId other = (WordId) obj;
		     return word.equals(other.word);
		   }
		   
	   }


}
