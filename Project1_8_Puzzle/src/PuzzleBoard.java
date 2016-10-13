
public class PuzzleBoard {
	private int[][] puzzle;
	private PuzzleBoard parent;
	private int aStar;
	private int pathCost;
	private boolean goalState;
	
	public PuzzleBoard(int [][]puzzle, PuzzleBoard parent, int pathCost, int heuristicType)
	{
		this.puzzle = puzzle;
		this.parent = parent;
		this.pathCost = pathCost;
		this.goalState = false;
		if(heuristicType == 0)
		{
			//Do misplaced tiles heuristic
			int misplacedTiles = misplacedTiles();
			if(misplacedTiles == 0)
				this.goalState = true;
			this.aStar = pathCost + misplacedTiles();
		}
		else if(heuristicType == 1)
		{
			//Do manhattan distance heuristic function
			int manhattanDistance = manhattanDistance();
			if(manhattanDistance == 0)
			{
				this.goalState = true;
			}
			this.aStar = pathCost + manhattanDistance;
		}
	}
	public PuzzleBoard()
	{
		this.puzzle = null;
		this.parent = null;
		this.aStar = 0;
		this.pathCost = 0;
	}

	public int[][] getPuzzle() {
		return puzzle;
	}

	public void setPuzzle(int[][] puzzle) {
		this.puzzle = puzzle;
	}

	public PuzzleBoard getParent() {
		return parent;
	}

	public void setParent(PuzzleBoard parent) {
		this.parent = parent;
	}

	public int getAStar() {
		return aStar;
	}

	public void setAStar(int fn) {
		this.aStar = fn;
	}
	public int getPathCost()
	{
		return pathCost;
	}
	public void setPathCost(int pathCost)
	{
		this.pathCost = pathCost;
	}
	public boolean isGoalState() {
		return goalState;
	}
	public void setGoalState(boolean goalState) {
		this.goalState = goalState;
	}
	public int misplacedTiles()
	{
		int [][]puzzleConfig = this.puzzle;
		int index = 0;
		int misplacedTiles = 0;
		for(int i = 0; i < puzzleConfig.length; i++)
		{
			for (int j = 0; j < puzzleConfig[i].length; j++)
			{
				if(index != puzzleConfig[i][j])
				{
					misplacedTiles++;
				}
				index++;
			}
		}
		return misplacedTiles;
	}
	public int manhattanDistance()
	{
		int manhattanDistance = 0;
		for (int i = 0; i < puzzle.length; i++)
		{
			for(int j = 0; j < puzzle[i].length; j++)
			{
				int value = puzzle[i][j];
				if(value != 0)
				{
					int expectedX = (value) / puzzle.length;
					int expectedY = (value) % puzzle.length;
					int dx = i - expectedX;
					int dy = j - expectedY;
					manhattanDistance += Math.abs(dx) + Math.abs(dy);
				}
			}
		}
		return manhattanDistance;
	}
	public int hash()
	{
		int hashValue = 0;
		int index = 1;
		for(int i = 0; i < puzzle.length; i++)
		{
			for (int j = 0; j < puzzle[i].length; j++)
			{
				hashValue += Math.pow(-1.0, index)*Math.pow(((puzzle[i][j]+ 1)), index);
				index++;
			}
		}
		return hashValue;
	}
	public boolean isSolvable()
	{
		//Keep track of invertedTiles
		int []arrayTransform = new int [9];
		int index = 0;
		for(int i = 0; i < puzzle.length; i++)
		{
			for(int j = 0; j < puzzle[i].length; j++)
			{
				arrayTransform[index] = puzzle[i][j];
				
			}
		}
		//if invertedTiles is not even then unsolvable
		return (findInvertedTiles(arrayTransform) % 2) == 0;
	}
	public int findInvertedTiles(int []array)
	{
		int invertedTiles = 0;
		for (int i = 0; i < array.length; i++)
		{
			int value = array[i];
			if (value != 0)
			{
				for(int j = i; j < array.length; j++)
				{
					if(value > array[j] && array[j] != 0)
					{
						invertedTiles++;
					}
					
				}
			}
		}
		return invertedTiles;
	}
	public static void main(String []args)
	{
		int [][]puzzleConfig = {{0,1,2}, {3,4,5}, {6,7,8}};
		int [][]puzzleConfig1 = {{1,0,2}, {6,4,5}, {3,7,8}};
		int [][]puzzleConfig2 = {{8,0,8}, {8,0,8}, {8,0,8}};
		PuzzleBoard pb = new PuzzleBoard(puzzleConfig, null, 0, 0);
		System.out.println(pb.hash());
		System.out.println(pb.manhattanDistance());
		
		pb.setPuzzle(puzzleConfig1);
		System.out.println(pb.isSolvable());
		System.out.println(pb.hash());
		pb.setPuzzle(puzzleConfig2);
		System.out.println(pb.hash());
	}
}
