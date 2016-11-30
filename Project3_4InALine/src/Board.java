import java.util.Arrays;
import java.util.HashMap;

public class Board {
	private String [][]boardConfig;
	private double utilityFunction;
	private boolean isGameOver;
	private int rowMove;
	private int colMove;
	public static final double WIN = Double.MAX_VALUE;
	public static final double LOSS = -Double.MAX_VALUE;
	// The initial board
	public static final String [][] INITIALBOARD = 
		{
			{"-", "-", "-", "-", "-", "-", "-", "-"},
			{"-", "-", "-", "-", "-", "-", "-", "-"},
			{"-", "-", "-", "-", "-", "-", "-", "-"},
			{"-", "-", "-", "-", "-", "-", "-", "-"},
			{"-", "-", "-", "-", "-", "-", "-", "-"},
			{"-", "-", "-", "-", "-", "-", "-", "-"},
			{"-", "-", "-", "-", "-", "-", "-", "-"},
			{"-", "-", "-", "-", "-", "-", "-", "-"}	
		};
	// The initial board
	public static final String [][] TESTBOARD = 
		{
			{"O", "X", "O", "X", "O", "X", "-", "-"},
			{"X", "X", "O", "X", "O", "X", "-", "-"},
			{"O", "X", "O", "X", "O", "X", "-", "-"},
			{"O", "O", "X", "O", "X", "O", "-", "-"},
			{"O", "X", "O", "X", "O", "-", "-", "-"},
			{"X", "X", "O", "X", "O", "X", "-", "-"},
			{"O", "X", "O", "X", "O", "X", "-", "-"},
			{"O", "O", "X", "O", "X", "X", "-", "-"}	
		};
	//A mapping of character to row number
	public static HashMap<Character, Integer> charToRowNum;
	//A mapping of 
	public static HashMap<Integer, Character> rowToChar;
	/**
	 * Constructor which initializes this board
	 * @param boardConfig
	 * @param utilityFunction
	 */
	public Board(String [][]boardConfig)
	{
		//Check if the static hashmap is not intitialized yet
		//if so initiliaze it
		if(charToRowNum == null)
		{
			charToRowNum = new HashMap<>();
			charToRowNum.put('a', 0);
			charToRowNum.put('b', 1);
			charToRowNum.put('c', 2);
			charToRowNum.put('d', 3);
			charToRowNum.put('e', 4);
			charToRowNum.put('f', 5);
			charToRowNum.put('g', 6);
			charToRowNum.put('h', 7);
		}
		if (rowToChar == null)
		{
			rowToChar = new HashMap<>();
			rowToChar.put(0, 'a');
			rowToChar.put(1, 'b');
			rowToChar.put(2, 'c');
			rowToChar.put(3, 'd');
			rowToChar.put(4, 'e');
			rowToChar.put(5, 'f');
			rowToChar.put(6, 'g');
			rowToChar.put(7, 'h');
		}
		this.boardConfig = boardConfig;
		this.utilityFunction = 0;
		this.isGameOver = false;
		this.rowMove = -1;
		this.colMove = -1;
	}
	/**
	 * Default Constructor
	 */
	public Board()
	{
		this.boardConfig = null;
		this.utilityFunction = 0;
	}
	/**
	 * Get this board configuration
	 * @return boardConfig
	 */
	public String[][] getBoardConfig() {
		return boardConfig;
	}
	/**
	 * Set this board configuration
	 * @param boardConfig
	 */
	public void setBoardConfig(String[][] boardConfig) {
		this.boardConfig = boardConfig;
	}
	/**
	 * Get the utilityFunction value from this board
	 * @return utilityFunction
	 */
	public double getUtilityFunction() {
		return utilityFunction;
	}
	/**
	 * Set the utility function for this configuration 
	 * @param utilityFunction
	 */
	public void setUtilityFunction(double utilityFunction) {
		this.utilityFunction = utilityFunction;
	}
	/**
	 * Get the row move
	 * @return
	 */
	public int getRowMove() {
		return rowMove;
	}
	/**
	 * set the row move
	 * @param rowMove
	 */
	public void setRowMove(int rowMove) {
		this.rowMove = rowMove;
	}
	/**
	 * Get the column move
	 * @return
	 */
	public int getColMove() {
		return colMove;
	}
	/**
	 * set the column move
	 * @param colMove
	 */
	public void setColMove(int colMove) {
		this.colMove = colMove;
	}
	/**
	 * Set both moves
	 * @param i
	 * @param j
	 */
	public void setMoves(int i, int j)
	{
		this.rowMove = i;
		this.colMove = j;
	}
	/**
	 * return boolean isGameOver 
	 * @return
	 */
	public boolean isGameOver() {
		return isGameOver;
	}
	/**
	 * set isGameover
	 * @param isGameOver
	 */
	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}
	/**
	 * Print this board configuration
	 */
	public void printBoard()
	{
		char rowValue = 'A';
		String firstRow = "  1 2 3 4 5 6 7 8";
		String [][]thisBoard = boardConfig;
		System.out.println(firstRow);
		for(int i = 0; i < 8; i++)
		{
			System.out.print(rowValue + " ");
			for(int j = 0; j < 8; j++)
			{
				System.out.print(thisBoard[i][j] + " ");
			}
			System.out.println();
			rowValue++;
		}
	}
	/**
	 * Check whether a certain is allowed
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isAllowedMove(int i, int j)
	{
		//Check if i and j within board
		if(i < 0 || i > 7  || j < -1 || j > 7)
			return false;
		
		return boardConfig[i][j].equals("-") ;
	}
	/**
	 * calculates whether the board is a terminal state
	 * and assigns a utility value
	 * @return
	 */
	public boolean calcTerminalState(int i, int j, String value)
	{
		
		boolean leftMost = false;
		boolean rightMost = false;
		boolean midRow = false;
		boolean top = false;
		boolean bottom = false;
		boolean midCol = false;
		
		//Check if value was put at left most of three others
		if(j + 1 < 8 && j + 2 < 8 && j + 3 < 8)
		{
			leftMost = boardConfig[i][j + 1].equals(value) 
					&& boardConfig[i][j + 2].equals(value)
					&& boardConfig[i][j + 3].equals(value);
		}
		//Check if value was put at left most of three others
		if(j - 1 >= 0 && j - 2 >= 0 && j - 3 >= 0)
		{
			rightMost = boardConfig[i][j - 1].equals(value) 
					&& boardConfig[i][j - 2].equals(value)
					&& boardConfig[i][j - 3].equals(value);
		}
		//Check if value was put at top of three others
		if(i + 1 < 8 && i + 2 < 8 && i + 3 < 8)
		{
			top = boardConfig[i + 1][j].equals(value) 
					&& boardConfig[i + 2][j].equals(value)
					&& boardConfig[i + 3][j].equals(value);
		}
		//Check if value was put at the bottom of three others
		if(i - 1 >= 0 && i - 2 >= 0 && i - 3 >= 0)
		{
			bottom = boardConfig[i - 1][j].equals(value) 
					&& boardConfig[i - 2][j].equals(value)
					&& boardConfig[i - 3][j].equals(value);
		}
		int rowValue = 1;
		if(i - 1 >= 0 && boardConfig[i - 1][j].equals(value))
		{
			rowValue ++;
			if(i - 2 >= 0 && boardConfig[i - 2][j].equals(value))
				rowValue ++;
			
		}
		
		if(i + 1 < 8 &&  boardConfig[i + 1][j].equals(value))
		{
			rowValue ++;
			if(i + 2 < 8 && boardConfig[i + 2][j].equals(value))
				rowValue ++;
		}
		
		if(rowValue >= 4)
			midRow = true;
		
		int colValue = 1;
		if(j - 1 >= 0 && boardConfig[i][j - 1].equals(value))
		{
			colValue ++;
			if(j - 2 >= 0 && boardConfig[i][j - 2].equals(value))
				colValue ++;
		}
		
		if(j + 1 < 8 &&  boardConfig[i][j + 1].equals(value))
		{
			
			colValue ++;
			if(j + 2 < 8 && boardConfig[i][j + 2].equals(value))
				colValue ++;
		}
		
		if(colValue >= 4)
			midCol = true;
		
		if(leftMost || rightMost || top || midCol || midRow || bottom)
		{
			if(value.equals("X"))
			{
				utilityFunction = Board.WIN;
			}
			else
			{
				utilityFunction = Board.LOSS;
			}
			this.isGameOver = true;
		}
		return leftMost || rightMost || top || midCol || midRow || bottom;
	}
	/**
	 * Copies parent to new a board. To preserve parent board
	 * @param parent
	 * @return
	 */
	public String [][]copy ()
	{	
		String [][] child = new String[boardConfig.length][];
		for(int i = 0; i < boardConfig.length; i++)
		    child[i] = Arrays.copyOf(boardConfig[i], boardConfig.length);
		return child;
	}
	/**
	 * Checks if game has tied.
	 * @return
	 */
	public boolean isTieGame()
	{
		String [][] board = this.boardConfig;
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[i].length; j++)
			{
				if(board[i][j].equals("-"))
					return false;
			}
		}
		
		return true;
	}
}
