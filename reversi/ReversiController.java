package reversi;

public class ReversiController implements IController {

	//object references for both model and view
	private IModel mod;
	private IView myView;
	
	//array to store the possible captures at each given square
	private int[][] whiteArr;
	private int[][] blackArr;
	
	//int to keep track of score for endgame
	private int whiteScore=0;
	private int blackScore=0;
	
	//array of integers to store the x, y and number of token taken of the best possible move
	private int [] blackMax;
	private int [] whiteMax;
	
	//integer array storing the best moves with indexes: 0 ~ x coord, 1 ~ y coord, 2 ~ number of tokens taken
	public ReversiController() {

		blackMax = new int[3];
		whiteMax = new int[3];
				
	}

	//initalise all variables for controller
	@Override
	public void initialise(IModel model, IView view) {
		//store model and view object references
		this.mod = model;
		this.myView = view;
		
		//initalize integer 2d array based on board height and width using model
		whiteArr=new int [mod.getBoardHeight()][mod.getBoardWidth()];
		blackArr=new int [mod.getBoardHeight()][mod.getBoardWidth()];

	}
	
	//setup the board for new game
	@Override
	public void startup() {
		//set finished flag to false 
		mod.setFinished(false);
		
		//initalize the board
		this.initializaBoard();
		
		//ensure turn is white and appropraite feedback is given
		this.initializeTurns();
		
		//refresh GUI view to show updated model
		myView.refreshView();
	}
	
	//inizalize the board
	private void initializaBoard(){
		
		//set width and heigh value to set up board values
		int width = mod.getBoardWidth();
		int height = mod.getBoardHeight();
		
		//iterate through every square on the board
		for ( int x = 0 ; x < width ; x++ ){
			for ( int y = 0 ; y < height ; y++ ){
				if ((x==3||x==4) && (y==3||y==4)) {
					//if in the middle 4x4 square then set to the relevant pieces (white only if (3,3) or (4,4))
					mod.setBoardContents(x, y, (x==y)?ReversiMain.WHITE_PLAYER:ReversiMain.BLACK_PLAYER);
				}
				else {
					//if not in the middle 4x4 square then set to empty
					mod.setBoardContents(x, y, ReversiMain.EMPTY_SQUARE);
				}
				
				
			}
		}
		
		
		//update possible captures to reflect new board state
		this.updateCaptureCount();

	}
	
	
	//set turn to the appropriate player and relevant feedback to user is given
	private void initializeTurns(){
		
		//white starts by default so set player to whtie
		mod.setPlayer(ReversiMain.WHITE_PLAYER);
		
		//change user feedback to know who goes first
		myView.feedbackToUser(ReversiMain.WHITE_PLAYER,"White player - choose where to put your piece");
		myView.feedbackToUser(ReversiMain.BLACK_PLAYER,"Black player - not your turn");
		
	}
	
	//update possible capture counts for each square for each player
	public void updateCaptureCount() {
		this.countCapture(ReversiMain.WHITE_PLAYER, whiteArr);
		this.countCapture(ReversiMain.BLACK_PLAYER, blackArr);
	}
	
	//count tokens on board for each player (used for end of game)
	public void countTokens() {
		
		//set scores to 0
		whiteScore=0;
		blackScore=0;
		
		//set current focused square to invalid null value
		int square=-1;
		for(int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				//get contents for each square
				square = mod.getBoardContents(i, j);
				//if square is whtie add 1 to white score
				if (square==ReversiMain.WHITE_PLAYER) {
					whiteScore++;
				}
				
				//if score to black add 1 to black score
				else if (square==ReversiMain.BLACK_PLAYER) {
					blackScore++;
				}
			}
		}
	}
	
	
	//check how the game has finished if finished and send relevant messages to each user
	public void finishSignal() {
		
		//check if game has finished
		if (mod.hasFinished()==true) {
			//count the tokens for each player storing in the whiteScore and blackScore
			this.countTokens();
			
			//if both players don't have the same score the send each player the correct message with the scores
			if(whiteScore!=blackScore) {
				myView.feedbackToUser(ReversiMain.WHITE_PLAYER,(whiteScore>blackScore)?"White won. White "+whiteScore+" to Black "+blackScore+". Reset the game to replay.":"Black won. Black "+blackScore+" to White "+whiteScore+". Reset the game to replay.");
				myView.feedbackToUser(ReversiMain.BLACK_PLAYER,(whiteScore>blackScore)?"White won. White "+whiteScore+" to Black "+blackScore+". Reset the game to replay.":"Black won. Black "+blackScore+" to White "+whiteScore+". Reset the game to replay.");
			}
			
			//if a draw the send each player draw message with the score both got
			else {
				myView.feedbackToUser(ReversiMain.WHITE_PLAYER, "Draw. Both players ended with "+whiteScore+" pieces."+" Reset the game to replay.");
				myView.feedbackToUser(ReversiMain.BLACK_PLAYER, "Draw. Both players ended with "+blackScore+" pieces."+" Reset the game to replay.");
			}
		}
	}

	
	//helper function for testing to reflect changes in the model manually set
	@Override
	public void update() {
		
		//check what the current player
		int player = mod.getPlayer();
		
		//update possible captures and capture array
		this.updateCaptureCount();
		
		//declare finish variable to store player with possible next turn
		int finish;
		
		//if white has a possible move and black has a possible move then continue the game accordingly
		if (whiteMax[2]>0&&blackMax[2]>0) {
			
			//if current player is black then black plays and not whites turn (likewise vice versa)
			myView.feedbackToUser((player),(player!=ReversiMain.WHITE_PLAYER)?"Black player - choose where to put your piece":"White player - choose where to put your piece");
			myView.feedbackToUser((player%2)+1, (player!=ReversiMain.WHITE_PLAYER)?"White player - not your turn":"Black player - not your turn");;//valid move for both
		}
		
		//if both players have no possible moves then end game
		else if (!(whiteMax[2]>0||blackMax[2]>0)) {
			//set finished flag to true
			mod.setFinished(true);
			
			//start finish procedure 
			finishSignal();
		}
		
		//if one player has a possible move then the respective player gets next turn
		else {
			//if it is white that has a possible move then finish set to white
			finish = ((whiteMax[2]>0)?ReversiMain.WHITE_PLAYER:ReversiMain.BLACK_PLAYER);
			
			//set player to the relevant player in model
			mod.setPlayer(finish);
			
			//send relevant message to the both players
			myView.feedbackToUser(finish,(finish==ReversiMain.WHITE_PLAYER)?"White player - choose where to put your piece":"Black player - choose where to put your piece");
			myView.feedbackToUser((finish%2)+1,(finish==ReversiMain.WHITE_PLAYER)?"Black player - not your turn":"White player - not your turn" );
		}
	}
	
	//given an x and y direction to shift by, how many counters can you take 
	private int countDirect(int X, int Y, int opponent, int xPos, int yPos, int countCaptured, int take) {
		//current square variable
	    int squareValue;
	    
	    //is it on the edge or empty
	    if (isOutOfBoundsOrEmpty(xPos, yPos, X, Y)) {
	        return 0;
	    }

	    //store next shifted square's value
	    squareValue = mod.getBoardContents(xPos + X, yPos + Y);
	    
	    //if the current square is the opponents
	    if (squareValue == opponent) {
	    	//check the next square
	        countCaptured = countDirect(X, Y, opponent, xPos + X, yPos + Y, countCaptured + 1, take);
	        //if we are capturing and the number possible tokens captured is larger than 0, then set squares to current player
	        if (take == 1 && countCaptured > 0) {
	            mod.setBoardContents(xPos, yPos, (opponent % 2) + 1);
	        }
	        //return the number of possible captures in this recursion
	        return countCaptured;
	        
	        //if the square is the players token or possible tokens captures is larger than 0, set square to current player
	    } else if (squareValue == ((opponent % 2) + 1) && countCaptured > 0) {
	        if (take == 1) {
	            mod.setBoardContents(xPos, yPos, (opponent % 2) + 1);
	        }
	        //return the current number of captures up till now
	        return countCaptured;
	    }
	    //if it leads to an empty square then return zero i.e no possible capture
	    return 0;
	}

	
	//helper function to check if on the edge or empty square
	private boolean isOutOfBoundsOrEmpty(int x, int y, int oX, int oY) {
	    int newX = x + oX;
	    int newY = y + oY;
	    return (newX < 0 || newX >= 8 || newY < 0 || newY >= 8 || mod.getBoardContents(newX, newY) == 0);
	}

	//function to count the surrounding directions of a given square
	public int countSurround(int player, int x, int y, int take) {
		
		//max capture variable to store max number of possible captures
	    int maxCaptured = 0;
	    //current square
	    int square;
	    //number of squares captured in a direction (reset to zero for different shifts)
	    int captured=0;
	    //opponent
	    int opponent;
	    
	    //iterate through each y offset direction to check in each y direction
	    for (int offsetY = -1; offsetY < 2; offsetY++) {
	    	//iterate through each x offset direction to check every x direction
	        for (int offsetX = -1; offsetX < 2; offsetX++) {
	        	//if not the current square and on the boundary or empty
	            if (!(offsetX == 0 && offsetY == 0) && !isOutOfBoundsOrEmpty(x, y, offsetX, offsetY)) {
	            	//get contents of the square at the new shifted location
	            	square = mod.getBoardContents(offsetX+x, offsetY+y);
	            	//if that square is not the current players
	            	if (square!=player) {
	            		//set opponent var and count number of captures in the given direction
		                opponent = (player % 2) + 1;
		                captured = countDirect(offsetX, offsetY, opponent, x + offsetX, y + offsetY, 1, take);
	            	}
	            	//add the number of tokens captured by that square in the given direction to total max captured
	                maxCaptured += captured;
	                //reset the directional capture total
	                captured = 0;
	            }
	        }
	    }
	    //return the maximum capture (how many will be captured placing a token at a given square)
	    return maxCaptured;
	}

	
	//count the possible captures and store in a given array as well as modifying the best capture array for a player
	public void countCapture(int player, int[][] arr) {
		// row and col variable to keep track of rows and cols of best capture
	    int row = 0;
	    int col = 0;
	    // max capture which will store the best capture
	    int maxCaptured = 0;
	    
	    //variable to keep track of the current square value
	    int squareValue;
	    
	    //the relevant best capture array for either player
	    int[] pArr = (player == ReversiMain.WHITE_PLAYER) ? whiteMax : blackMax;

	    
	    //iterate through each square on the board
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	        	//store the current square value
	            squareValue = mod.getBoardContents(i, j);
	            //if the square is empty then count the surrounding possible captures without taking any squares
	            if (squareValue == ReversiMain.EMPTY_SQUARE) {
	                arr[i][j] = countSurround(player, i, j, 0);
	                //if the number captured is bigger than current max capture then store in max capture
	                if (arr[i][j] > maxCaptured) {
	                    row = i;
	                    col = j;
	                    maxCaptured = arr[i][j];
	                }
	              
	            //else in the array store square as taken square (-1)
	            } else {
	                arr[i][j] = ReversiMain.TAKEN_SQUARE;
	            }
	        }
	    }
	    
	    //after whole grid is searched, store the details of the best capture
	    pArr[0] = row;
	    pArr[1] = col;
	    pArr[2] = maxCaptured;
	}

	
	
	
	
	
	
	//helper function to print the capture arrays
	public void printArr(int [][]arr) {
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				System.out.print(arr[j][i]);
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println("------------------------------------");
	}
	
	//helper function to print the models data
	public void printModGrid() {
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				System.out.print(mod.getBoardContents(j,i));
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println("------------------------------------");
	}
	
	//when a square is selected this will be called
	@Override
	public void squareSelected(int player, int x, int y) {
		//get the current player
		int currentPlayer = mod.getPlayer();
		//if it is the correct player whose turn it is
		if (currentPlayer==player) {
			//check if the relevant player has a possible move at that square
			if(!(((player==ReversiMain.WHITE_PLAYER)?whiteArr:blackArr)[x][y]==0)) {
				//set the square to that player in the model
				mod.setBoardContents(x, y, player);	
				//count the surrounding area of that square whilst taking where possible
				this.countSurround(player,x,y,1);
				//check if this move has finished the game
				this.checkFinished(player);
				//set to the next player
				mod.setPlayer((player%2)+1);
				
			}
			
		}
		//if not the correct plater then send message to respective player that it is not their turn
		else {
			myView.feedbackToUser(player,(player==1)?"No, White player - not your turn":"No, Black player - not your turn");
		}
		
		//refresh the GUI view to reflect changes made
		myView.refreshView();

	}
	
	//check to see if the game has been finished and manage the end game procedure
	public void checkFinished(int player) {
		//check player possible capture arrays to see if any possibe captures
		this.updateCaptureCount();
		
		//variable to store the next player
		int finish;
		
		//if white and black have a possible move, give the turn to the next player
		if (whiteMax[2]>0&&blackMax[2]>0) {
			//if current player is white then give the next turn to black and vice versa
			myView.feedbackToUser((player%2)+1,(player==ReversiMain.WHITE_PLAYER)?"Black player - choose where to put your piece":"White player - choose where to put your piece");
			myView.feedbackToUser((player), (player==ReversiMain.WHITE_PLAYER)?"White player - not your turn":"Black player - not your turn");;//valid move for both
		}
		//if there are no possible moves
		else if (!(whiteMax[2]>0||blackMax[2]>0)) {
			
			//set finished flag to true and call finish procedure
			mod.setFinished(true);
			finishSignal();
		}
		//if valid move for only one player
		else {
			//determine which player has a move
			finish = ((whiteMax[2]>0)?1:2);
			//set player to the only player that can play
			mod.setPlayer(finish);
			//send the relevant feedback to the correct player
			myView.feedbackToUser(finish,(finish==ReversiMain.WHITE_PLAYER)?"White player - choose where to put your piece":"Black player - choose where to put your piece");
			myView.feedbackToUser((finish%2)+1,(finish==ReversiMain.WHITE_PLAYER)?"Black player - not your turn":"White player - not your turn" );
		}
	}
	
	//greedy algorithm function aka (plutus)
	@Override
	public void doAutomatedMove(int player) {
		//determine which player best move array we are using
		int[] arr = (player==ReversiMain.WHITE_PLAYER)?whiteMax:blackMax;
		//select the best move
		this.squareSelected(player, arr[0], arr[1]);

	}

}
