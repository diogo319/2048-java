package tps.tp4;

public abstract class OccupiedPlace{

	private int xBoard;
	private int yBoard;
	
	public OccupiedPlace(int x, int y){
		this.xBoard = x;
		this.yBoard = y;
	}
	
	public void setXBoard(int newX){
		this.xBoard = newX;
	}
	
	public void setYBoard(int newY){
		this.yBoard = newY;
	}
	
	public int getXBoard(){
		return this.xBoard;
	}
	
	public int getYBoard(){
		return this.yBoard;
	}
}