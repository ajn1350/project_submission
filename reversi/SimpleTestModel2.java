package reversi;


import java.awt.GridLayout;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * No commenting unfortunately :(
 */

public class SimpleTestModel2 extends SimpleModel{
	Random rand = new Random();

	public void verifyBoardState( String expectedStateAsDotOneTwo ){
		for ( int y = 0 ; y < 8 ; y++ )
			for ( int x = 0 ; x < 8 ; x++ )
				switch( expectedStateAsDotOneTwo.charAt(y*9+x) ){	
				case '.':
					if ( boardContents[x][y] != 0 )
						System.err.println("Failed: Expected empty square at position (" + x + "," + y + ")");
					break;				
				case '1':
					if ( boardContents[x][y] != 1 )
						System.err.println("Failed: Expected empty player 1 piece at position (" + x + "," + y + ")");
					break;				
				case '2':
					if ( boardContents[x][y] != 2 )
						System.err.println("Failed: Expected empty player 2 piece at position (" + x + "," + y + ")");
					break;	
				default:
					System.err.println("Error: give verifyBoardState() a string to validate against with 64+7 characters which are . 1 or 2 with an extra separator every 9th character");
					return; 
				}
	}

	
	public SimpleTestModel2(){
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setTitle("TEST FACILITY");
		
		testFrame.getContentPane().setLayout(new GridLayout(12,2,5,5));
		
		testFrame.getContentPane().add( new JLabel("--- View tests: --- ", SwingConstants.CENTER ));
		testFrame.getContentPane().add( new JLabel("--- Reversi Controller tests (use them in order 1,2,3 etc): --- ", SwingConstants.CENTER));

		JButton button1 = new JButton("Almost Fill Model, clear finished flag and call update");
		button1.addActionListener( 
				e->{ 
					clear(1); 
					setBoardContents( 0, 0, 0 ); 
					setBoardContents( 1, 1, 0 ); 
					setFinished(false); 
					view.refreshView(); 
					controller.update(); 
				});
		testFrame.getContentPane().add(button1);

		JButton buttonCtrl1 = new JButton("1. Test placing pieces - setup board");
		buttonCtrl1.addActionListener( 
				e->{
					clear(0); 
					setBoardContents( 3, 3, 1 ); 
					setBoardContents( 3, 4, 2 ); 
					setBoardContents( 4, 3, 2 ); 
					setBoardContents( 4, 4, 1 ); 
					setFinished(false);
					this.setPlayer(1); 
					view.refreshView(); 
					controller.update();
					System.out.println("Testing board state before move...");
					verifyBoardState("........|........|........|...12...|...21...|........|........|........");
				});
		testFrame.getContentPane().add(buttonCtrl1);

		JButton button2 = new JButton("Almost fill, with ability to play, clear finished flag and call update");
		button2.addActionListener( 
				e->{ 
					clear(1); 
					setBoardContents( 0, 0, 0 ); 
					setBoardContents( 1, 1, 2 ); 
					setFinished(false); 
					view.refreshView(); 
					controller.update();
				});
		testFrame.getContentPane().add(button2);
		
		JButton buttonCtrl2 = new JButton("2. White captures piece ...");
		buttonCtrl2.addActionListener( 
				e->{
					controller.squareSelected(1, 5, 3);
					System.out.println("Testing board state after white move...");
					verifyBoardState("........|........|........|...111..|...21...|........|........|........");
					if ( this.getPlayer() != 2 )
						System.err.println("Your controller should have set player to 2 after player 1 made a move!");
				});
		testFrame.getContentPane().add(buttonCtrl2);

		JButton button3 = new JButton("Clear totally, clear finished flag and call update");
		button3.addActionListener( 
				e->{ 
					clear(0);
					setFinished(false);
					view.refreshView();
					controller.update();
				}); 
		testFrame.getContentPane().add(button3);
		
		JButton buttonCtrl3 = new JButton("3. Black captures other row...");
		buttonCtrl3.addActionListener( 
				e->{
					controller.squareSelected(2, 5, 4);
					System.out.println("Testing board state after black move...");
					verifyBoardState("........|........|........|...111..|...222..|........|........|........");
					if ( this.getPlayer() != 1 )
						System.err.println("Your controller should have set player to 1 after player 2 made a move!");
				});
		testFrame.getContentPane().add(buttonCtrl3);

		JButton button4 = new JButton("Fill black, clear finished flag and call update");
		button4.addActionListener( 
				e->{ 
					clear(2);
					setFinished(false);
					view.refreshView();
					controller.update(); 
				}); 
		testFrame.getContentPane().add(button4);
		
		JButton buttonCtrl4 = new JButton("4. White captures most of board...");
		buttonCtrl4.addActionListener( 
				e->{
					controller.squareSelected(1, 5, 5);
					System.out.println("Testing board state after white move...");
					verifyBoardState("........|........|........|...111..|...211..|.....1..|........|........");
					if ( this.getPlayer() != 2 )
						System.err.println("Your controller should have set player to 2 after player 1 made a move!");
				});
		testFrame.getContentPane().add(buttonCtrl4);

		JButton button5 = new JButton("Random fill white or black, clear finished flag and call update");
		button5.addActionListener( 
				e->{ 
					for ( int x = 0 ; x < getBoardWidth(); x++ )
						for ( int y = 0 ; y < getBoardHeight(); y++ )
							setBoardContents(  x, y, 1+rand.nextInt(2) ); setFinished(false); view.refreshView(); controller.update(); 
				});
		testFrame.getContentPane().add(button5);
		
		JButton buttonCtrl5 = new JButton("5. Black captures diagonal...");
		buttonCtrl5.addActionListener( 
				e->{
					controller.squareSelected(2, 5, 2);
					System.out.println("Testing board state after white move...");
					verifyBoardState("........|........|.....2..|...121..|...211..|.....1..|........|........");
					if ( this.getPlayer() != 1 )
						System.err.println("Your controller should have set player to 1 after player 2 made a move!");
				});
		testFrame.getContentPane().add(buttonCtrl5);

		JButton button6 = new JButton("Totally random, empty/white/black, clear finished flag and call update");
		button6.addActionListener( 
				e->{ 
					for ( int x = 0 ; x < getBoardWidth(); x++ ) 
						for ( int y = 0 ; y < getBoardHeight(); y++ ) 
							setBoardContents(  x, y, rand.nextInt(3) );
							setFinished(false); view.refreshView();
							controller.update(); 
				});
		testFrame.getContentPane().add(button6);
		
		JButton buttonCtrl6 = new JButton("6. White recaptures the middle one...");
		buttonCtrl6.addActionListener( 
				e->{
					controller.squareSelected(1, 4, 2);
					System.out.println("Testing board state after white move...");
					verifyBoardState("........|........|....12..|...111..|...211..|.....1..|........|........");
					if ( this.getPlayer() != 2 )
						System.err.println("Your controller should have set player to 2 after player 1 made a move!");
				});
		testFrame.getContentPane().add(buttonCtrl6);

		JButton button7 = new JButton("Set player 1 and call update, clear finished flag and call update");
		button7.addActionListener( 
				e->{ 
					setPlayer(1);
					setFinished(false);
					view.refreshView();
					controller.update();
				});
		testFrame.getContentPane().add(button7);

		JButton buttonCtrl7 = new JButton("7. Black takes the top and left...");
		buttonCtrl7.addActionListener( 
				e->{
					controller.squareSelected(2, 3, 2);
					System.out.println("Testing board state after white move...");
					verifyBoardState("........|........|...222..|...211..|...211..|.....1..|........|........");
					if ( this.getPlayer() != 1 )
						System.err.println("Your controller should have set player to 1 after player 2 made a move!");
				});
		testFrame.getContentPane().add(buttonCtrl7);

		JButton button8 = new JButton("Set player 2 and call update, clear finished flag and call update");
		button8.addActionListener( 
				e->{ 
					setPlayer(2);
					setFinished(false);
					view.refreshView();
					controller.update();
				});
		testFrame.getContentPane().add(button8);

		JButton buttonCtrl8 = new JButton("8. White likes diagonals...");
		buttonCtrl8.addActionListener( 
				e->{
					controller.squareSelected(1, 2, 1);
					System.out.println("Testing board state after white move...");
					verifyBoardState("........|..1.....|...122..|...211..|...211..|.....1..|........|........");
					if ( this.getPlayer() != 2 )
						System.err.println("Your controller should have set player to 2 after player 1 made a move!");
				});
		testFrame.getContentPane().add(buttonCtrl8);

		JButton button9 = new JButton("Set finished and call update");
		button9.addActionListener( 
				e->{ 
					setFinished(true); 
					view.refreshView();
					controller.update();
				});
		testFrame.getContentPane().add(button9);

		JButton buttonCtrl9 = new JButton("9. Black captures multiple directions, multiple pieces...");
		buttonCtrl9.addActionListener( 
				e->{
					controller.squareSelected(2, 6, 4);
					System.out.println("Testing board state after black move...");
					verifyBoardState("........|..1.....|...122..|...212..|...2222.|.....1..|........|........");
					if ( this.getPlayer() != 1 )
						System.err.println("Your controller should have set player to 1 after player 2 made a move!");
				});
		testFrame.getContentPane().add(buttonCtrl9);

		JButton button10 = new JButton("Set not finished and call update (to see whether it gets set again)");
		button10.addActionListener( 
				e->{ 
					setFinished(false);
					view.refreshView();
					controller.update();
				});
		testFrame.getContentPane().add(button10);
		
		JButton buttonCtrl10 = new JButton("10. White captures just one piece...");
		buttonCtrl10.addActionListener( 
				e->{
					controller.squareSelected(1, 4, 1);
					System.out.println("Testing board state after white move...");
					verifyBoardState("........|..1.1...|...112..|...212..|...2222.|.....1..|........|........");
					if ( this.getPlayer() != 2 )
						System.err.println("Your controller should have set player to 2 after player 1 made a move!");
				});
		testFrame.getContentPane().add(buttonCtrl10);

		JButton button11 = new JButton("Play AI to the end");
		button11.addActionListener( 
				e->{ 
					while( !hasFinished() ){
						controller.doAutomatedMove(1);
						controller.doAutomatedMove(2);
						view.refreshView();
						controller.update();
					} 
				});
		testFrame.getContentPane().add(button11);
		
		JButton buttonCtrl11 = new JButton("11. Black multidirection capture...");
		buttonCtrl11.addActionListener( 
				e->{
					controller.squareSelected(2, 3, 1);
					System.out.println("Testing board state after white move...");
					verifyBoardState("........|..121...|...222..|...212..|...2222.|.....1..|........|........");
					if ( this.getPlayer() != 1 )
						System.err.println("Your controller should have set player to 1 after player 2 made a move!");
				});
		
		
		testFrame.getContentPane().add(buttonCtrl11);

		testFrame.pack();
		testFrame.setVisible(true);
	}
}
