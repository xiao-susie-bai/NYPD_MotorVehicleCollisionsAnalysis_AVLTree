package nypdproject;
import java.util.ArrayList;

/**
 * This class creates a collision object with all the information associated with the collision.
 * 
 * @author baixiao
 * @version Dec. 7th, 2017
 */
public class Collision implements Comparable<Collision> {
	private Date date;
	private String zip;
	private String key;
	private int deathNum;
	private int injuryNum;
	private int pedKilled;
	private int pedInjured;
	private int cycKilled;
	private int cycInjured;
	private int motorKilled;
	private int motorInjured;

	
	/**
	 * This is the default constructor of the Collision class
	 * @param entries   ArrayList<String> type object with all the information represented as String associated with the collision.
	 * @throws IllegalArgumentException when the collision's zip code, date, and key are not valid
	 */
	public Collision(ArrayList<String> entries) throws IllegalArgumentException {
		//debug the following line:correct
		if (entries == null) throw new IllegalArgumentException("This is a null ArrayList parameter!");
		if (entries.size()<24) throw new IllegalArgumentException("This is not a valid ArrayList!");
		//validate the Date
		if (entries.get(0).length()==0) throw new IllegalArgumentException("Oops! This is not a valid date!");
		try {
			this.date = new Date(entries.get(0));       //QUESTION: do we have to validate the format of the string representing a Date here(eg.:"mm/dd/yyyy")??
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Oops! This is not a valid Date!");
		}

		//validate the zip code
		if (entries.get(3).length() != 5) throw new IllegalArgumentException("Oops! This is not a valid zip code!");
		if (entries.get(3).length() == 5) {
			for (int i=0; i<entries.get(3).length(); i++) {
				if (!Character.isDigit(entries.get(3).charAt(i))) {
					throw new IllegalArgumentException("Oops! This is not a valid zip code!");
				}
			}
		}
		this.zip = entries.get(3);
		
		//validate the unique key
		if (entries.get(23).length()==0) throw new IllegalArgumentException("Oops! This is not a valid key!");
		this.key = entries.get(23);
		
		//"11": persons killed; "10": persons injured; "13": pedestrians killed; "12": pedestrians injured; 
		//"15": cyclist killed; "14": cyclist injured; "17": motorist killed; "16": motorist injured
		int intTotalInjury = Integer.parseInt(entries.get(10));
		int intTotalDeath = Integer.parseInt(entries.get(11));
		int intPedInjured = Integer.parseInt(entries.get(12));
		int intPedKilled = Integer.parseInt(entries.get(13));
		int intCycInjured = Integer.parseInt(entries.get(14));
		int intCycKilled = Integer.parseInt(entries.get(15));
		int intMotorInjured = Integer.parseInt(entries.get(16));
		int intMotorKilled = Integer.parseInt(entries.get(17));
		//validate the number integers
		if (intTotalInjury<0 || intTotalDeath<0 || intPedInjured<0 || intPedKilled<0) throw new IllegalArgumentException("Oops! The number of injuries and fatalities cannot be negative!");
		if (intCycInjured<0 || intCycKilled<0 || intMotorInjured<0 || intMotorKilled<0) throw new IllegalArgumentException("Oops! The number of injuries and fatalities cannot be negative!");
		
		
		this.deathNum = intTotalDeath;
		this.injuryNum = intTotalInjury;
		this.pedKilled = intPedKilled;
		this.pedInjured = intPedInjured;
		this.cycKilled = intCycKilled;
		this.cycInjured = intCycInjured;
		this.motorKilled = intMotorKilled;
		this.motorInjured = intMotorInjured;
		
	}
	
	/**
	 * getter of the date of the collision
	 * @return this.date    Date object
	 */     
	public Date getDate() {
		return this.date;
	}
	
	/**
	 * getter of the zip code of the collision
	 * @return this.zip    String type
	 */    
	public String getZip() {
		return this.zip;
	}
	
	/**
	 * getter of the key of the collision
	 * @return this.key    String type
	 */    
	public String getKey() {
		return this.key;
	}
	
	/**
	 * getter of the number of people injured in the collision
	 * @return this.injuryNum    int type
	 */    
	public int getPersonsInjured() {
		return this.injuryNum;
	}
	
	/**
	 * getter of the number of people killed in the collision
	 * @return this.deathNum    int type
	 */  
	public int getPersonsKilled() {
		return this.deathNum;
	}
	
	/**
	 * getter of the number of pedestrians injured in the collision
	 * @return this.pedInjured    int type
	 */   
	public int getPedestriansInjured() {
		return this.pedInjured;
	}
	
	/**
	 * getter of the number of pedestrians injured in the collision
	 * @return this.pedKilled    int type
	 */    
	public int getPedestriansKilled() {
		return this.pedKilled;
	}
	
	/**
	 * getter of the number of pedestrians killed in the collision
	 * @return this.cycInjured    int type
	 */    
	public int getCyclistsInjured() {
		return this.cycInjured;
	}
	
	/**
	 * getter of the number of cyclists injured in the collision
	 * @return this.cycKilled   int type
	 */    
	public int getCyclistsKilled() {
		return this.cycKilled;
	}
	
	/**
	 * getter of the number of cyclists killed in the collision
	 * @return this.motorInjured   int type
	 */   
	public int getMotoristsInjured() {
		return this.motorInjured;
	}
	
	/**
	 * getter of the number of motorists injured in the collision
	 * @return this.motorKilled    int type
	 */   
	public int getMotoristsKilled() {
		return this.motorKilled;
	}
	
	/**
	 * compareTo implements the required method in the interface. Compare two Collision object
	 * @param c      Collision type
	 * @return int shows the result of the comparison
	 * @throws IllegalArgumentException if the comparing object is null or not of Collision type
	 */
	public int compareTo(Collision c) {
		if (c==null) throw new IllegalArgumentException("The Collision object to be compared should not be null!");
		if (!(c instanceof Collision)) throw new IllegalArgumentException("The object to be compared should be a Collision object!");
		if (this.getZip().compareTo(c.getZip())!=0) {
			return (this.getZip().compareTo(c.getZip()));
		} else {
			if (this.getDate().compareTo(c.getDate())!=0) {
				return (this.getDate().compareTo(c.getDate()));
			} else {
				if (this.getKey().compareTo(c.getKey())!=0) {
					return (this.getKey().compareTo(c.getKey()));
				} else {
					return 0;
				}
			}
		}
	}
	
	/**
	 * Override the "equals()" method in Object class. Define "equality" between two object.
	 * @param o   Object type
	 * @return true or false
	 */
	public boolean equals(Object o) {
		if (o==null) return false;
		if (!(o instanceof Collision)) return false;
		if (this==o) return true;        //do not forget the scenario when "this Collision object" points to the same object as "o"!
		Collision another = (Collision) o;
		if (this.getZip().equals(another.getZip()) && this.getDate().equals(another.getDate()) && this.getKey().equals(another.getKey())) {     //*Do not forget to use "equals" ALWAYS when comparing String and other objects!!!
			return true;
		} else {
			return false;
		}
	}
	
	/*
	public static void main(String[] args) {
		System.out.println(Character.isDigit('0'));
		String num1 = "1234";
		String num2 = "4321";
		System.out.println(num1.compareTo(num2));
		
		ArrayList<String> simulate = new ArrayList<>(100);
		simulate.add("11/1/2017");
		simulate.add("0:00");
		simulate.add("BRONX");
		simulate.add("10460");
		simulate.add("40.835705");
		simulate.add("-73.88875");
		simulate.add("(40.835705, -73.88875)"); 
		simulate.add("Southern Boulevard");
		simulate.add("East 173 Street");
		simulate.add("");
		simulate.add("0");
		simulate.add("0");
		simulate.add("0");
		simulate.add("0");
		simulate.add("0");
		simulate.add("0");
		simulate.add("0");
		simulate.add("0");
		simulate.add("Following too closely");
		Collision test = new Collision(simulate);
		System.out.println(test.getDate().toString());
		//System.out.println(test.getKey());
		System.out.println(test.getZip());
		System.out.println(test.getCyclistsInjured());
	}
	*/
	
}
