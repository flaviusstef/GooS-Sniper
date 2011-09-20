package ro.flaviusstef.goos;

public class Item {
	public final String identifier;
	public final int stopPrice;
	
	public Item(String identifier, int stopPrice) {
		this.identifier = identifier;
		this.stopPrice = stopPrice;
	}
	
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Item)) 
			return false;
		
		return this.identifier.equals(((Item)other).identifier) && 
		       this.stopPrice == ((Item) other).stopPrice;
	}
	
	public String toString() {
		return identifier + " stop price: " + stopPrice;
	}
	
	public boolean allowsBid(int bid) {
		return bid <= stopPrice;
	}
	
	// TODO: care era faza cu hashCode again?
	public int hashCode() {
		return 11;
	}
}
