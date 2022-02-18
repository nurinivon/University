/*
 * PieceWorker is a sub class for Employee. it is an employee which his earnings calculated by the amount of pieces he created and the payment for each piece
 */
public class PieceWorker extends Employee{
	//unique attributes for PieceWorker object in addition to Employee
	private int pieces;
	private double paymentPerPiece;
	//constants variables used by this class
	private final int MIN_PIECES_NUMBER = 0;
	private final double MIN_PAYMENT_PER_PIECE = 0.0;
	
	//constructor for PieceWorker object, first construct an Employee object and only then initializing PieceWorker's attributes
	public PieceWorker(String firstName, String lastName, String SSN, int year, int month, int day, int pieces, double paymentPerPiece) {
		super(firstName, lastName, SSN, year, month, day);
		if(paymentPerPiece <= MIN_PAYMENT_PER_PIECE) {
			throw new IllegalArgumentException("payment per piece must be > 0.0");
		}
		if(pieces < MIN_PIECES_NUMBER) {
			throw new IllegalArgumentException("number of pieces must be >= 0.0");
		}
		this.pieces = pieces;
		this.paymentPerPiece = paymentPerPiece;
	}
	
	//setters for PieceWorker attributes
	public void setPieces(int pieces) {
		if(pieces < MIN_PIECES_NUMBER) {
			throw new IllegalArgumentException("number of pieces must be >= 0.0");
		}
		this.pieces = pieces;
	}
	
	public void setPaymentPerPiece(double paymentPerPiece) {
		if(paymentPerPiece <= MIN_PAYMENT_PER_PIECE) {
			throw new IllegalArgumentException("payment per piece must be > 0.0");
		}
		this.paymentPerPiece = paymentPerPiece;
	}
	
	//getters for PieceWorker attributes
	public int getPieces() {
		return this.pieces;
	}
	
	public double getPaymentPerPiece() {
		return this.paymentPerPiece;
	}
	
	//the implementation of abstract method earnings unique to PieceWorker
	public double earnings() {
		return getPieces() * getPaymentPerPiece();
	}
	
	//the implementation of toString method uses the base string from the Employee class and extends it accordingly
	public String toString() {
		return String.format("Piece Worker: %s%nPayment Per Piece: $%,.2f%nNumber Of Pieces: %d",super.toString(), this.getPaymentPerPiece(), this.getPieces());
	}
}
