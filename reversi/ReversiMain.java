package reversi;

public class ReversiMain{
	
	IModel model;
	IView view;
	IController controller;
	
	//Constants
	static final int WHITE_PLAYER = 1;
	static final int BLACK_PLAYER = 2;
	static final int EMPTY_SQUARE = 0;
	static final int TAKEN_SQUARE = -1;
	


	//Main game singleton
	ReversiMain()
	{
		//model selector (just uncomment)
		//model = new SimpleModel();
		model = new SimpleTestModel2();
		
		//gui implementation of the view (possible idea to make it CLI)
		view = new GUIView();
		
		
		//controller implementation
		controller = new ReversiController();
		
		
		//initialize all objects
		model.initialize(8, 8, view, controller);
		controller.initialise(model, view);
		view.initialise(model, controller);

		//controller startup procedure
		controller.startup();

	}
	
	public static void main(String[] args)
	{
		new ReversiMain();
	}
	
}


