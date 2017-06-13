package tps.tp4;

public class EmptyPlace extends Place {

	public EmptyPlace(int x, int y) {
		super(x, y);
	}
	
	public String toString(){
		String myString = "";
		myString = myString + "X = " + this.getXBoard() + "     Y = " + this.getYBoard(); 
		return myString;
	}
}