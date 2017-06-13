package tps.tp4;

public class RockPlace extends OccupiedPlace{
	
	public static int JOGADAS_RESTANTES = 4;

	public RockPlace(int x, int y) {
		super(x, y);
	}
	
	public int jogadasRestantes = JOGADAS_RESTANTES;
	
	public String toString(){
		String myString = "";
		
		myString = myString + "RockPlace: X = " + this.getXBoard() + "    Y = " + this.getYBoard() + "   restante = " + this.jogadasRestantes;
		
		return myString;
	}
}