package tps.tp4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Game{

	public static int BOARD_SIZE = 5;
	
	public static EmptyPlace[][] gridEmpty = new EmptyPlace[BOARD_SIZE][BOARD_SIZE];
	
	public static NumericPlace[][] gridNumeric = new NumericPlace[BOARD_SIZE][BOARD_SIZE];
	
	public static RockPlace[][] gridRock = new RockPlace[BOARD_SIZE][BOARD_SIZE];
	
	public static int PONTUACAO = 0;
	
	private static String FILE_NAME;
	
	private static File LOAD_FILE;
	
	private static int COUNT_TIMER = 1;
	
	private static JFrame MAIN_FRAME;
	
	private static int KEY_PRESSED_X, KEY_PRESSED_Y;
	
	private static Clip MUSIC = null;
	
	public static void main(String[] args){
		
		startMusic();
		initGame();
		randomPlace();
		randomPlace();
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}
	
	public static boolean gameOver(){
		if(Board.collectionEmpty.size() == 0){
			return true;
		}
		return false;
	}
	
	public static boolean vitoria(){
		for(NumericPlace num: Board.collectionNumeric){
			if(num.numericValue == NumericPlace.possibleValues._2048){
				return true;
			}
		}
		return false;
	}
	
	public static void createGUI(){
		MAIN_FRAME = new JFrame();
		MAIN_FRAME.setTitle("2048");
		MAIN_FRAME.setSize(700, 700);
		MAIN_FRAME.setLocationRelativeTo(null);
		MAIN_FRAME.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		MAIN_FRAME.setBackground(Color.blue);
		MAIN_FRAME.getContentPane().setBackground(new Color(0, 150, 200));
		
		BorderLayout frameLayout = new BorderLayout();
		MAIN_FRAME.setLayout(frameLayout);
		
		JPanel outroPanel = new JPanel();
		
		Color corButton = new Color(255, 255, 255);
		
		JButton buttonLeft = new JButton("Left");
		JButton buttonDown = new JButton("Down");
		JButton buttonRight = new JButton("Right");
		JButton buttonMenu = new JButton("Menu");
		JButton buttonLoad = new JButton("Load Game");
		JButton buttonPlayback = new JButton("Playback Game");
		JButton buttonInit = new JButton("Init");
		
		
		buttonLeft.setBackground(corButton);
		buttonDown.setBackground(corButton);
		buttonRight.setBackground(corButton);
		buttonMenu.setBackground(corButton);
		buttonLoad.setBackground(corButton);
		buttonPlayback.setBackground(corButton);
		buttonInit.setBackground(corButton);
		
		Color buttonText = new Color(0,91,0);
		buttonLeft.setForeground(buttonText);
		buttonDown.setForeground(buttonText);
		buttonRight.setForeground(buttonText);
		buttonMenu.setForeground(buttonText);
		buttonLoad.setForeground(buttonText);
		buttonPlayback.setForeground(buttonText);
		buttonInit.setForeground(buttonText);
		
		ActionListener lLeft = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Board.moveLeft();
				Game.printMoveToFile('a');
				MAIN_FRAME.dispose();
				if(vitoria()){
					showVitoria();
				}
				else if(gameOver()){
					showGameOver();
				}
				else{
					if(Board.MOV == false){
						createGUI();
					}
					else{
						randomPlace();
						createGUI();
					}
				}
			}
		};
		
		ActionListener lDown = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Board.moveDown();
				Game.printMoveToFile('s');
				MAIN_FRAME.dispose();
				if(vitoria()){
					showVitoria();
				}
				else if(gameOver()){
					showGameOver();
				}
				else{
					if(Board.MOV == false){
						createGUI();
					}
					else{
						randomPlace();
						createGUI();
					}
				}
			}
		};
		
		ActionListener lRight = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Board.moveRight();
				Game.printMoveToFile('d');
				MAIN_FRAME.dispose();
				if(vitoria()){
					showVitoria();
				}
				else if(gameOver()){
					showGameOver();
				}
				else{
					if(Board.MOV == false){
						createGUI();
					}
					else{
						randomPlace();
						createGUI();
					}
				}
			}
		};
		
		ActionListener lMenu = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MAIN_FRAME.dispose();
				showMenu();
			}
		};
		
		ActionListener lInit = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MAIN_FRAME.dispose();
				initGame();
				randomPlace();
				randomPlace();
				createGUI();
			}
		};
		
		ActionListener lLoad = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JPanel frameLoad = new JPanel();
				BorderLayout loadLayout = new BorderLayout();
				frameLoad.setLayout(loadLayout);
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int returnVal = fc.showOpenDialog(frameLoad);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					LOAD_FILE = fc.getSelectedFile();
					MAIN_FRAME.dispose();
					loadGame(LOAD_FILE);
					System.out.println("Loaded: " + LOAD_FILE.getName());
				}
				
				frameLoad.add(fc);
			}
		};
		
		ActionListener lPlayback = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JPanel framePlayback = new JPanel();
				BorderLayout playbackLayout = new BorderLayout();
				framePlayback.setLayout(playbackLayout);
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int returnVal = fc.showOpenDialog(framePlayback);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					LOAD_FILE = fc.getSelectedFile();
					MAIN_FRAME.dispose();
					playbackSave(LOAD_FILE);
					System.out.println("Playing back: " + LOAD_FILE.getName());
				}
				
				framePlayback.add(fc);
			}
		};
		
		MouseListener lMouse = new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == 3){
					int keyX = e.getX();
					int keyY = e.getY();
					if(keyX - KEY_PRESSED_X > 50){
						Board.moveRight();
						Game.printMoveToFile('d');
						MAIN_FRAME.dispose();
						if(vitoria()){
							showVitoria();
						}
						else if(gameOver()){
							showGameOver();
						}
						else{
							if(Board.MOV == false){
								createGUI();
							}
							else{
								randomPlace();
								createGUI();
							}
						}
					}
					else if(KEY_PRESSED_X - keyX > 50){
						Board.moveLeft();
						Game.printMoveToFile('a');
						MAIN_FRAME.dispose();
						if(vitoria()){
							showVitoria();
						}
						else if(gameOver()){
							showGameOver();
						}
						else{
							if(Board.MOV == false){
								createGUI();
							}
							else{
								randomPlace();
								createGUI();
							}
						}
					}
					else if(keyY - KEY_PRESSED_Y > 50){
						Board.moveDown();
						Game.printMoveToFile('s');
						MAIN_FRAME.dispose();
						if(vitoria()){
							showVitoria();
						}
						else if(gameOver()){
							showGameOver();
						}
						else{
							if(Board.MOV == false){
								createGUI();
							}
							else{
								randomPlace();
								createGUI();
							}
						}
					}
				}
			}

			public void mousePressed(MouseEvent e) {
				if(e.getButton() == 3){
					KEY_PRESSED_X = e.getX();
					KEY_PRESSED_Y = e.getY();
				}
			}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		};
		
		KeyListener lKeys = new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();

		        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
		        	Board.moveLeft();
					Game.printMoveToFile('a');
					MAIN_FRAME.dispose();
					if(vitoria()){
						showVitoria();
					}
					else if(gameOver()){
						showGameOver();
					}
					else{
						if(Board.MOV == false){
							createGUI();
						}
						else{
							randomPlace();
							createGUI();
						}
					}
		        }

		        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
		        	Board.moveRight();
					Game.printMoveToFile('d');
					MAIN_FRAME.dispose();
					if(vitoria()){
						showVitoria();
					}
					else if(gameOver()){
						showGameOver();
					}
					else{
						if(Board.MOV == false){
							createGUI();
						}
						else{
							randomPlace();
							createGUI();
						}
					}
		        }

		        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
		        	Board.moveDown();
					Game.printMoveToFile('s');
					MAIN_FRAME.dispose();
					if(vitoria()){
						showVitoria();
					}
					else if(gameOver()){
						showGameOver();
					}
					else{
						if(Board.MOV == false){
							createGUI();
						}
						else{
							randomPlace();
							createGUI();
						}
					}
		        }
			}

			public void keyPressed(KeyEvent e) {
			}
		};
		
		buttonLeft.addActionListener(lLeft);
		buttonDown.addActionListener(lDown);
		buttonRight.addActionListener(lRight);
		buttonMenu.addActionListener(lMenu);
		buttonLoad.addActionListener(lLoad);
		buttonPlayback.addActionListener(lPlayback);
		buttonInit.addActionListener(lInit);
		
		outroPanel.add(buttonMenu);
		outroPanel.add(buttonLeft);
		outroPanel.add(buttonDown);
		outroPanel.add(buttonRight);
		outroPanel.add(buttonLoad);
		outroPanel.add(buttonPlayback);
		outroPanel.add(buttonInit);
		
		JPanel grid = new Board(BOARD_SIZE).grid;

		MAIN_FRAME.setFocusable(true);
		MAIN_FRAME.addKeyListener(lKeys);
		MAIN_FRAME.addMouseListener(lMouse);
		
		MAIN_FRAME.add(outroPanel, BorderLayout.SOUTH);
		MAIN_FRAME.add(grid, BorderLayout.CENTER);
		
		MAIN_FRAME.setVisible(true);
	}
	
	
	public static void showMenu(){
		JFrame frameMenu = new JFrame();
		frameMenu.setTitle("2048 - Menu");
		
		frameMenu.setSize(700, 700);
		frameMenu.setLocationRelativeTo(null);
		frameMenu.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frameMenu.setBackground(Color.blue);
		
		int sizeInicial = BOARD_SIZE;
		
		GridLayout menuLayout = new GridLayout(5, 1, 15, 15);
		
		JButton[] buttonsMenu = new JButton[5];

		buttonsMenu[0] = new JButton("Resume");
		buttonsMenu[1] = new JButton("Init");
		buttonsMenu[2] = new JButton("Help");
		buttonsMenu[3] = new JButton("About");
		buttonsMenu[4] = new JButton("Options");
		
		Font defaultFont = new Font(buttonsMenu[0].getFont().getName(), Font.BOLD, 25);
		buttonsMenu[0].setFont(defaultFont);
		buttonsMenu[1].setFont(defaultFont);
		buttonsMenu[2].setFont(defaultFont);
		buttonsMenu[3].setFont(defaultFont);
		buttonsMenu[4].setFont(defaultFont);
		
		Color corButton = new Color(0,169,0);
		Color corText = new Color(255,255,255);
		buttonsMenu[0].setBackground(corButton);
		buttonsMenu[0].setForeground(corText);
		buttonsMenu[1].setBackground(corButton);
		buttonsMenu[1].setForeground(corText);
		buttonsMenu[2].setBackground(corButton);
		buttonsMenu[2].setForeground(corText);
		buttonsMenu[3].setBackground(corButton);
		buttonsMenu[3].setForeground(corText);
		buttonsMenu[4].setBackground(corButton);
		buttonsMenu[4].setForeground(corText);
		
		ActionListener buttonResume = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sizeInicial == BOARD_SIZE){
					frameMenu.dispose();
					createGUI();
				}
				else{
					frameMenu.dispose();
					initGame();
					randomPlace();
					randomPlace();
					createGUI();
				}
			}
		};
		
		ActionListener buttonInit = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameMenu.dispose();
				initGame();
				randomPlace();
				randomPlace();
				createGUI();
			}
		};
		
		ActionListener buttonHelp = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frameHelp = new JFrame();
				frameHelp.setSize(700, 500);
				frameHelp.setTitle("Help");
				frameHelp.setLocationRelativeTo(null);
				frameHelp.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				
				JButton helpBack = new JButton("Back");
				Color textColor = new Color(0,91,0);
				
				helpBack.setBackground(corButton);
				helpBack.setForeground(corText);
				BorderLayout helpLayout = new BorderLayout();
				JTextPane helpInstructions = new JTextPane();
				helpInstructions.setEditable(false);

				helpInstructions.setText(System.lineSeparator() + "Instruções de jogo:" + System.lineSeparator() + System.lineSeparator() + "Use as teclas das setas, ASD ou o rato para mover as peças no tabuleiro. Quando duas peças com o mesmo valor se tocam, fundem-se numa só com o valor duplicado. Sempre que há um movimento aparece uma nova peça no tabuleiro." + System.lineSeparator() + 
						"A pontuação consiste na soma de todas as fusões de peças. Por exemplo, quando duas peças de valor 2 se fundem, criam uma nova peça de valor 4 e é adicionado 4 ao total da pontuação." + System.lineSeparator() + 
						"A condição de vitória dá-se quando conseguir ter uma peça com o valor 2048." + System.lineSeparator() + "A condição de Game Over é quando não houver mais espaços em branco.");

				Font textFont = new Font(helpInstructions.getFont().getFontName(), Font.PLAIN, 20);
				helpInstructions.setFont(textFont);
				helpInstructions.setForeground(textColor);
				StyledDocument doc = helpInstructions.getStyledDocument();
				SimpleAttributeSet center = new SimpleAttributeSet();
				StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
				doc.setParagraphAttributes(0, doc.getLength(), center, false);

				frameHelp.setLayout(helpLayout);
				
				
				frameHelp.add(helpInstructions, BorderLayout.CENTER);
				frameHelp.add(helpBack, BorderLayout.NORTH);
				
				ActionListener lBack = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						frameHelp.dispose();
						frameMenu.setVisible(true);
					}
				};
				helpBack.addActionListener(lBack);
				
				frameMenu.dispose();
				frameHelp.setVisible(true);
			}
		};
		
		ActionListener buttonAbout = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFrame frameAbout = new JFrame();
				frameAbout.setSize(700, 700);
				frameAbout.setTitle("About");
				frameAbout.setLocationRelativeTo(null);
				frameAbout.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				
				JPanel helpText = new JPanel();
				JPanel text = new JPanel();
				JButton helpBack = new JButton("Back");
				
				Color textColor = new Color(0,91,0);
				
				BorderLayout helpLayout = new BorderLayout();
				GridLayout gridAbout = new GridLayout(1,2,10,10);
				GridLayout gridText = new GridLayout(5,1,10,10);

				helpBack.setBackground(corButton);
				helpBack.setForeground(corText);
				helpText.setLayout(gridAbout);
				text.setLayout(gridText);
				helpText.setBackground(new Color(255,255,255));
				text.setBackground(new Color(255,255,255));
				
				JLabel helpInstructions = new JLabel("About", SwingConstants.CENTER);
				Font titleFont = new Font(helpInstructions.getFont().getFontName(), Font.BOLD, 35);
				helpInstructions.setFont(titleFont);
				helpInstructions.setForeground(textColor);
				text.add(helpInstructions);
				text.add(new JLabel("LEIM", SwingConstants.CENTER));
				text.add(new JLabel("MoP", SwingConstants.CENTER));
				text.add(new JLabel("Semestre 1516SV", SwingConstants.CENTER));
				text.add(new JLabel("42557 - Diogo Milheiro", SwingConstants.CENTER));
				helpText.add(text);
				
				ImageIcon fotoGrupo = new ImageIcon(new ImageIcon("foto.jpg").getImage().getScaledInstance(320, 427, Image.SCALE_DEFAULT));
				JLabel foto = new JLabel(fotoGrupo);
				helpText.add(foto);
				frameAbout.setLayout(helpLayout);
				frameAbout.add(helpText, BorderLayout.CENTER);
				frameAbout.add(helpBack, BorderLayout.NORTH);
				
				ActionListener lBack = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						frameAbout.dispose();
						frameMenu.setVisible(true);
					}
				};
				helpBack.addActionListener(lBack);
				
				frameMenu.dispose();
				frameAbout.setVisible(true);
			}
		};
		
		ActionListener buttonOptions = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFrame frameOptions = new JFrame();
				frameOptions.setSize(700, 700);
				frameOptions.setTitle("Options");
				frameOptions.setLocationRelativeTo(null);
				frameOptions.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				GridLayout optionsGrid = new GridLayout(4, 1, 15, 15);
				frameOptions.setLayout(optionsGrid);
				
				JButton[] buttonsOptions = new JButton[4];
				
				buttonsOptions[0] = new JButton("Tamanho da grelha");
				buttonsOptions[1] = new JButton("Som");
				buttonsOptions[2] = new JButton("Jogadas de Rocks");
				buttonsOptions[3] = new JButton("Back");
				
				buttonsOptions[0].setBackground(corButton);
				buttonsOptions[0].setForeground(corText);
				buttonsOptions[1].setBackground(corButton);
				buttonsOptions[1].setForeground(corText);
				buttonsOptions[2].setBackground(corButton);
				buttonsOptions[2].setForeground(corText);
				buttonsOptions[3].setBackground(corButton);
				buttonsOptions[3].setForeground(corText);
				
				buttonsOptions[0].setFont(defaultFont);
				buttonsOptions[1].setFont(defaultFont);
				buttonsOptions[2].setFont(defaultFont);
				buttonsOptions[3].setFont(defaultFont);
				
				ActionListener tamanhoOptions = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFrame tamanhoOptions = new JFrame();
						String boardSize = JOptionPane.showInputDialog(tamanhoOptions, "Qual o tamanho para a grelha? (Introduza só valores inteiros)", "5");
						BOARD_SIZE = Integer.parseInt(boardSize);
						System.out.println("Tamanho = " + boardSize);
						tamanhoOptions.dispose();
					}
				};
				
				ActionListener somOptions = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFrame somOptions = new JFrame();
						somOptions.setSize(700, 700);
						somOptions.setTitle("Som");
						somOptions.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
						JRadioButton[] radioButtons = new JRadioButton[2];
						final ButtonGroup group = new ButtonGroup();
						radioButtons[0] = new JRadioButton("On");
						radioButtons[0].setActionCommand("on");
						radioButtons[1] = new JRadioButton("Off");
						radioButtons[1].setActionCommand("off");
						
						radioButtons[0].setSelected(true);
						
						group.add(radioButtons[0]);
						group.add(radioButtons[1]);
						
						JButton okButton = new JButton("OK");
						
						okButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String command = group.getSelection().getActionCommand();
								if(command == "on"){
									MUSIC.stop();
									startMusic();
									somOptions.dispose();
									frameOptions.setVisible(true);
								}
								else if(command == "off"){
									MUSIC.stop();
									somOptions.dispose();
									frameOptions.setVisible(true);
								}
							}
						});
						JPanel box = new JPanel();
						box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
						
						for(int i = 0; i < radioButtons.length; i++){
							box.add(radioButtons[i]);
						}
						
						JPanel pane = new JPanel();
						JLabel som = new JLabel("Selecione como quer o som");
						pane.setLayout(new BorderLayout());
						pane.add(som, BorderLayout.PAGE_START);
						pane.add(box, BorderLayout.CENTER);
						pane.add(okButton, BorderLayout.PAGE_END);
						somOptions.add(pane);
						somOptions.pack();
						somOptions.setLocationRelativeTo(null);
						frameOptions.dispose();
						somOptions.setVisible(true);
					}
				};
				
				ActionListener rockOptions = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFrame rockOptions = new JFrame();
						String jogadasRocks = JOptionPane.showInputDialog(rockOptions, "Qual o número de jogadas que em que são visíveis as Rocks? (Introduza só valores inteiros)", "4");
						RockPlace.JOGADAS_RESTANTES = Integer.parseInt(jogadasRocks);
						System.out.println("Jogadas Rocks = " + jogadasRocks);
						rockOptions.dispose();
					}
				};
				
				ActionListener backOptions = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						frameOptions.dispose();
						frameMenu.setVisible(true);
					}
				};
				
				buttonsOptions[0].addActionListener(tamanhoOptions);
				buttonsOptions[1].addActionListener(somOptions);
				buttonsOptions[2].addActionListener(rockOptions);
				buttonsOptions[3].addActionListener(backOptions);
				
				frameOptions.add(buttonsOptions[0]);
				frameOptions.add(buttonsOptions[1]);
				frameOptions.add(buttonsOptions[2]);
				frameOptions.add(buttonsOptions[3]);
				
				frameMenu.dispose();
				frameOptions.setVisible(true);
			}
		};
		
		buttonsMenu[0].addActionListener(buttonResume);
		buttonsMenu[1].addActionListener(buttonInit);
		buttonsMenu[2].addActionListener(buttonHelp);
		buttonsMenu[3].addActionListener(buttonAbout);
		buttonsMenu[4].addActionListener(buttonOptions);
		
		for(int i = 0; i < buttonsMenu.length; i++){
			frameMenu.add(buttonsMenu[i]);
		}
		
		frameMenu.setLayout(menuLayout);
		
		frameMenu.setVisible(true);
	}
	
	public static void showGameOver(){
		JFrame frameGameOver = new JFrame();
		
		frameGameOver.setTitle("GAME OVER");
		frameGameOver.setSize(700, 700);
		frameGameOver.setLocationRelativeTo(null);
		frameGameOver.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frameGameOver.setBackground(Color.blue);
		
		GridLayout gameOverGrid = new GridLayout(2, 1, 0, 0);
		BorderLayout gameOverLayout = new BorderLayout();
		
		frameGameOver.setLayout(gameOverLayout);
		
		JPanel gameOver = new JPanel(gameOverGrid);
		gameOver.setBackground(Color.red);
		
		JLabel gameOverText = new JLabel("GAME OVER!");
		Font defaultFont = gameOverText.getFont();
		gameOverText.setFont(new Font(defaultFont.getName(), Font.BOLD, 50));
		gameOverText.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel gameOverPontos = new JLabel("Pontuação: " + PONTUACAO);
		gameOverPontos.setFont(new Font(defaultFont.getName(), Font.PLAIN, 45));
		gameOverPontos.setHorizontalAlignment(SwingConstants.CENTER);
		
		gameOver.add(gameOverText);
		gameOver.add(gameOverPontos);
		
		JButton gameOverInit = new JButton("Init");
		
		ActionListener lGameOverInit = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameGameOver.dispose();
				initGame();
				randomPlace();
				randomPlace();
				createGUI();
			}
		};
		
		gameOverInit.addActionListener(lGameOverInit);
		
		frameGameOver.add(gameOverInit, BorderLayout.NORTH);
		frameGameOver.add(gameOver, BorderLayout.CENTER);
		frameGameOver.setVisible(true);
	}
	
	public static void showVitoria(){
		JFrame frameVitoria = new JFrame();
		
		frameVitoria.setTitle("VITÓRIA!");
		frameVitoria.setSize(700, 700);
		frameVitoria.setLocationRelativeTo(null);
		frameVitoria.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frameVitoria.setBackground(Color.blue);
		
		GridLayout vitoriaGrid = new GridLayout(2, 1, 0, 0);
		BorderLayout vitoriaLayout = new BorderLayout();
		
		frameVitoria.setLayout(vitoriaLayout);
		
		JPanel vitoria = new JPanel(vitoriaGrid);
		vitoria.setBackground(Color.green);
		
		JLabel vitoriaText = new JLabel("Parabéns!");
		Font defaultFont = vitoriaText.getFont();
		vitoriaText.setFont(new Font(defaultFont.getName(), Font.BOLD, 50));
		vitoriaText.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel vitoriaPontos = new JLabel("Pontuação: " + PONTUACAO);
		vitoriaPontos.setFont(new Font(defaultFont.getName(), Font.PLAIN, 45));
		vitoriaPontos.setHorizontalAlignment(SwingConstants.CENTER);
		
		vitoria.add(vitoriaText);
		vitoria.add(vitoriaPontos);
		
		JButton vitoriaInit = new JButton("Init");
		
		ActionListener lVitoriaInit = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameVitoria.dispose();
				initGame();
				randomPlace();
				randomPlace();
				createGUI();
			}
		};
		
		vitoriaInit.addActionListener(lVitoriaInit);
		
		frameVitoria.add(vitoriaInit, BorderLayout.NORTH);
		frameVitoria.add(vitoria, BorderLayout.CENTER);
		frameVitoria.setVisible(true);
		
	}
	
	public static String getFileName(){
		Calendar calendarNow = Calendar.getInstance();
		int calendarYear = calendarNow.get(Calendar.YEAR);
		int calendarMonth = calendarNow.get(Calendar.MONTH) + 1;
		int calendarDay = calendarNow.get(Calendar.DAY_OF_MONTH);
		int calendarHour = calendarNow.get(Calendar.HOUR_OF_DAY);
		int calendarMinute = calendarNow.get(Calendar.MINUTE);
		int calendarSecond = calendarNow.get(Calendar.SECOND);
		String fileName = "2048_" + calendarYear + "_" + calendarMonth + "_" + calendarDay 	+ "_" + calendarHour + "_" + calendarMinute + "_" + calendarSecond + ".txt";
	 	
		return fileName;
	}
	
	public static boolean printMoveToFile(char move){
		boolean prov = false;
		try{
			FileWriter fw = new FileWriter(FILE_NAME, true);
			fw.write(move + ";");
		 	fw.close();
		 	prov = true;
		 	return prov;
		}
		catch (IOException e) {
			e.printStackTrace();
			return prov;
		}
	}
	
	public static boolean printOptionsToFile(){
		boolean prov = false;
		try{
			FileWriter fw = new FileWriter(FILE_NAME, true);
			fw.write(String.valueOf(BOARD_SIZE));
			fw.write(";" + RockPlace.JOGADAS_RESTANTES);
			fw.write(System.lineSeparator());
			fw.close();
			prov = true;
			return prov;
		}
		catch (IOException e) {
			e.printStackTrace();
			return prov;
		}
	}
	
	public static boolean printNewPlaceToFile(OccupiedPlace place){
		boolean prov = false;
		try{
			FileWriter fw = new FileWriter(FILE_NAME, true);
			if(place instanceof RockPlace){
				fw.write("(" + place.getXBoard() + "," + place.getYBoard() + ");");
			}
			else if(place instanceof NumericPlace){
				NumericPlace newNumeric = (NumericPlace)place;
				fw.write("(" + place.getXBoard() + "," + place.getYBoard() + "," + newNumeric.numericValue + ");");
			}
			fw.close();
			prov = true;
			return prov;
		}
		catch (IOException e) {
			e.printStackTrace();
			return prov;
		}
	}
	
	public static void loadGame(File file){
		Scanner fileScanner = null;
		try {
			fileScanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String fileOptions = fileScanner.nextLine();
		String fileContent = fileScanner.nextLine();
		fileScanner.close();
		String[] jogadasPlaces = fileContent.split(";");
		String[] options = fileOptions.split(";");

		BOARD_SIZE = Integer.parseInt(options[0]);
		RockPlace.JOGADAS_RESTANTES = Integer.parseInt(options[1]);
		initGame();
		try {
			FileWriter fw = new FileWriter(FILE_NAME, true);
			fw.write(fileContent);
			fw.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < jogadasPlaces.length; i++){
			if(jogadasPlaces[i].length() == 1){
				if(jogadasPlaces[i].equals("a")){
					Board.moveLeft();
				}
				else if(jogadasPlaces[i].equals("s")){
					Board.moveDown();
				}
				else if(jogadasPlaces[i].equals("d")){
					Board.moveRight();
				}
			}
			else if(jogadasPlaces[i].length() == 5){
				String[] places = jogadasPlaces[i].split(",");
				int placesX = Integer.parseInt(places[0].substring(1));
				int placesY = Integer.parseInt(places[1].substring(0, places[1].length()-1));
				Board.collectionRock.add(gridRock[placesX][placesY]);
				Board.collectionEmpty.remove(gridEmpty[placesX][placesY]);
			}
			else{
				String[] places = jogadasPlaces[i].split(",");
				int placesX = Integer.parseInt(places[0].substring(1));
				int placesY = Integer.parseInt(places[1]);
				String placesValue = places[2].substring(0, places[2].length()-1);

				gridNumeric[placesX][placesY].numericValue = NumericPlace.possibleValues.valueOf(placesValue);
				Board.collectionNumeric.add(gridNumeric[placesX][placesY]);
				Board.collectionEmpty.remove(gridEmpty[placesX][placesY]);
			}
		}
		createGUI();
	}
	
	public static void playbackSave(File file){
		Scanner fileScanner = null;
		try {
			fileScanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String fileOptions = fileScanner.nextLine();
		String fileContent = fileScanner.nextLine();
		fileScanner.close();
		String[] jogadasPlaces = fileContent.split(";");
		String[] options = fileOptions.split(";");

		BOARD_SIZE = Integer.parseInt(options[0]);
		RockPlace.JOGADAS_RESTANTES = Integer.parseInt(options[1]);
		Timer timer = new Timer();
		TimerTask moves = new TimerTask() {
			public void run() {
				if(jogadasPlaces.length < COUNT_TIMER){
					timer.cancel();
				}
				else{
					for(int i = ((COUNT_TIMER) - 1) ; i < COUNT_TIMER; i++){
						if(jogadasPlaces[i].length() == 1){
							if(jogadasPlaces[i].equals("a")){
								Board.moveLeft();
							}
							else if(jogadasPlaces[i].equals("s")){
								Board.moveDown();
							}
							else if(jogadasPlaces[i].equals("d")){
								Board.moveRight();
							}
						}
						else if(jogadasPlaces[i].length() == 5){
							String[] places = jogadasPlaces[i].split(",");
							int placesX = Integer.parseInt(places[0].substring(1));
							int placesY = Integer.parseInt(places[1].substring(0, places[1].length()-1));
							Board.collectionRock.add(gridRock[placesX][placesY]);
							Board.collectionEmpty.remove(gridEmpty[placesX][placesY]);
						}
						else{
							String[] places = jogadasPlaces[i].split(",");
							int placesX = Integer.parseInt(places[0].substring(1));
							int placesY = Integer.parseInt(places[1]);
							String placesValue = places[2].substring(0, places[2].length()-1);
	
							gridNumeric[placesX][placesY].numericValue = NumericPlace.possibleValues.valueOf(placesValue);
							Board.collectionNumeric.add(gridNumeric[placesX][placesY]);
							Board.collectionEmpty.remove(gridEmpty[placesX][placesY]);
						}
					}
				COUNT_TIMER++;
				MAIN_FRAME.dispose();
				createGUI();
				}
			}
		};
		initGame();
		try {
			FileWriter fw = new FileWriter(FILE_NAME, true);
			fw.write(fileContent);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		timer.schedule(moves, 0, 1000);
	}
	
	public static void randomPlace(){
		Random gerador = new Random();
		int randomEmpty = gerador.nextInt(Board.collectionEmpty.size());
		EmptyPlace newEmpty = (EmptyPlace)Board.collectionEmpty.toArray()[randomEmpty];
		
		int randomX = newEmpty.getXBoard();
		int randomY = newEmpty.getYBoard();
		float proximoNumero = gerador.nextFloat();
		
		if(proximoNumero < 0.02){
			Board.collectionRock.add(gridRock[randomX][randomY]);
			Board.collectionEmpty.remove(gridEmpty[randomX][randomY]);
			printNewPlaceToFile(gridRock[randomX][randomY]);
		}
		else if(proximoNumero < 0.9){
			Board.collectionNumeric.add(gridNumeric[randomX][randomY]);
			Board.collectionEmpty.remove(gridEmpty[randomX][randomY]);
			printNewPlaceToFile(gridNumeric[randomX][randomY]);
		}
		else{
			Board.collectionNumeric.add(gridNumeric[randomX][randomY]);
			Board.collectionEmpty.remove(gridEmpty[randomX][randomY]);
			gridNumeric[randomX][randomY].numericValue = NumericPlace.possibleValues._4;
			printNewPlaceToFile(gridNumeric[randomX][randomY]);
		}
	}
	
	public static void startMusic(){
		String[] allSongs = new String[8];
		for(int i = 1; i < 9; i++){
			allSongs[i-1] = "musica_fundo" + i + ".wav";
		}
		Random random = new Random();
		int randomSong = random.nextInt(7) + 1;
		
		try {
			MUSIC = AudioSystem.getClip();
			File newFile = new File(allSongs[randomSong]);
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(newFile);
			MUSIC.open(inputStream);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		
		MUSIC.loop(Clip.LOOP_CONTINUOUSLY);
		MUSIC.start();
	}
	
	public static void initGame(){
		Board.collectionEmpty.clear();
		Board.collectionRock.clear();
		Board.collectionNumeric.clear();
		PONTUACAO = 0;
		FILE_NAME = getFileName();
		printOptionsToFile();
		gridNumeric = new NumericPlace[BOARD_SIZE][BOARD_SIZE];
		gridEmpty = new EmptyPlace[BOARD_SIZE][BOARD_SIZE];
		gridRock = new RockPlace[BOARD_SIZE][BOARD_SIZE];
		for(int i = 0; i < BOARD_SIZE; i++){
			for(int j = 0; j < BOARD_SIZE; j++){
				gridNumeric[i][j] = new NumericPlace(i,j);
				gridEmpty[i][j] = new EmptyPlace(i,j);
				gridRock[i][j] = new RockPlace(i,j);
				Board.collectionEmpty.add(gridEmpty[i][j]);
			}
		}
	}
}