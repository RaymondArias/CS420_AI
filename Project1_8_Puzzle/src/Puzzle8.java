import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Puzzle8 {
	public Stack<PuzzleBoard> aStarSearch(int [][] puzzle, int heuristicFunction)
	{
		//Create initial state of problem
		PuzzleBoard node = new PuzzleBoard(puzzle, null, 0, heuristicFunction);
		if(!node.isSolvable())
		{
			System.out.println("Puzzle not solvable");
			return new Stack<>();
		}
		//Create frontier and add initial state
		PriorityQueue<PuzzleBoard> frontier 
		= new PriorityQueue<PuzzleBoard>(10, new PuzzleComparator());
		HashSet<Integer> alreadyVisited = new HashSet<>();
		frontier.add(node);
		ArrayList<int [][]> possibleMoves = null;
		PuzzleBoard leaf = null;
		while(!frontier.isEmpty())
		{
			//Get the leaf with lowest aStar value from frontier
			leaf = frontier.poll();
			if(leaf.isGoalState())
			{
				if(heuristicFunction == 0)
				{
					System.out.println("Heuristic Function: Misplaced Tiles");
				}
				else
				{
					System.out.println("Heuristic Function: Manhattan Distance");
				}
				System.out.println("Depth: " + leaf.getPathCost());
				System.out.println("Nodes Generated: " + (frontier.size() + alreadyVisited.size()));
				break;
			}
			//Add it alreadyVisted table
			alreadyVisited.add(leaf.hash());
			//Get all possible moves from this configuration of puzzle
			possibleMoves = possiblePuzzleMoves(leaf.getPuzzle());
			PuzzleBoard puzzleConfig = null;
			for(int i = 0; i < possibleMoves.size(); i++)
			{
				int [][]puzzleMove = possibleMoves.get(i);
				puzzleConfig = new PuzzleBoard(puzzleMove, leaf, leaf.getPathCost() + 1, heuristicFunction);
				if(!alreadyVisited.contains(puzzleConfig.hash()))
				{
					frontier.add(puzzleConfig);
				}
			}
			
		}
		Stack<PuzzleBoard> puzzleMoves = new Stack<>();
		while(leaf != null)
		{
			puzzleMoves.push(leaf);
			leaf = leaf.getParent();
		}	
		return puzzleMoves;
	}
	public ArrayList<int [][]> possiblePuzzleMoves(int [][]parent)
	{
		ArrayList<int [][]> possibleMoves = new ArrayList<>();
		//find zero in parent board
		int emptyIndexI = 0;
		int emptyIndexJ = 0;
		for(int i = 0; i < parent.length; i++)
		{
			for (int j = 0; j < parent[i].length; j++)
			{
				if(parent[i][j] == 0)
				{
					emptyIndexI = i;
					emptyIndexJ = j;
					break;
				}
			}
		}
		//check if zero is not in the top row
		//which means we can swap the tile above zero
		if(emptyIndexI > 0)
		{
			int [][]newPuzzleConfig = copy(parent);
			int tileAbove = newPuzzleConfig[emptyIndexI - 1][emptyIndexJ];
			newPuzzleConfig[emptyIndexI - 1][emptyIndexJ] = 0;
			newPuzzleConfig[emptyIndexI][emptyIndexJ] = tileAbove;
			possibleMoves.add(newPuzzleConfig);
		}
		//check if zero is not on bottom row
		//which means the zero can be swapped with row below zero
		if(emptyIndexI < 2)
		{
			int [][]newPuzzleConfig = copy(parent);
			int tileBelow = newPuzzleConfig[emptyIndexI + 1][emptyIndexJ];
			newPuzzleConfig[emptyIndexI + 1][emptyIndexJ] = 0;
			newPuzzleConfig[emptyIndexI][emptyIndexJ] = tileBelow;
			possibleMoves.add(newPuzzleConfig);
			
		}
		//Check if zero is not in left most column
		//which means zero can be swapped with tile to its left
		if(emptyIndexJ > 0)
		{
			int [][]newPuzzleConfig = copy(parent);
			int tileToLeft = newPuzzleConfig[emptyIndexI][emptyIndexJ-1];
			newPuzzleConfig[emptyIndexI][emptyIndexJ-1] = 0;
			newPuzzleConfig[emptyIndexI][emptyIndexJ] = tileToLeft;
			possibleMoves.add(newPuzzleConfig);
		}
		//Check if zero is not in right most column
		//which means zero can be swapped with tile to its right
		if(emptyIndexJ < 2)
		{
			int [][]newPuzzleConfig = copy(parent);
			int tileToRight = newPuzzleConfig[emptyIndexI][emptyIndexJ+1];
			newPuzzleConfig[emptyIndexI][emptyIndexJ+1] = 0;
			newPuzzleConfig[emptyIndexI][emptyIndexJ] = tileToRight;
			possibleMoves.add(newPuzzleConfig);
		}
		return possibleMoves;
	}
	public int [][]copy (int [][]parent)
	{
		int [][] child = new int[parent.length][];
		for(int i = 0; i < parent.length; i++)
		    child[i] = Arrays.copyOf(parent[i],parent.length);
		return child;
	}
	public int [][] generateRandomPuzzles(int [][]puzzle, int shuffles)
	{
	
		ArrayList<int [][]> puzzleBoards = null;
		Random random = new Random();
		for(int i = 0; i < shuffles; i++)
		{
			puzzleBoards = this.possiblePuzzleMoves(puzzle);
			puzzle = puzzleBoards.get(random.nextInt(puzzleBoards.size()));
		}
		PuzzleBoard pb = new PuzzleBoard(puzzle, null, 0, 0);
		if(!pb.isSolvable())
			return generateRandomPuzzles(puzzle, 100);
		return puzzle;
	}
	public void printMoves(Stack<PuzzleBoard>moves)
	{
		int counter = 0;
		while(!moves.isEmpty())
		{
			int [][]puzzleConfig = moves.pop().getPuzzle();
			System.out.println("Move: " + counter);
			for(int i = 0; i < puzzleConfig.length; i++)
			{
				for (int j = 0; j < puzzleConfig[i].length; j++)
				{
					System.out.print(puzzleConfig[i][j]);
				}
				
				System.out.println();
			}
			counter++;
			System.out.println();
			
		}
	}
	public static void main(String []args)
	{
		/*int [][] puzzle = {{0, 1, 2}, {3,4,5}, {6,7,8}};
		Puzzle8 p8 =  new Puzzle8();
		puzzle = p8.generateRandomPuzzles(puzzle, 100);
		Stack<PuzzleBoard> moves = p8.aStarSearch(puzzle, 1);
		p8.printMoves(moves);*/
		Scanner scanner = new Scanner(System.in);
		Puzzle8 puzzle8 = new Puzzle8();
		while(true)
		{
			System.out.println("Please choose a command: ");
			System.out.println("1.Enter a puzzle");
			System.out.println("2. 100 Random puzzles");
			int userChoice = scanner.nextInt();
			if(userChoice == 1)
			{
				scanner.nextLine();
				System.out.println("Please Enter each Row");
				String line1 = scanner.nextLine();
				String line2 = scanner.nextLine();
				String line3 = scanner.nextLine();
				String []lineOneData = line1.split(" ");
				String []lineTwoData = line2.split(" ");
				String []lineThreeData = line3.split(" ");
				int [][]userPuzzle = {{Integer.parseInt(lineOneData[0]), Integer.parseInt(lineOneData[1]), Integer.parseInt(lineOneData[2])},
				{Integer.parseInt(lineTwoData[0]), Integer.parseInt(lineTwoData[1]), Integer.parseInt(lineTwoData[2])},
				{Integer.parseInt(lineThreeData[0]), Integer.parseInt(lineThreeData[1]), Integer.parseInt(lineThreeData[2])}};
				Stack<PuzzleBoard> moves = puzzle8.aStarSearch(userPuzzle, 0);
				puzzle8.printMoves(moves);
			}
			else if(userChoice == 2)
			{
				for (int i = 0; i < 200; i++)
				{
					System.out.println("Puzzle: " + (i + 1));
					int [][] puzzle = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
					puzzle = puzzle8.generateRandomPuzzles(puzzle, 100);
					Stack<PuzzleBoard> moves = puzzle8.aStarSearch(puzzle, 0);
					Stack<PuzzleBoard> moves1 = puzzle8.aStarSearch(puzzle, 1);
					System.out.println();
					
					//puzzle8.printMoves(moves);
				}
			}
			
		}
	}

}
