import java.util.ArrayList;
import java.util.Random;
/*
 * LifeMatrix is a class implements all the "Life Game" logic.
 * LifeMatrix object has Boolean matrix initialize with random values.
 */
public class LifeMatrix {
	private Boolean[][] matrix; //the matrix attribute of LifeMatrix object
	private Random rand; //random object to initialize the matrix
	
	//constant variables
	private final int MATRIX_SIZE = 10;
	private final int NEIGHBOR_DISTANCE = 1;
	private final int MIN_INDEX = 0;
	private final int MAX_INDEX = 9;
	private final int BIRTH_CONDITION = 3;
	private final int MIN_ALIVE_ALLOWED_NEIGHBORS = 2;
	private final int MAX_ALIVE_ALLOWED_NEIGHBORS = 3;
	
	//constructor for LifeMatrix, create new matrix and initializes it with random values.
	public LifeMatrix() {
		this.matrix = new Boolean[MATRIX_SIZE][MATRIX_SIZE];
		this.rand = new Random();
		for(int i = 0; i < MATRIX_SIZE; i++) {
			for(int j = 0; j < MATRIX_SIZE; j++) {
				this.matrix[i][j] = rand.nextBoolean();
			}
		}
	}
	
	//getter for the instance's matrix
	public Boolean[][] getMatrix(){
		return this.matrix;
	}
	
	/*
	 * nextGeneration is the class implements the game logic.
	 * the game rules are:
	 * if a cell is alive (true)
	 *     if it has 2 or 3 neighbors alive (true) he will keep living
	 *     else - it will turn into dead (false)
	 * if a cell is dead (false)
	 *     if it has exactly 3 neighbors alive (true) he will turn alive (true)
	 *     else - it will keep being dead (false)
	 * in order to meet with the game's requirements first every cell will be checked and only then changes of cells will be done if needed.
	 */
	public int nextGeneration() {
		/*
		 * changes holds the coordinates(i and j) of a cell needs to change is value (from true to false or false to true)
		 */
		ArrayList<Coordinates> changes = new ArrayList<Coordinates>();
		for(int i = 0; i < this.matrix.length; i++) {
			for(int j = 0; j < this.matrix[i].length; j++) {
				int neighborsAlive = 0;
				//for each neighbor check, first need to check the indexes are not out of matrix's bounds
				if(i > MIN_INDEX && j > MIN_INDEX && this.matrix[i - NEIGHBOR_DISTANCE][j - NEIGHBOR_DISTANCE]) {
					neighborsAlive++;
				}
				if(j > MIN_INDEX && this.matrix[i][j - NEIGHBOR_DISTANCE]) {
					neighborsAlive++;
				}
				if(i < MAX_INDEX && j > MIN_INDEX && this.matrix[i + NEIGHBOR_DISTANCE][j - NEIGHBOR_DISTANCE]) {
					neighborsAlive++;
				}
				if(i < MAX_INDEX && this.matrix[i + NEIGHBOR_DISTANCE][j]) {
					neighborsAlive++;
				}
				if(i < MAX_INDEX && j < MAX_INDEX && this.matrix[i + NEIGHBOR_DISTANCE][j + NEIGHBOR_DISTANCE]) {
					neighborsAlive++;
				}
				if(j < MAX_INDEX && this.matrix[i][j + NEIGHBOR_DISTANCE]) {
					neighborsAlive++;
				}
				if(i > MIN_INDEX && j < MAX_INDEX && this.matrix[i - NEIGHBOR_DISTANCE][j + NEIGHBOR_DISTANCE]) {
					neighborsAlive++;
				}
				if(i > MIN_INDEX && this.matrix[i - NEIGHBOR_DISTANCE][j]) {
					neighborsAlive++;
				}
				if(this.matrix[i][j]) {
					if(neighborsAlive < MIN_ALIVE_ALLOWED_NEIGHBORS || neighborsAlive > MAX_ALIVE_ALLOWED_NEIGHBORS) {
						changes.add(new Coordinates(i,j));
					}
				}else {
					if(neighborsAlive == BIRTH_CONDITION) {
						changes.add(new Coordinates(i,j));
					}
				}
			}
		}
		// only after checking the whole matrix
		// iterate changes need to be done - a change means that a cell just turn into the opposite of its current value.
		for(Coordinates change : changes) {
			this.matrix[change.getI()][change.getJ()] = !this.matrix[change.getI()][change.getJ()];
		}
		return changes.size();
	}
}
