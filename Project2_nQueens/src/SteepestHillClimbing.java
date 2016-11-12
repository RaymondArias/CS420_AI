import java.util.ArrayList;
import java.util.Random;

public class SteepestHillClimbing {
	
	public Board steepHillClimbing(String initialBoard)
	{
		int n = initialBoard.split(" ").length;
		Board current = new Board(initialBoard, n);
		int searchCost = 0;
		while(true)
		{
			ArrayList<Board> childrenConfigs = generateAllBoardConfigs(current.getBoard().split(" "), n);
			searchCost += childrenConfigs.size();
			Board neighbor = getBestChild(childrenConfigs);
			if(current.getAttackingQueens() <= neighbor.getAttackingQueens())
			{
				current.setSearchCost(searchCost);
				return current;
			}
			current = neighbor;
		}
	}
	public ArrayList<Board> generateAllBoardConfigs(String []board, int n)
	{
		ArrayList<Board> childBoards = new ArrayList<>(n*n);
		for(int i = 0; i < n; i++)
		{
			//int columnNum = i;
			String originalNum = board[i];
			for(int j = 0; j < n; j++)
			{
				board[i] = new String((j + 1) + "");
				if(!board[i].equals(originalNum))
				{
					String newBoard = boardToString(board);
					Board childBoard = new Board(newBoard, n);
					childBoards.add(childBoard);
				}
			}
			board[i] = originalNum;
		}
		
		return childBoards;
	}
	public String boardToString(String []board)
	{
		StringBuilder sb = new StringBuilder();
		for(String str: board)
		{
			sb.append(str + " ");
		}
		return sb.toString();
	}
	public Board getBestChild(ArrayList<Board> boardConfigs)
	{
		Board bestBoard = boardConfigs.get(0);
		for(int i = 0; i < boardConfigs.size(); i++)
		{
			Board board = boardConfigs.get(i);
			if(board.getAttackingQueens() <= bestBoard.getAttackingQueens())
				bestBoard = board;
		}
		
		return bestBoard;
		
	}
	public String generateRandomPuzzle(int n)
	{
		StringBuilder builder = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < n; i++)
		{
			String str = (1 + random.nextInt(n)) + " ";
			builder.append(str);
			
		}
		return builder.toString();
	}
	public static void main(String []args)
	{
		SteepestHillClimbing hillClimb = new SteepestHillClimbing();
		int correctPuzzles = 0;
		for (int i = 0; i < 1000; i++)
		{
			String puzzle = hillClimb.generateRandomPuzzle(8);
			
			Board answer = hillClimb.steepHillClimbing(puzzle);
			if(answer.getAttackingQueens() == 0)
			{
				
				System.out.println("Solved Puzzle");
				System.out.println(puzzle);
				//System.out.println(answer.getBoard());
				answer.printBoard();
				correctPuzzles++;
			}
			else
			{
				//System.out.println("Puzzle not solzed");
			}
			
		}
		System.out.println((double)(correctPuzzles / 1000.0));
	}

}
