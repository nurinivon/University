/*
 * Coordinates object holds a pair of integers
 */
public class Coordinates {
	//the Coordinates object's attributes
	private int i;
	private int j;
	
	//constructor allowed only with two integers
	public Coordinates(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	//attributes' getters
	public int getI() {
		return this.i;
	}
	
	public int getJ() {
		return this.j;
	}
}
