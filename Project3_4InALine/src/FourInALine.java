import java.util.Arrays;
import java.util.Scanner;

public class FourInALine {
	Scanner input;
	AlphaBetaPruning abPruning;
	/**
	 * Simple constructor with does some preprocessing
	 * in order to same some computation that could be used
	 * by the alpha beta algortithm
	 */
	public FourInALine()
	{
		input = new Scanner(System.in);
		abPruning = new AlphaBetaPruning();
	}
	/**
	 * Main game loop for the program
	 */
	public void game()
	{
		//Ask who want to go first
		System.out.println("Who gets first move");
		System.out.println("1: player");
		System.out.println("2: computer");
		int firstChoice = input.nextInt();
		boolean playerMove = false;
		if(firstChoice == 1)
		{
			playerMove = true;
		}
		//Set the board to initial blank board
		Board board = new Board(Board.INITIALBOARD);
		Board currentConfig = board;
		board.printBoard();
		while(true)
		{
			if(playerMove)
			{
				//Let the user make a move
				currentConfig = userMove(currentConfig);
			}
			else {
				//Computer's move
				currentConfig = computerMove(currentConfig);
			}
			
			if(currentConfig == null)
				break;
			
			//print the current configuration of the board
			currentConfig.printBoard();
			//check if a winner is found
			if(currentConfig.isGameOver())
			{
				//Print out which player won
				if(!playerMove)
				{
					System.out.println("Computer wins");
				}
				else
				{
					System.out.println("Player wins");
				}
				break;
			}
			//Check if the game has tied
			if(currentConfig.isTieGame())
			{
				System.out.println("Tie Game");
				break;
				
			}
			//Change move to other player
			playerMove = !playerMove;
		}
		
	}
	/**
	 * Copies parent to new a board. To preserve parent board
	 * @param parent
	 * @return
	 */
	public String [][]copy (String [][]parent)
	{
		String [][] child = new String[parent.length][];
		for(int i = 0; i < parent.length; i++)
		    child[i] = Arrays.copyOf(parent[i],parent.length);
		return child;
	}
	/**
	 * Used as the user's choice
	 * @param board
	 */
	public Board userMove(Board board)
	{
		input.nextLine();
		//Get the user's move
		System.out.println("Please enter a move");
		String userChoice = input.nextLine();
		
		//Copy Old Board
		String [][]newBoardConfig = copy(board.getBoardConfig());
		int row = -1;
		//Check the number from char to number map
		if(Board.charToRowNum.containsKey(userChoice.charAt(0)))
		{
			//Get the number value mapped to a character
			row = Board.charToRowNum.get(userChoice.charAt(0));
		}
		//Convert string to column number
		int column = (int)userChoice.charAt(1) - 49;
		//Verify user choice is allowed
		while(!board.isAllowedMove(row, column))
		{
			//if the move is not allowed loop the following code
			System.out.println("Move Not Allowed");
			System.out.println("Please pick another move");
			board.printBoard();
			userChoice = input.nextLine();
			row = Board.charToRowNum.get(userChoice.charAt(0));
			column = (int)userChoice.charAt(1) - 49;
			
		}
		//Print out the move
		System.out.println("User Chose " + userChoice);
		//Place user move on the board
		newBoardConfig[row][column] = "O";
		//Create a board object
		Board retBoard = new Board(newBoardConfig);
		retBoard.calcTerminalState(row, column, "O");
		if(retBoard.isGameOver())
		{
			return retBoard;
		}
		//return the new board
		return retBoard;
	}
	/**
	 * Implementation of a computer move
	 * @param board
	 */
	public Board computerMove(Board board)
	{
		System.out.println("Please enter how many seconds for this turn");
		//Calculate how many the computer has to run
		long seconds = input.nextLong() * 1000;
		//Figure at which time the computer must stop
		long timeEnding = System.currentTimeMillis() + seconds;
		//Run alpha beta algorithm to get the best move
		Board state = abPruning.alphaBetaAlgorithm(board, true, 
				-Double.MAX_VALUE, Double.MAX_VALUE, 6, timeEnding);
		String [][]newBoard = board.copy();
		//Save the best board's move
		int rowMove = state.getRowMove();
		int colMove = state.getColMove();
		//Get the character mapped to column number
		char rowChar = Board.rowToChar.get(rowMove);
		//Place computer's move on board
		newBoard[rowMove][colMove] = "X";
		Board newState = new Board(newBoard);
		newState.calcTerminalState(rowMove, colMove, "X");
		//print out computer's move
		System.out.println("Computer choses: " + rowChar + (colMove + 1));
		//return the new board
		return newState;
		
	}
	/**
	 * Main method for program
	 * @param args
	 */
	public static void main(String []args)
	{
		FourInALine f = new FourInALine();
		f.game();
		
	}

}
