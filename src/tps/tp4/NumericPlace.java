package tps.tp4;

import java.awt.Color;

public class NumericPlace extends OccupiedPlace{
	
	public NumericPlace(int x, int y) {
		super(x, y);
		this.setValue();
	}
	
	public possibleValues numericValue;
	
	public enum possibleValues{
		_2{
			public Color color(){
				return new Color(238, 238, 218);
			}
		},
		_4{
			public Color color(){
				return new Color(236, 224, 200);
			}
		},
		_8{
			public Color color(){
				return new Color(243, 176, 121);
			}
		}, 
		_16{
			public Color color(){
				return new Color(246, 148, 99);
			}
		}, 
		_32{
			public Color color(){
				return new Color(245, 124, 95);
			}
		}, 
		_64{
			public Color color(){
				return new Color(246, 94, 57);
			}
		}, 
		_128{
			public Color color(){
				return new Color(237, 206, 113);
			}
		}, 
		_256{
			public Color color(){
				return new Color(236, 203, 96);
			}
		}, 
		_512{
			public Color color(){
				return new Color(238, 199, 80);
			}
		}, 
		_1024{
			public Color color(){
				return new Color(237, 197, 63);
			}
		}, 
		_2048{
			public Color color(){
				return new Color(238, 194, 46);
			}
		};
		public abstract Color color();
	}
	
	public void setValue(){
		if (this.numericValue == null){
			this.numericValue = possibleValues._2;
		}
		else{
			this.numericValue = possibleValues.values()[this.numericValue.ordinal() + 1];
		}
	}
	
	public String toString(){
		String myString = "";
		
		myString = myString + "NumericPlace: X = " + this.getXBoard() + "    Y = " + this.getYBoard() + "    Value = " + this.numericValue;
		
		return myString;
	}
}