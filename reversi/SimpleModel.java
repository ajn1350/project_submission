package reversi;

//standard model to just play the game
public class SimpleModel implements IModel{
	
	//board array is stored here in 2d array
	int [][] boardContents;
	
	//store width and heigh of the board
	int width;
	int height;
	
	//int var to store the variable
	int player;
	
	//finished flagv(true for game has finish)
	boolean finished;
	
	//view and controller object references
	IView view;
	IController controller;
	

	 //initializes the model to have the given width and height stored as well as the view and controller object references

	@Override
	public void initialize(int width, int height, IView view, IController controller){
		this.width = width;
		this.height = width;
		this.view = view;
		this.controller = controller;
		//board contents size set based on the array
		boardContents = new int[width][height];
	}

	//used to fill the board with a given value (usually 0)
	@Override
	public void clear(int value){
		//iterate through each item on the board and set it to value
		for ( int x = 0 ; x < width ; x++ )
			for ( int y = 0 ; y < width ; y++ )
				boardContents[x][y] = value;
	}

	//getters and setters -> get or set attributes of the model (names of functions will be self descriptive)
	@Override
	public int getBoardWidth(){
		return width;
	}

	@Override
	public int getBoardHeight(){
		return height;
	}

	@Override
	public int getBoardContents(int x, int y){
		return boardContents[x][y];
	}

	@Override
	public void setBoardContents(int x, int y, int value){
		boardContents[x][y] = value;
	}

	@Override
	public void setPlayer(int player){
		this.player = player;
	}

	@Override
	public int getPlayer(){
		return player;
	}

	//essentially get Finished but more comfortable naming (--potential edit?--)
	@Override
	public boolean hasFinished(){
		return finished;
	}

	@Override
	public void setFinished(boolean finished){
		this.finished = finished;
	}
}
