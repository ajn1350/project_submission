package reversi;
import javax.swing.*;
import java.awt.*;

public class GUIView implements IView {
	
	//stores the object references for both the model and view
	private IModel mod;
	private IController cont;
	
	//2d array to store the board gird
	Square[][] sqArr1;
	Square[][] sqArr2;
	
	//JLabel to store the references for the player messages
	JLabel whiteMessage;
	JLabel blackMessage;
	
	//int to store the grid size of the game
	int gridSizex;	
	int gridSizey;
	
	//initialise the GUI
	@Override
	public void initialise(IModel model, IController controller) {
		
		//model and controller object references
		this.mod = model;
		this.cont = controller;
		
		//gridSize vals for loops
		gridSizey = mod.getBoardHeight();
		gridSizex = mod.getBoardWidth();
		
		//Grid array for each player (1 -> White  2 -> Black)
		sqArr1 = new Square[mod.getBoardHeight()][mod.getBoardWidth()];
		sqArr2 = new Square[mod.getBoardHeight()][mod.getBoardWidth()];
		
		//Initialize each window for each player 
		JFrame frameWhitePlayer = new JFrame("Reversi - White Player");
		JFrame frameBlackPlayer = new JFrame("Reversi - Black Player");
		
		//disable resizing
		frameWhitePlayer.setResizable(false);
		frameBlackPlayer.setResizable(false);
		
		//add panel to add all buttons to for each player
		JPanel boardWhite = new JPanel();
		JPanel boardBlack = new JPanel();
		
		//assemble square buttons onto the board
		makeButtons(boardWhite,ReversiMain.WHITE_PLAYER,sqArr1);
		makeButtons(boardBlack,ReversiMain.BLACK_PLAYER,sqArr2);
		
		//add board to the window
		frameWhitePlayer.add(boardWhite,BorderLayout.CENTER);
		frameBlackPlayer.add(boardBlack,BorderLayout.CENTER);
		
		//add both buttons for greedy move and restarting the game, store object reference to message for each player 
		whiteMessage = makeView(frameWhitePlayer,ReversiMain.WHITE_PLAYER);
		blackMessage = makeView(frameBlackPlayer,ReversiMain.BLACK_PLAYER);
		
		
		//make window visible
		frameWhitePlayer.setVisible(true);
		frameBlackPlayer.setVisible(true);
		
		
			

	}
	
	
	//method to create the board, consisting of multiple square objects
	//one frame is normal and the other 7-i to flip the view
	public void makeButtons(JPanel board,int id,Square[][] arr) {
		
		//Set panel layout to grid
	    board.setLayout(new GridLayout(gridSizex,gridSizey));
	    
	    //setting the colors and grid line thicknesses
	    board.setBackground(Color.GREEN);
	    board.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    board.setForeground(Color.BLACK);
	    
	    //iterate for each row and col and creating a square object reference stored in the respective array
		for (int i=0;i<gridSizex;i++) {
			for (int j=0;j<gridSizey;j++) {
				
				int col = i; 
		        int row = j;
		        
		        if (id==ReversiMain.WHITE_PLAYER) {
		        	arr[col][row] = new Square(cont,row,col);
					arr[col][row].addActionListener(e -> {
						
					cont.squareSelected(id,col,row);
					});
					board.add(arr[col][row]);
		        }
		        else if (id==ReversiMain.BLACK_PLAYER) {
		        	arr[(gridSizex-1)-col][(gridSizey-1)-row] = new Square(cont,row,col);
		        	arr[(gridSizex-1)-col][(gridSizey-1)-row].addActionListener(e -> {
						
					cont.squareSelected(id,((gridSizex-1)-col),((gridSizey-1)-row));
					});
					
					board.add(arr[(gridSizex-1)-col][(gridSizey-1)-row]);
		        }
				
				
			}
		}
		
		
	}
	
	
	//assemble both restart button and greedy button alongside with the whole window
	public JLabel makeView(JFrame frame,int player) {
		
		//exit when exit button pressed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//setting size of window
		frame.setSize(500,600);
		
		//creating button panel
		JPanel panel = new JPanel();	
		panel.setLayout(new GridLayout(2,1));
		
		//new buttons to create (greedy button and restart)
		JButton plutus = new JButton("Activate Plutus Next Move");
		JButton restart = new JButton("Restart Game");
		restart.addActionListener(e -> {
			cont.startup();
		});
		plutus.addActionListener(e -> {
			cont.doAutomatedMove((player==1)?1:2);
		});
		
		//adding buttons to panel
		panel.add(plutus);
		panel.add(restart);
		
		//add button panel to window
		frame.add(panel,BorderLayout.SOUTH);
		
		//feedback label ("Start" is just placeholder as this is changed by controller immediately on start)
	    JLabel label = new JLabel("Start");
	    
	    //config of label
	    label.setHorizontalAlignment(JLabel.CENTER);
	    label.setOpaque(true);
	    label.setBackground(Color.WHITE);
	    label.setForeground(Color.BLACK);
	    
	    //add label to window
	    frame.add(label, BorderLayout.NORTH);
	    
	    //returns the feedback label object reference
	    return label;

	}


	//refreshes the visual state of buttons by referring to the models data
	@Override
	public void refreshView() {
		
		//iterate through each row and col to update each square
		for (int i=0;i<gridSizey;i++) {
			for (int j=0;j<gridSizex;j++){
				int row = i;
				int col = j;
				
				//if square is white then set col to white
				if(mod.getBoardContents(col,row)==ReversiMain.WHITE_PLAYER) {
					sqArr1[col][row].setCurrentColor(Color.WHITE);
					sqArr2[col][row].setCurrentColor(Color.WHITE);
				}
				
				//if square is black then set col to black
				else if(mod.getBoardContents(col,row)==ReversiMain.BLACK_PLAYER){
					sqArr1[col][row].setCurrentColor(Color.BLACK);
					sqArr2[col][row].setCurrentColor(Color.BLACK);
				}
				
				//if not either then colour remains green
				else {
					sqArr1[col][row].setCurrentColor(Color.GREEN);
					sqArr2[col][row].setCurrentColor(Color.GREEN);
				}
			}
		}
	}

	
	//when called updates feedback of relevant player to show the message passed to method (string)
	@Override
	public void feedbackToUser(int player, String message) {
		//if meant for white the set whites label text to new message
		if (player==ReversiMain.WHITE_PLAYER) {
			whiteMessage.setText(message);
		}
		
		//if meant for black the set label text to new message
		else if(player==ReversiMain.BLACK_PLAYER) {
			blackMessage.setText(message);
		}
		
		//invalid feedbackToUser call so can only be either 1 or 2
		else {
			System.err.println("Invalid Player ~ Please use either ReversiMain.WHITE_PLAYER or ReversiMain.BLACK_PLAYER");
		}

	}

}
