package edu.gatech.mass;

public class Fact {
	
	private String type;	// category of fact (either "preventative", "scientific", or "fun")
	private String content;	// fact content to be displayed to the user occasionally

	/**
	 * Initializes instance of Provider.
	 *
	 * * @param content
	 * 		The fact's content to be displayed to the user occasionally
	 */
	public Fact(String content) {
		type = "fun";
		this.content = content;
	}
	
	/**
	 * Initializes instance of Fact.
	 *
	 * * @param type
	 * 		The category of fact (either "preventative", "scientific", or "fun")
	 * * @param content
	 * 		The fact's content to be displayed to the user occasionally
	 */
	public Fact(String type, String content) {
		if (!(type.equals("preventative") || type.equals("scientific"))) {
			this.type = "fun";
		}
		else {
			this.type = type;
		}
		
		this.content = content;
	}
	
	public String getType() {
		return type;
	}
	
	public String getContent() {
		return content;
	}
}