import java.util.ArrayList;
import java.util.Random;

/**
 * Class which implements the min max alpha beta pruning algorithm
 * 
 * @author Raymond Arias.
 *
 */
public class AlphaBetaPruning {
	private Random rand;
	
	/**
	 * Constructor for AlphaBetaPruning Object
	 */
	public AlphaBetaPruning() {
		rand = new Random(System.currentTimeMillis());

	}

	/**
	 * Used to generate all the successors of a state
	 * 
	 * @param board
	 * @return List of successor
	 */
	public ArrayList<Board> getSuccessors(Board board, String turnValue, int lastRow, int lastCol) {
		ArrayList<Board> successors = new ArrayList<>(100);
		String[][] state = board.getBoardConfig();
		
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[i].length; j++) {
				// Check to see if move is allowed
				if (board.isAllowedMove(i, j)) {
					String[][] newBoard = board.copy();
					// Create a successor
					newBoard[i][j] = turnValue;
					Board successor = new Board(newBoard);
					successor.setMoves(i, j);
					// calculate its ultility function;
					successor.calcTerminalState(i, j, turnValue);
					// add to successors arraylist
					successors.add(successor);
				}
				
			}
			
			
		}
		return successors;
	}

	public Board alphaBetaAlgorithm(Board state, boolean maxPlayer, double alpha, double beta, int depth,
			long endingTime) {
		// If game is over return this state
		if (state.isGameOver()) {
			return state;
		}
		// Check if the time is over or the max depth is hit
		if (depth == 0 || System.currentTimeMillis() >= endingTime) {
			
			// Run evalution function for non leaf state
			double evaluationFunction = this.evaluation(state);
			// set the state's utility function
			state.setUtilityFunction(evaluationFunction);
			return state;
		}
		// If it max's turn find the max in sucessors
		if (maxPlayer) {
			Board bestBoard = state;
			double bestValue = -Double.MAX_VALUE;
			// Variables to save the best move
			int bestRow = state.getRowMove();
			int bestCol = state.getColMove();
			// Get all successors for this state
			ArrayList<Board> children = this.getSuccessors(state, "X", bestRow, bestCol);
			// If there are no sucessors simply return this state
			if (children.size() == 0) {
				return state;
			}
			
			// Iterate through all children
			for (Board child : children) {
				// Run min algorithm on successors
				Board valueBoard = alphaBetaAlgorithm(child, false, alpha, beta, depth - 1, endingTime);
				// Get the best utility function
				double value = valueBoard.getUtilityFunction();
				// if the value is better than the bestValue
				// a better state was found so set it to bestValue
				if (value > bestValue) {
					bestValue = value;
					// Set the best board found so far
					bestBoard = valueBoard;
					// Save the move for this best board
					bestRow = child.getRowMove();
					bestCol = child.getColMove();
				}
				// Prune branches that do not lead to better choices
				if (value >= beta) {
					break;
				}
				// set alpha to whatever variable is larger
				// between alpha max
				alpha = Math.max(alpha, value);
			}
			// set the moves for the best board found
			bestBoard.setMoves(bestRow, bestCol);
			return bestBoard;
		}
		// Algorithm runs the same for min just everything is minimized
		else {
			Board bestBoard = state;
			double bestValue = Double.MAX_VALUE;
			int bestRow = state.getRowMove();
			int bestCol = state.getColMove();
			ArrayList<Board> children = this.getSuccessors(state, "O", bestRow, bestCol);
			if (children.size() == 0) {
				return state;
			}
			for (Board child : children) {
				Board valueBoard = alphaBetaAlgorithm(child, true, alpha, beta, depth - 1, endingTime);
				double value = valueBoard.getUtilityFunction();
				if (value < bestValue) {
					bestValue = value;
					bestBoard = valueBoard;
					bestRow = child.getRowMove();
					bestCol = child.getColMove();
				}

				if (value <= alpha) {
					break;
				}
				beta = Math.min(beta, bestValue);
			}
			bestBoard.setMoves(bestRow, bestCol);
			return bestBoard;
		}

	}

	/**
	 * Calculates the evaluation function for this board
	 * 
	 * @param board
	 * @return
	 */
	public double evaluation(Board board) {
		String[][] boardConfig = board.getBoardConfig();
		double eval = 0.0;
		for (int i = 0; i < boardConfig.length; i++) {
			for (int j = 0; j < boardConfig[i].length; j++) {
				if (!boardConfig[i][j].equals("-")) {
					eval += evalPiece(i, j, boardConfig, boardConfig[i][j], board);	
				}
			}
		}
		return eval;
	}

	/**
	 * Calculates an evaluation for each piece on the board
	 * 
	 * @param i
	 * @param j
	 * @param board
	 * @return
	 */
	public double evalPiece(int i, int j, String[][] board, String value, Board config) {
		double compPieces = 0.2;

		// Check the pieces in the same row
		// Check if the next piece over is a computer piece
		if (j + 1 < 8 && board[i][j + 1].equals(value)) {
			compPieces += 0.4;
			if((j + 2 < 8 && board[i][j + 2].equals("-")) && (j - 1 > -1 && board[i][j - 1].equals("-")))
			{
				//-xx-
				compPieces += 0.6;
			}
			// Check if the piece two moves down is the computer's
			if (j + 2 < 8 && board[i][j + 2].equals(value)) {
				compPieces += 0.6;
				// Check if the piece three moves down is the computer's
				if (j + 3 < 8 && board[i][j + 3].equals(value)) {
					compPieces += 1.0;

				}
			}
		}
		// Check the pieces to the right
		if (j - 1 > -1 && board[i][j - 1].equals(value)) {
			compPieces += 0.4;
			if((j + 1 < 8 && board[i][j + 1].equals("-")) && (j - 2 > -1 && board[i][j - 2].equals("-")))	
			{
				compPieces += 0.6;
			}
			// Check if the piece two moves down is the computer's
			if (j - 2 > -1 && board[i][j - 2].equals(value)) {
				compPieces += 0.6;
				// Check if the piece three moves down is the computer's
				if (j - 3 > -1 && board[i][j - 3].equals(value)) {
					compPieces += 1.0;
				}
			}
		}
		
		// Check if the piece next to this move is blank
		if (j + 1 < 8 && board[i][j + 1].equals("-")) {
			// Check if the piece two moves down is the computer's
			if (j + 2 < 8 && board[i][j + 2].equals(value)) {
				compPieces += 0.2;
				// Check if the piece three moves down is the computer's
				if (j + 3 < 8 && board[i][j + 3].equals(value)) {
					compPieces += 1.0;
				}
			}

		}
		
		// Check if the move next to this move is blank
		if (j - 1 > -1 && board[i][j - 1].equals("-")) {
			// Check if the piece two moves down is the computer's
			if (j - 2 > -1 && board[i][j - 2].equals(value)) {
				compPieces += 0.2;
				// Check if the piece three moves down is the computer's
				if (j - 3 > -1 && board[i][j - 3].equals(value)) {
					compPieces += 1.0;
				}
			}
		}
		
		// Check the pieces in the column row
		// Check if the next piece over is a computer piece
		if (i + 1 < 8 && board[i + 1][j].equals(value)) {
			compPieces += 0.4;
			if((i + 2 < 8 && board[i + 2][j].equals("-")) && (i - 1 > -1 && board[i-1][j].equals("-")))	
			{
				compPieces += 0.6;
			}
			// Check if the piece two moves down is the computer's
			else if (i + 2 < 8 && board[i + 2][j].equals(value)) {
				compPieces += 0.6;
				// Check if the piece three moves down is the computer's
				if (i + 3 < 8 && board[i + 3][j].equals(value)) {
					compPieces += 1.0;
				}
			}
		}
		// Check the pieces in the column row
		// Check if the next piece over is a computer piece
		if (i - 1 > -1 && board[i - 1][j].equals(value)) {
			compPieces += 0.4;
			if((i - 2 > -1 && board[i - 2][j].equals("-")) && (i + 1 < 8 && board[i+1][j].equals("-")))	
			{
				compPieces += 0.6;
			}
			// Check if the piece two moves down is the computer's
			if (i - 2 > -1 && board[i - 2][j].equals(value)) {
				compPieces += 0.6;
				// Check if the piece three moves down is the computer's
				if (i - 3 > -1 && board[i - 3][j].equals(value)) {
					compPieces += 1.0;
				}
			}
		}
		
		// Check Killer moves
		if (j + 1 < 8 && j + 2 < 8 && j + 3 < 8 && j - 1 > -1 && board[i][j + 1].equals(value)
				&& board[i][j + 2].equals(value) && board[i][j + 3].equals("-") && board[i][j - 1].equals("-")) {
			// Board config if -xxx-. Killer Move
			compPieces += 5.0;
			if((j + 4 < 8 && board[i][j + 4].equals("-")) || (j - 2 > -1 && board[i][j - 2].equals("-")))
				compPieces += 1.0;
		}
		if (i + 1 < 8 && i + 2 < 8 && i + 3 < 8 && i - 1 > -1 && board[i + 1][j].equals(value)
				&& board[i + 2][j].equals(value) && board[i + 3][j].equals("-") && board[i - 1][j].equals("-")) {
			// Killer Move Row Wise
			// Board Config
			// -
			// x
			// x
			// x
			// -

			compPieces += 5.0;
			if((i + 4 < 8 && board[i + 4][j].equals("-")) || (i - 2 > -1 && board[i - 2][j].equals("-")))
				compPieces += 1.0;
		}
		if (j - 1 > -1 && j - 2 > -1 && j - 3 > -1 && j + 1 < 8 && board[i][j - 1].equals(value)
				&& board[i][j - 2].equals(value) && board[i][j - 3].equals("-") && board[i][j + 1].equals("-")) {
			// Board config if -xxx-. Killer Move
			compPieces += 5.0;
			if((j - 4 > -1 && board[i][j - 4].equals("-")) || (j + 2 < 8 && board[i][j + 2].equals("-")))
				compPieces += 1.0;
		}
		if (i - 1 > -1 && i - 2 > -1 && i - 3 > -1 && i + 1 < 8 && board[i - 1][j].equals(value)
				&& board[i - 2][j].equals(value) && board[i - 3][j].equals("-") && board[i + 1][j].equals("-")) {
			// Killer Move Row Wise
			// Board Config
			// -
			// x
			// x
			// x
			// -
			compPieces += 5.0;
			if((i - 4 > -1 && board[i - 4][j].equals("-")) || (i + 2 < 8 && board[i + 2][j].equals("-")))
				compPieces += 1.0;
		}
		
		// return compPieces;
		// The computer's piece on the board
		if (value.equals("X")) {
			return compPieces;
		} else if (value.equals("O")) {
			return -compPieces;
		}

		return 0.0;
	}

}