import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/*
 * Controller is the class that uses as controller to handle the GUI's events
 */
public class Controller {
	
	//myCanvas is automatically initialize to canvas node on the window
	@FXML
	private Canvas myCanvas;
	
	private Color[] colors = {Color.BLACK, Color.RED, Color.YELLOW, Color.PURPLE, Color.ORANGE, Color.BROWN, Color.GREEN, Color.PINK, Color.MINTCREAM, Color.PEACHPUFF, Color.MISTYROSE, Color.OLIVE, Color.LIGHTSKYBLUE};
	
	//private attributes for the controller methods
	private final int START_X = 0;
	private final int START_Y = 0;
	private final int BORDER_START_MARGIN = 5;
	private final int BORDER_END_MARGIN = 10;
	private final Color BACKGROUND_COLOR = Color.BLUE;
	private final Color BORDER_COLOR = Color.BLACK;
	private final double LINE_WIDTH = 3.0;
	private final int NUMBER_OF_SHAPES = 10;
	//enum for the possible shapes types
	private enum SHAPE_TYPE{
		STROKE,
		RECT,
		OVAL
	}
	
	/*
	 * generateShapes is the method the creates random shapes on the canvas node.
	 * the method start with clearing the canvas and then drawing randoms shapes on the canvas.
	 */
	public void generateShapes(ActionEvent e) {
		int canvasWidth = (int) myCanvas.getWidth();
		int canvasHeight = (int) myCanvas.getHeight();
		
		GraphicsContext gc = myCanvas.getGraphicsContext2D();
		initializeCanvas(gc, canvasWidth, canvasHeight);		
		for(int i = 0; i < NUMBER_OF_SHAPES; i++) {
			drawRandomShape(gc, canvasWidth, canvasHeight);
		}
	}
	
	/*
	 * initializeCanvas is a method that preparing the canvas before drawing shapes.
	 * it clears the canvas from any graphic content, set background color, border, and line width (for lines that will be shown)
	 */
	public void initializeCanvas(GraphicsContext gc, int canvasWidth, int canvasHeight) {
		//clear canvas
		gc.clearRect(START_X, START_Y, canvasWidth, canvasHeight);
		//border
		gc.setFill(BORDER_COLOR);
		gc.fillRect(START_X, START_Y, canvasWidth, canvasHeight);
		//background
		gc.setFill(BACKGROUND_COLOR);
		gc.fillRect(BORDER_START_MARGIN, BORDER_START_MARGIN, (canvasWidth - BORDER_END_MARGIN), (canvasHeight - BORDER_END_MARGIN));
		//line width
		gc.setLineWidth(LINE_WIDTH);
	}
	
	/*
	 * randomColor is method that return a random color out of colors array
	 */
	public Color randomColor() {
		Random rand = new Random();
		return colors[rand.nextInt(colors.length)];
	}
	
	/*
	 * drawRandomShape is a method that gets a GraphicsContext instance and drawing a random shape on it.
	 * The shape could one of 3 types (enum types above).
	 * The method gets the canvas dimensions in order to meet the requirement that the shape height / width could not be bigger than quarter of the canvas height / width.
	 */
	public void drawRandomShape(GraphicsContext gc, int canvasWidth, int canvasHeight) {
		Random rand = new Random();
		//random shape type
		SHAPE_TYPE shapeType = SHAPE_TYPE.values()[rand.nextInt(SHAPE_TYPE.values().length)];
		//dimensions limitations
		int topWidth = canvasWidth / 4;
		int topHeight = canvasHeight / 4;
		//random starting coordinates inside the canvas
		int startX = rand.nextInt(canvasWidth);
		int startY = rand.nextInt(canvasHeight);
		//max strokes length (width and height)
		int maxX = startX + topWidth;
		int maxY = startY + topHeight;
		switch(shapeType) {
			case STROKE:
				// draw line
				gc.setStroke(randomColor());
				gc.strokeLine(startX, startY, rand.nextInt((maxX - startX) + 1) + startX, rand.nextInt((maxY - startY) + 1) + startY);
				break;
			case RECT:
				// draw rectangle
		        gc.setFill(randomColor());
		        gc.fillRect(startX, startY, rand.nextInt(topWidth), rand.nextInt(topHeight));
		        break;
			case OVAL:
				// draw oval
		        gc.setFill(randomColor());
		        gc.fillOval(startX, startY, rand.nextInt(topWidth), rand.nextInt(topHeight));
		        break;
		}
	}
}
