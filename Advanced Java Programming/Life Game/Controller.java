import javax.swing.JOptionPane;

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
	
	//private attributes for the controller methods
	private LifeMatrix currentMatrix;
	private Boolean isFirstTime = true;
	private final int START_X = 0;
	private final int START_Y = 0;
	private final int NUMBER_OF_RECTS = 10;
	private final int STROKE_SIZE = 2;
	
	/*
	 * buttonClicked is the method handles the on click event for the button in the window.
	 * If this is the first time the button was clicked, so a new matrix should be created, else, we can move to the next generation in the "Life Game"
	 * either way, the matrix should be drawn on the canvas.
	 */
	public void buttonClicked(ActionEvent e) {
		if(isFirstTime) {
			isFirstTime = false;// after first time initialization.
			this.currentMatrix = new LifeMatrix();
		}else {
			//if no changes left in the game the user can restart
			if(this.currentMatrix.nextGeneration() == 0) {
				int choice = JOptionPane.showConfirmDialog(null, "Game Over. Would you like to start a new one?");
				if(choice == JOptionPane.OK_OPTION) {
					this.currentMatrix = new LifeMatrix();
				}
			}
		}
		drawMatrix();
	}
	
	/*
	 * drawMatrix is the method that draw the matrix on the canvas according to the currentMatrix attribute of the controller.
	 * currentMatrix is Boolean matrix, for each "TRUE" cell, a gray rectangle will be drawn, for each "FALSE" cell - white rectangle.
	 * either way a black rectangle will be drawn in the background of those to be used as stroke.
	 */
	public void drawMatrix() {
		int canvasWidth = (int) myCanvas.getWidth();
		int canvasHeight = (int) myCanvas.getHeight();
		int rectWidth = canvasWidth / NUMBER_OF_RECTS;
		int rectHeight = canvasHeight / NUMBER_OF_RECTS;
		Boolean[][] tempMatrix = this.currentMatrix.getMatrix();
		GraphicsContext gc = myCanvas.getGraphicsContext2D();
		gc.clearRect(START_X, START_Y, canvasWidth, canvasHeight);
		for(int i = 0; i < tempMatrix.length; i++) {
			for(int j = 0; j < tempMatrix[i].length; j++) {
				//implement black stroke for the rectangles
				gc.setFill(Color.BLACK);
				gc.fillRect(j * rectWidth, i * rectHeight, (j * rectWidth) + rectWidth, (i * rectHeight) + rectHeight);
				//if cell is alive - color will be gray else - color is white
				if(tempMatrix[i][j]) {
			        gc.setFill(Color.GRAY);			
				}else {
					gc.setFill(Color.WHITE);
				}
				//draw rectangle with stroke
				gc.fillRect((j * rectWidth) + STROKE_SIZE, (i * rectHeight) + STROKE_SIZE, (j * rectWidth) + rectWidth - STROKE_SIZE, (i * rectHeight) + rectHeight - STROKE_SIZE);
			}
		}
	}
}
