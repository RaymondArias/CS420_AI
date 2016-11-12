
public class Board {
	private String board;
	private int attackingQueens;
	private int n;
	private double fitnessFunction;
	private int searchCost;
	
	public Board(String board, int n)
	{
		this.board = board;
		this.n = n;
		this.attackingQueens = calcAttackingQueens();
		
		this.fitnessFunction = 1/(double)attackingQueens;
		//System.out.println(attackingQueens);
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
	public int getN()
	{
		return n;
	}
	public void setN(int n)
	{
		this.n = n;
	}
	public double getFitnessFunction() {
		return fitnessFunction;
	}
	public void setFitnessFunction(double fitnessFuction) {
		this.fitnessFunction = fitnessFuction;
	}
	public int getSearchCost() {
		return searchCost;
	}
	public void setSearchCost(int searchCost) {
		this.searchCost = searchCost;
	}
	public int calcAttackingQueens()
	{
		int attacking = 0;
		String[] data = board.split(" ");
		//Calc the num of attacking rows
		for(int i = 0; i < data.length; i++)
		{
			int queenNumber = Integer.parseInt(data[i]);
			attacking += findAttackingPerQueen(i, queenNumber, data);
		}
		return attacking;
	}
	int findAttackingPerQueen(int queen_number, int row_position, String []position) {
		int attack = 0;
	    for(int i=0; i<queen_number; i++) {
	        int other_row_pos = Integer.parseInt(position[i]);
	        if(other_row_pos == row_position ||
	           other_row_pos == row_position - (queen_number-i) || 
	           other_row_pos == row_position + (queen_number-i)) 
	            attack++;
	    }
	    return attack;
	}
	public void printBoard()
	{
		String []rowValue = board.split(" ");
		for(int i = 0; i < n; i++)
		{
			int columnNumWithQueen = Integer.parseInt(rowValue[i]);
			for (int j = 0; j < n; j++)
			{
				if(columnNumWithQueen == j + 1)
				{
					System.out.print("Q");
				}
				else
				{
					System.out.print("-");
				}
			}
			System.out.println();
		}
	}
	public static void main(String []args)
	{
		Board board = new Board("3 1 4 2", 4);
	}
}
