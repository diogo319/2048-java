package tps.tp4;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Board{
	
	public static Collection<EmptyPlace> collectionEmpty = new ArrayList<EmptyPlace>();
	
	public static Collection<RockPlace> collectionRock = new ArrayList<RockPlace>();
	
	public static Collection<NumericPlace> collectionNumeric = new ArrayList<NumericPlace>();
	
	public int size;
	
	public JPanel grid = new JPanel();
	
	public static boolean MOV = false;
	
	public Board(int size){
		
		this.size = size;
		
		GridLayout boardLayout = new GridLayout(size, size, 0, 0);
		
		grid.setLayout(boardLayout);
		
		JLabel[][] labels = new JLabel[size][size];
		
		for(int x = 0; x < size; ++x){
			for(int y = 0; y < size; ++y){
				if(collectionEmpty.contains(Game.gridEmpty[x][y])){
					JLabel label = labels[x][y] = new JLabel("");
					label.setBorder(new LineBorder(new Color(0,153,0), 1));
					Font defaultFont = new Font(label.getFont().getName(), Font.BOLD, 25);
					label.setFont(defaultFont);
					label.setOpaque(true);
					label.setBackground(Color.green);
					label.setHorizontalAlignment(SwingConstants.CENTER);
					grid.add(label);
				}
				else if(collectionNumeric.contains(Game.gridNumeric[x][y])){
					JLabel label = labels[x][y] = new JLabel(Game.gridNumeric[x][y].numericValue.toString().substring(1));
					label.setBorder(new LineBorder(new Color(0,153,0), 1));
					Font defaultFont = new Font(label.getFont().getName(), Font.BOLD, 25);
					label.setFont(defaultFont);
					label.setOpaque(true);
					label.setBackground(Game.gridNumeric[x][y].numericValue.color());
					label.setHorizontalAlignment(SwingConstants.CENTER);
					grid.add(label);
				}
				else{
					JLabel label = labels[y][x] = new JLabel(String.valueOf(Game.gridRock[x][y].jogadasRestantes));
					label.setBorder(new LineBorder(new Color(0,153,0), 1));
					Font defaultFont = new Font(label.getFont().getName(), Font.BOLD, 25);
					label.setFont(defaultFont);
					label.setOpaque(true);
					label.setBackground(Color.blue);
					label.setHorizontalAlignment(SwingConstants.CENTER);
					grid.add(label);
				}
			}
		}
	}
	
	public static void moveLeft(){
		MOV = false;
		for(int x = 0; x < Game.BOARD_SIZE; ++x){
			int count = 0;
			for(int y = 0; y < Game.BOARD_SIZE; ++y){
				if(collectionRock.contains(Game.gridRock[x][y])){
					count = Game.gridRock[x][y].getYBoard() + 1;
				}
				else if(collectionNumeric.contains(Game.gridNumeric[x][y])){
					if(count != y){
						Game.gridNumeric[x][count].numericValue = Game.gridNumeric[x][y].numericValue;
						collectionNumeric.add(Game.gridNumeric[x][count]);
						collectionNumeric.remove(Game.gridNumeric[x][y]);
						collectionEmpty.remove(Game.gridEmpty[x][count]);
						collectionEmpty.add(Game.gridEmpty[x][y]);
						count++;
						MOV = true;
					}
					else{
						count++;
					}
				}
			}
			for(int y = 0; y < Game.BOARD_SIZE - 1; y++){
				if(collectionNumeric.contains(Game.gridNumeric[x][y]) && collectionNumeric.contains(Game.gridNumeric[x][y+1])){
					if(Game.gridNumeric[x][y].numericValue.equals(Game.gridNumeric[x][y+1].numericValue)){
						Game.PONTUACAO = Game.PONTUACAO + (2 * Integer.parseInt(Game.gridNumeric[x][y].numericValue.toString().substring(1)));
						Game.gridNumeric[x][y].numericValue = NumericPlace.possibleValues.values()[Game.gridNumeric[x][y].numericValue.ordinal()+1];
						collectionNumeric.remove(Game.gridNumeric[x][y+1]);
						collectionEmpty.add(Game.gridEmpty[x][y+1]);
						MOV = true;
					}
				}
			}
		}
		if(MOV == true){
			RockPlace[] rocksEliminar = new RockPlace[collectionRock.size()];
			int count = 0;
			for(RockPlace rock: collectionRock){
				rock.jogadasRestantes = rock.jogadasRestantes - 1;
				System.out.println("Jogadas = " + rock.jogadasRestantes);
				if(rock.jogadasRestantes == 0){
					rocksEliminar[count] = rock;
					count++;
					collectionEmpty.add(Game.gridEmpty[rock.getXBoard()][rock.getYBoard()]);
				}
			}
			for(int i = 0; i < rocksEliminar.length; i++){
				if(rocksEliminar[i] != null){
					collectionRock.remove(rocksEliminar[i]);
				}
			}
		}
	}
	
	public static void moveDown(){
		MOV = false;
		for(int y = Game.BOARD_SIZE - 1; y >= 0; --y){
			int count = Game.BOARD_SIZE - 1;
			for(int x = Game.BOARD_SIZE - 1; x >= 0; --x){
				if(collectionRock.contains(Game.gridRock[x][y])){
					count = Game.gridRock[x][y].getXBoard() - 1;
				}
				else if(collectionNumeric.contains(Game.gridNumeric[x][y])){
					if(count != x){
						Game.gridNumeric[count][y].numericValue = Game.gridNumeric[x][y].numericValue;
						collectionNumeric.add(Game.gridNumeric[count][y]);
						collectionNumeric.remove(Game.gridNumeric[x][y]);
						collectionEmpty.remove(Game.gridEmpty[count][y]);
						collectionEmpty.add(Game.gridEmpty[x][y]);
						count--;
						MOV = true;
					}
					else{
						count--;
					}
				}
			}
			for(int x = 0; x < Game.BOARD_SIZE - 1; ++x){
				if(collectionNumeric.contains(Game.gridNumeric[x][y]) && collectionNumeric.contains(Game.gridNumeric[x+1][y])){
					if(Game.gridNumeric[x][y].numericValue.equals(Game.gridNumeric[x+1][y].numericValue)){
						Game.PONTUACAO = Game.PONTUACAO + (2 * Integer.parseInt(Game.gridNumeric[x][y].numericValue.toString().substring(1)));
						Game.gridNumeric[x+1][y].numericValue = NumericPlace.possibleValues.values()[Game.gridNumeric[x+1][y].numericValue.ordinal()+1];
						collectionNumeric.remove(Game.gridNumeric[x][y]);
						collectionEmpty.add(Game.gridEmpty[x][y]);
						MOV = true;
					}
				}
			}
		}
		if(MOV == true){
			RockPlace[] rocksEliminar = new RockPlace[collectionRock.size()];
			int count = 0;
			for(RockPlace rock: collectionRock){
				rock.jogadasRestantes = rock.jogadasRestantes - 1;
				System.out.println("Jogadas = " + rock.jogadasRestantes);
				if(rock.jogadasRestantes == 0){
					rocksEliminar[count] = rock;
					count++;
					collectionEmpty.add(Game.gridEmpty[rock.getXBoard()][rock.getYBoard()]);
				}
			}
			for(int i = 0; i < rocksEliminar.length; i++){
				if(rocksEliminar[i] != null){
					collectionRock.remove(rocksEliminar[i]);
				}
			}
		}
	}
	
	public static void moveRight(){
		MOV = false;
		for(int x = Game.BOARD_SIZE - 1; x >= 0; --x){
			int count = Game.BOARD_SIZE - 1;
			for(int y = Game.BOARD_SIZE - 1; y >= 0; --y){
				if(collectionRock.contains(Game.gridRock[x][y])){
					count = Game.gridRock[x][y].getYBoard() - 1;
				}
				else if(collectionNumeric.contains(Game.gridNumeric[x][y])){
					if(count != y){
						Game.gridNumeric[x][count].numericValue = Game.gridNumeric[x][y].numericValue;
						collectionNumeric.add(Game.gridNumeric[x][count]);
						collectionNumeric.remove(Game.gridNumeric[x][y]);
						collectionEmpty.remove(Game.gridEmpty[x][count]);
						collectionEmpty.add(Game.gridEmpty[x][y]);
						count--;
						MOV = true;
					}
					else{
						count--;
					}
				}
			}
			for(int y = 0; y < Game.BOARD_SIZE - 1; y++){
				if(collectionNumeric.contains(Game.gridNumeric[x][y]) && collectionNumeric.contains(Game.gridNumeric[x][y+1])){
					if(Game.gridNumeric[x][y].numericValue.equals(Game.gridNumeric[x][y+1].numericValue)){
						Game.PONTUACAO = Game.PONTUACAO + (2 * Integer.parseInt(Game.gridNumeric[x][y].numericValue.toString().substring(1)));
						Game.gridNumeric[x][y+1].numericValue = NumericPlace.possibleValues.values()[Game.gridNumeric[x][y+1].numericValue.ordinal()+1];
						collectionNumeric.remove(Game.gridNumeric[x][y]);
						collectionEmpty.add(Game.gridEmpty[x][y]);
						MOV = true;
					}
				}
			}
		}
		if(MOV == true){
			RockPlace[] rocksEliminar = new RockPlace[collectionRock.size()];
			int count = 0;
			for(RockPlace rock: collectionRock){
				rock.jogadasRestantes = rock.jogadasRestantes - 1;
				System.out.println("Jogadas = " + rock.jogadasRestantes);
				if(rock.jogadasRestantes == 0){
					rocksEliminar[count] = rock;
					count++;
					collectionEmpty.add(Game.gridEmpty[rock.getXBoard()][rock.getYBoard()]);
				}
			}
			for(int i = 0; i < rocksEliminar.length; i++){
				if(rocksEliminar[i] != null){
					collectionRock.remove(rocksEliminar[i]);
				}
			}
		}
	}
}