import java.util.ArrayList;
import java.util.Random;
/**
 * Class which implements the min max alpha beta pruning algorithm
 * @author Raymond Arias
 *
 */
public class AlphaBetaPruning {
	private Random rand;
	/**
	 * Constructor for AlphaBetaPruning Object
	 */
	public AlphaBetaPruning()
	{
		rand = new Random(System.currentTimeMillis());
		
	}
	/**
	 * Used to generate all the successors of a state
	 * @param board
	 * @return List of successor
	 */
	public ArrayList<Board> getSuccessors(Board board, String turnValue) {
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

	
	public Board alphaBetaAlgorithm(Board state, boolean maxPlayer, 
			double alpha, double beta, int depth, long endingTime){
		//If game is over return this state
		if(state.isGameOver())
		{
			return state;
		}
		//Check if the time is over or the max depth is hit
		if(depth == 0 || System.currentTimeMillis() + 200 >= endingTime)
		{
			//Run evalution function for non leaf state
			double evaluationFunction = this.evaluation(state);
			//set the state's utility function
			state.setUtilityFunction(evaluationFunction);
			return state;
		}
		//If it max's turn find the max in sucessors
		if(maxPlayer)
		{
			Board bestBoard = state;
			double bestValue = -Double.MAX_VALUE;
			//Get all successors for this state
			ArrayList<Board> children = this.getSuccessors(state, "X");
			//If there are no sucessors simply return this state
			if(children.size() == 0)
			{
				return state;
			}
			// Variables to save the best move
			int bestRow = state.getRowMove();
			int bestCol = state.getColMove();
			//randomly choose a successor first
			int randIndex = rand.nextInt(children.size());
			bestRow = children.get(randIndex).getRowMove();
			bestCol = children.get(randIndex).getColMove();
			//Iterate through all children
			for (Board child: children)
			{
				//Run min algorithm on successors
				Board valueBoard = alphaBetaAlgorithm(child, false, alpha, beta, depth - 1, endingTime);
				//Get the best utility function
				double value = valueBoard.getUtilityFunction();
				//if the value is better than the bestValue
				//a better state was found so set it to bestValue
				if(value > bestValue)
				{
					bestValue = value;
					//Set the best board found so far
					bestBoard = valueBoard;
					//Save the move for this best board
					bestRow = child.getRowMove();
					bestCol = child.getColMove();
				}
				//Prune branches that do not lead to better choices
				if(value >= beta)
				{
					break;
				}
				//set alpha to whatever variable is larger
				//between alpha max
				alpha = Math.max(alpha, value);
			}
			//set the moves for the best board found
			bestBoard.setMoves(bestRow, bestCol);
			//return best board
			return bestBoard;
		}
		//Algorithm runs the same for min just everything is minimized
		else
		{
			Board bestBoard = state;
			double bestValue = Double.MAX_VALUE;
			int bestRow = state.getRowMove();
			int bestCol = state.getColMove();
			ArrayList<Board> children = this.getSuccessors(state, "O");
			if(children.size() == 0)
			{
				return state;
			}
			int randIndex = rand.nextInt(children.size());
			bestRow = children.get(randIndex).getRowMove();
			bestCol = children.get(randIndex).getColMove();
			for(Board child: children)
			{
				Board valueBoard = alphaBetaAlgorithm(child, true, alpha, beta, depth - 1, endingTime);
				double value = valueBoard.getUtilityFunction();
				if(value < bestValue)
				{
					bestValue = value;
					bestBoard = valueBoard;
					bestRow = child.getRowMove();
					bestCol = child.getColMove();
				}
				
				if(value <= alpha)
				{
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
	 * @param board
	 * @return
	 */
	public double evaluation(Board board)
	{
		String [][]boardConfig = board.getBoardConfig();
		double eval = 0.0;
		for(int i = 0; i < boardConfig.length; i++)
		{
			for(int j = 0; j < boardConfig[i].length; j++)
			{
				eval += evalPiece(i, j, boardConfig);
				
			}
		}
		return eval;
	}
	/**
	 * Calculates an evaluation for each piece on the board
	 * @param i
	 * @param j
	 * @param board
	 * @return
	 */
	public double evalPiece(int i, int j, String [][]board)
	{
		double evalPiece = 0.0;
		//The computer's piece on the board
		if(board[i][j].equals("X"))
		{
			double compPieces = 0.2;
			//Check the pieces in the same row
			//Check if the next piece over is a computer piece
			if(j + 1 < 8 && board[i][j + 1].equals("X"))
			{
				compPieces += 0.4;
				//Check if the piece two moves down is the computer's
				if(j + 2 < 8 && board[i][j + 2].equals("X"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the computer's
					if(j + 3 < 8 && board[i][j + 3].equals("X")){
						compPieces += 0.8;
					}
				}
			}
			//Check the pieces to the right
			if(j - 1 > -1 && board[i][j - 1].equals("X"))
			{
				compPieces += 0.4;
				//Check if the piece two moves down is the computer's
				if(j - 2 > -1 && board[i][j - 2].equals("X"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the computer's
					if(j - 3 > -1 && board[i][j - 3].equals("X")){
						compPieces += 0.8;
					}
				}
			}
			//Check if the piece next to this move is blank
			if(j + 1 < 8 && board[i][j + 1].equals("-"))
			{
				//Check if the piece two moves down is the computer's
				if(j + 2 < 8 && board[i][j + 2].equals("X"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the computer's
					if(j + 3 < 8 && board[i][j + 3].equals("X")){
						compPieces += 0.8;
					}
				}
				
			}
			//Check if the move next to this move is blank
			if(j - 1 > -1 && board[i][j - 1].equals("-"))
			{
				//Check if the piece two moves down is the computer's
				if(j - 2 > -1 && board[i][j - 2].equals("X"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the computer's
					if(j - 3 > -1 && board[i][j - 3].equals("X")){
						compPieces += 0.8;
					}
				}
			}
			//Check the pieces in the column row
			//Check if the next piece over is a computer piece
			if(i + 1 < 8 && board[i + 1][j].equals("X"))
			{
				compPieces += 0.4;
				//Check if the piece two moves down is the computer's
				if(i + 2 < 8 && board[i + 2][j].equals("X"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the computer's
					if(i + 3 < 8 && board[i + 3][j].equals("X")){
						compPieces += 0.8;
					}
				}
			}
			//Check the pieces in the column row
			//Check if the next piece over is a computer piece
			if(i - 1 > -1 && board[i - 1][j].equals("X"))
			{
				compPieces += 0.4;
				//Check if the piece two moves down is the computer's
				if(i - 2 > -1 && board[i - 2][j].equals("X"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the computer's
					if(i - 3 > -1 && board[i - 3][j].equals("X")){
						compPieces += 0.8;
					}
				}
			}
			//Check if the move next to this move is blank
			if(i + 1 < 8 && board[i + 1][j].equals("-"))
			{
				//Check if the piece two moves down is the computer's
				if(i + 2 < 8 && board[i + 2][j].equals("X"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the computer's
					if(i + 3 < 8 && board[i + 3][j].equals("X")){
						compPieces += 0.8;
					}
				}
				
			}
			//Check if the move next to this move is blank
			if(i - 1 > -1 && board[i - 1][j].equals("-"))
			{
				//Check if the piece two moves down is the computer's
				if(i - 2 > -1 && board[i - 2][j].equals("X"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the computer's
					if(i - 3 > -1 && board[i - 3][j].equals("X")){
						compPieces += 0.8;
					}
				}
				
			}
			return compPieces;
		}
		else if(board[i][j].equals("0"))
		{
			double compPieces = 0.2;
			//Check the pieces in the same row
			//Check if the next piece over is a player piece
			if(j + 1 < 8 && board[i][j + 1].equals("O"))
			{
				compPieces += 0.4;
				//Check if the piece two moves down is the player's
				if(j + 2 < 8 && board[i][j + 2].equals("O"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the player's
					if(j + 3 < 8 && board[i][j + 3].equals("O")){
						compPieces += 0.8;
					}
				}
			}
			//Check the pieces to the right
			if(j - 1 > -1 && board[i][j - 1].equals("O"))
			{
				compPieces += 0.4;
				//Check if the piece two moves down is the player's
				if(j - 2 > -1 && board[i][j - 2].equals("O"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the player's
					if(j - 3 > -1 && board[i][j - 3].equals("O")){
						compPieces += 0.8;
					}
				}
			}
			//Check if the piece next to this move is blank
			if(j + 1 < 8 && board[i][j + 1].equals("-"))
			{
				//Check if the piece two moves down is the play's
				if(j + 2 < 8 && board[i][j + 2].equals("O"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the player's
					if(j + 3 < 8 && board[i][j + 3].equals("O")){
						compPieces += 0.8;
					}
				}
				
			}
			//Check if the move next to this move is blank
			if(j - 1 > -1 && board[i][j - 1].equals("-"))
			{
				//Check if the piece two moves down is the player's
				if(j - 2 < 8 && board[i][j - 2].equals("O"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the player's
					if(j - 3 < 8 && board[i][j - 3].equals("O")){
						compPieces += 0.8;
					}
				}
			}
			//Check the pieces in the column row
			//Check if the next piece over is a player piece
			if(i + 1 < 8 && board[i + 1][j].equals("O"))
			{
				compPieces += 0.4;
				//Check if the piece two moves down is the player's
				if(i + 2 < 8 && board[i + 2][j].equals("O"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the player's
					if(i + 3 < 8 && board[i + 3][j].equals("O")){
						compPieces += 0.8;
					}
				}
			}
			//Check the pieces in the column row
			//Check if the next piece over is a player piece
			if(i - 1 > -1 && board[i - 1][j].equals("O"))
			{
				compPieces += 0.4;
				//Check if the piece two moves down is the player's
				if(i - 2 > -1 && board[i - 2][j].equals("O"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the player's
					if(i - 3 > -1 && board[i - 3][j].equals("O")){
						compPieces += 0.8;
					}
				}
			}
			//Check if the move next to this move is black
			if(i + 1 < 8 && board[i + 1][j].equals("-"))
			{
				//Check if the piece two moves down is the player's
				if(i + 2 < 8 && board[i + 2][j].equals("O"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the player's
					if(i + 3 < 8 && board[i + 3][j].equals("O")){
						compPieces += 0.8;
					}
				}
				
			}
			//Check if the move next to this move is blank
			if(i - 1 > -1 && board[i - 1][j].equals("-"))
			{
				//Check if the piece two moves down is the player's
				if(i - 2 > -1 && board[i - 2][j].equals("O"))
				{
					compPieces += 0.6;
					//Check if the piece three moves down is the player's
					if(i - 3 > -1 && board[i - 3][j].equals("O")){
						compPieces += 0.8;
					}
				}
				
			}
			return -compPieces;
		}
		return evalPiece;
	}

}
