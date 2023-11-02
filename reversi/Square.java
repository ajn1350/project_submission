package reversi;
import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial") // not sure if this is right (--potential edit?--)

//Square class
public class Square extends JButton {
	
	//current color of the square
	private Color currentCol;
	//object reference store to store the controller
	IController control;
	
	//2 ints to store the x and y pos of the square
	int xPos;
	int yPos;

	//initialising a square object based on passed params
	public Square(int width,int height,Color borderCol, Color draw, IController controlRef,int i, int j) {
		//store the controller object reference
		this.control = controlRef;
		//create a border to each square
		setBorder(BorderFactory.createLineBorder(borderCol));
		//fill background to draw color (green)
	    setBackground(draw);
	    //min size is the passed width and height and same as the preferred size
		setMinimumSize(new Dimension(width,height));
		setPreferredSize(new Dimension(width,height));
		
		//current col is not yet set
		currentCol = null;
		
		//x and y position are the passed i and j values
		xPos = i;
		yPos = j;
		
	}
	
	//another initialzer for ease of coding that just requires params: controller ref, x and y position
	public Square(IController controlReference,int i,int j) {
		this(50,50,Color.BLACK,Color.GREEN,controlReference,i,j);
	}
	
	//used to set the square (oval) to a new color (changing the counter)
	public void setCurrentColor(Color color) {
	    currentCol = color;
	    repaint();
	}
	
	//overrided JButton method to fill a token bound to the size of the square
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    //if the current color is a valid color
	    if (currentCol != null) {
	    	//then set the color to that and fill oval with the size spanning the square (rectangle)
	        g.setColor(currentCol);
	        g.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
	    }
	}
	


}
