package net.lukegjpotter.app.charliefoxtrot;

/**
 * @author Luke Potter
 * Date: 19/Mar/2013
 * 
 * Description:
 *     This class provides methods to take in a string and return it's
 *     phonetic representation.
 */
public class DeterminePhoneticRepresentation {

	/**
	 * Default Constructor
	 */
	public DeterminePhoneticRepresentation() { }
	
	/**
	 * This method parses the query and returns its phonetic representation.
	 * 
	 * @param query
	 * @return phoneticRepresentation
	 */
	public String parseString (String query) {
		
		query = query.toLowerCase();
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < query.length(); i++) {
			
			builder.append(parseLetter(query.charAt(i)));
			
			if (i < (query.length() - 1) && query.charAt(i) != ' ') builder.append(", ");
		}
		
		return builder.toString();
	}
	
	private String parseLetter (char letter) {
		
		switch (letter) {
			case 'a' : return "Alpha";
			case 'b' : return "Bravo";
			case 'c' : return "Charlie";
			case 'd' : return "Delta";
			case 'e' : return "Echo";
			case 'f' : return "Foxtrot";
			case 'g' : return "Golf";
			case 'h' : return "Hotel";
			case 'i' : return "India";
			case 'j' : return "Juliett";
			case 'k' : return "Kilo";
			case 'l' : return "Lima";
			case 'm' : return "Mike";
			case 'n' : return "November";
			case 'o' : return "Oscar";
			case 'p' : return "Papa";
			case 'q' : return "Quebec";
			case 'r' : return "Romeo";
			case 's' : return "Sierra";
			case 't' : return "Tango";
			case 'u' : return "Uniform";
			case 'v' : return "Victor";
			case 'w' : return "Whiskey";
			case 'x' : return "X-Ray";
			case 'y' : return "Yankee";
			case 'z' : return "Zulu";
			case '0' : return "Zero";
			case '1' : return "One";
			case '2' : return "Two";
			case '3' : return "Three";
			case '4' : return "Four";
			case '5' : return "Five";
			case '6' : return "Six";
			case '7' : return "Seven";
			case '8' : return "Eight";
			case '9' : return "Nine";
			default : return " ";
		}
	}

}
