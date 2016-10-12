import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

public class Puzzle8 {
	public Stack<PuzzleBoard> aStarSearch(int [][] puzzle)
	{
		//Create initial state of problem
		PuzzleBoard node = new PuzzleBoard(puzzle, null, 0, 0);
		//Create frontier and add initial state
		PriorityQueue<PuzzleBoard> frontier 
		= new PriorityQueue<PuzzleBoard>(new PuzzleComparator());
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
				puzzleConfig = new PuzzleBoard(puzzleMove, leaf, leaf.getPathCost() + 1, 0);
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
	public static void main(String []args)
	{
		int [][] puzzle = {{0,1, 2}, {4, 5, 6}, {3, 8, 7}};
		Puzzle8 p8 =  new Puzzle8();
		Stack<PuzzleBoard> moves = p8.aStarSearch(puzzle);
		ArrayList<int[][]> puzzles = p8.possiblePuzzleMoves(puzzle);
		while(!moves.isEmpty())
		{
			int [][]puzzleConfig = moves.pop().getPuzzle();
			for(int i = 0; i < puzzleConfig.length; i++)
			{
				for (int j = 0; j < puzzleConfig[i].length; j++)
				{
					System.out.print(puzzleConfig[i][j]);
				}
				System.out.println();
			}
			System.out.println();
			
		}
	}

}
