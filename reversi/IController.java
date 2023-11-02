package reversi;

public interface IController{
	/**
	 * Initialise the controller for a model and view
	 * @param model The model to use
	 * @param view The view to use.
	 */
	void initialise( IModel model, IView view );

	
	/**
	 * Start the game again - set up view and board appropriately
	 * Set the player number and finished flag to the correct values as well as clearing the board.
	 */
	void startup();
	
	/**
	 * If the controller uses the finished flag, then it should look at the board and set the finished flag or not according to whether the game has finished.
	 * If the controller use the player number, then it should check it in case it changed.
	 * Also set the feedback to the user according to which player is the current player (model.getPlayer()) and whether the game has finished or not.
	 */
	void update();
	
	/**
	 * Called by view when some position on the board is selected
	 * @param player Which player clicked the square, 1 (white) or 2 (black).
	 * @param x The x coordinate of the square which is clicked
	 * @param y The y coordinate of the square which is clicked
	 */
	void squareSelected( int player, int x, int y );
	
	
	/**
	 * Called when view requests an automated move.
	 * @param player Which player the automated move should happen for, 1 (white) or 2 (black).
	 */
	void doAutomatedMove( int player );
}
