package nypdproject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * this is the main class. this class reads the file, stores the entries in the file, interacts with 
 * the user, the generate the report.
 * @author baixiao
 * @version Dec. 8, 2017
 */
public class CollisionInfo {
	
	/**
	 * Splits the given line of a CSV file according to commas and double quotes
	 * (double quotes are used to surround multi-word entries so that they may contain commas)
	 * @param textLine	a line of text to be passed
	 * @return an Arraylist object containing all individual entries found on that line
	 */
	public static ArrayList<String> splitCSVLine(String textLine){
		
		ArrayList<String> entries = new ArrayList<String>(); 
		int lineLength = textLine.length(); 
		StringBuffer nextWord = new StringBuffer(); 
		char nextChar; 
		boolean insideQuotes = false; 
		boolean insideEntry= false;
		
		// iterate over all characters in the textLine
		for (int i = 0; i < lineLength; i++) {
			nextChar = textLine.charAt(i);
			
			// handle smart quotes as well as regular quotes
			if (nextChar == '"' || nextChar == '\u201C' || nextChar =='\u201D') {
					
				// change insideQuotes flag when nextChar is a quote
				if (insideQuotes) {
					insideQuotes = false; 
					insideEntry = false;
				}else {
					insideQuotes = true; 
					insideEntry = true;
				}
			} else if (Character.isWhitespace(nextChar)) {
				if ( insideQuotes || insideEntry ) {
				// add it to the current entry 
					nextWord.append( nextChar );
				}else { // skip all spaces between entries
					continue; 
				}
			} else if ( nextChar == ',') {
				if (insideQuotes){ // comma inside an entry
					nextWord.append(nextChar); 
				} else { // end of entry found
					insideEntry = false;
					entries.add(nextWord.toString());
					nextWord = new StringBuffer();
				}
			} else {
				// add all other characters to the nextWord
				nextWord.append(nextChar);
				insideEntry = true;
			} 
			
		}
		// add the last word ( assuming not empty ) 
		// trim the white space before adding to the list 
		if (!nextWord.toString().equals("")) {
			entries.add(nextWord.toString().trim());
		}

		return entries;
	}
	
	
	/**
	 * validate the specified zip code
	 * @param zip    String type
	 * @return true if the zip code is valid; otherwise false
	 */
	public static boolean validZip(String zip) {
		for (int i=0; i<zip.length(); i++) {
			if (!Character.isDigit(zip.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	//possibility to throw an IllegalArgumentException if Date objects cannot be created with the given strings
	/**
	 * validate that the starting date comes before(or equals) the ending date.
	 * @param beginDate    Date type
	 * @param endDate      Date type
	 * @return true if the starting date indeed comes before the ending date; otherwise false
	 */
	public static boolean validDateOrder(Date beginDate, Date endDate) {
		if (beginDate.compareTo(endDate)>0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * validate that the specified date is the valid
	 * @param date    String type
	 * @return true if the specified date indeed is valid; otherwise false
	 */
	public static boolean validDate(String date) {
		try {
			Date test = new Date(date);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	/**
	 * the main function. this class reads the file, stores the entries in the file, interacts with 
	 * the user, the generate the report.
	 * @param args     the command line argument specified by the user
	 * @throws FileNotFoundException  if the file user passed in cannot be found
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		if (args.length<1) {
			System.err.println("Error: the name of the input file is missing!\n");
			System.exit(1);
		}
		File inputFile = new File(args[0]);
		if (!inputFile.exists()) {
			System.err.println("Error: "+args[0]+" does not exist.\n");
			System.exit(1);
		}
		if (!inputFile.canRead()) {
			System.err.println("Error: "+args[0]+" cannot be read.\n");
		}
		Scanner readRecords = new Scanner(inputFile);
		CollisionsData avlTree = new CollisionsData();
		
		System.out.println("Begin reading");
		readRecords.nextLine();
		while (readRecords.hasNext()) {
			String line = readRecords.nextLine();
			ArrayList<String> allEntries = splitCSVLine(line);
			if (allEntries.size()<24) System.err.println("Invalid line!");

			try {
				Collision oneRecord = new Collision(allEntries);
				avlTree.add(oneRecord);
			} catch (IllegalArgumentException e) {
			}
			
		}
		readRecords.close();
		
		/*
		//Debug
		CollisionsData avlTree = new CollisionsData();
		Collision one = new Collision("10460","11/1/2017","3781550",0);
		avlTree.add(one);
		Collision two = new Collision("11413","11/1/2017","3781519",1);
		avlTree.add(two);
		Collision three = new Collision("10460","11/2/2017","3781553", 0);
		avlTree.add(three);
		//Debug end
		*/
		
		//start interacting with the user!
		//create Scanner object for user input
		Scanner user = new Scanner(System.in);
		
		System.out.print("Enter a zip code (or 'quit' to exit): ");
		String userInput = user.next();
		
		while (!userInput.equalsIgnoreCase("quit")) {
			//**validate the user inputs!!!
			while (userInput.length()!=5 || !validZip(userInput)) {
				System.out.println("Sorry, this is not a valid zip code.");
				System.out.print("Please enter another valid zip code(five digits): ");
				userInput = user.nextLine();        //***need to "clear-up" first here??
				userInput = user.next();
				if (userInput.equalsIgnoreCase("quit")) {
					System.out.println("Have a great Christmas!");
					System.exit(0);
				}
			}
			String userZip = userInput;        //valid zip that user entered
			
			
			//next, prompt the user to input the starting date
			System.out.print("Enter a start date (MM/DD/YYYY): ");
			userInput = user.next();
			while (!validDate(userInput)) {
				System.out.println("Sorry, this is not a valid starting date.");
				System.out.print("Please enter another valid starting date (MM/DD/YYYY): ");
				userInput = user.nextLine();         //"clear-up" previous inputs first
				userInput = user.next();
			}
			Date startingDate = new Date(userInput);
			
			System.out.print("Enter an end date (MM/DD/YYYY): ");
			userInput = user.next();
			while (!validDate(userInput)) {
				System.out.println("Sorry, this is not a valid ending date.");
				System.out.println("Please enter another valid ending date (MM/DD/YYYY): ");
				userInput = user.nextLine();
				userInput = user.next();
			}
			Date endingDate = new Date(userInput);
			
			//then check whether the starting date precedes the ending date; if not, prompt the user to enter the ending date again
			if (!validDateOrder(startingDate, endingDate)) {
				while (!validDateOrder(startingDate, endingDate)) {
					System.out.println("Sorry, the start date cannot comes before the end date!");
					System.out.print("Please enter an end date again (MM/DD/YYYY): ");
					userInput = user.next();
					while (!validDate(userInput)) {
						System.out.println("Sorry, this is not a valid ending date.");
						System.out.println("Please enter another valid ending date (MM/DD/YYYY): ");
						userInput = user.nextLine();
						userInput = user.next();            
					}
					endingDate = new Date(userInput);
				}
			}
			
			
			String report = avlTree.getReport(userZip, startingDate, endingDate);
			System.out.println();
			System.out.println(report);
			
			System.out.println();
			System.out.print("Enter a zip code (or 'quit' to exit): ");
			userInput = user.next();
		
		}
		user.close();
		System.out.println("Have a great day!");
		System.exit(1);
	}

}
