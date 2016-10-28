
public class Board {
	private String board;
	private int attackingQueens;
	private int n;
	
	public Board(String board, int n)
	{
		this.board = board;
		this.n = n;
		this.attackingQueens = calcAttackingQueens();
		System.out.println(attackingQueens);
	}
	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public int getAttackingQueens() {
		return attackingQueens;
	}

	public void setAttackingQueens(int attackingQueens) {
		this.attackingQueens = attackingQueens;
	}
	public int calcAttackingQueens()
	{
		int attacking = 0;
		String[] data = board.split(" ");
		int [][]boardConfig = new int[n][n];
		
		int count = 0;
		for(int i = 0; i < n; i++)
		{
			int rowValue = Integer.parseInt(data[i]);
			for(int j = 0; j < n; j++)
			{
				int otherQueenRow = Integer.parseInt(data[j]);
				if(j != i && rowValue == otherQueenRow)
				{
					attacking++;
				}
			}
			//boardConfig[rowValue][i] = 1;
		}
		/*for(int i = 0; i < n; i++)
		{
			int rowValue = boardConfig[count][i];
			for(int j = 0; j < n; j++)
			{
				System.out.print(boardConfig[i][j]);
			}
			System.out.println();
		}*/
		return attacking;
	}
	public static void main(String []args)
	{
		Board board = new Board("2 2 3 4", 4);
	}
}
