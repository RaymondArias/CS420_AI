import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithms {
	public Board geneticAlgorithm(int n)
	{
		Board retBoard = null;
		Random rand = new Random(System.currentTimeMillis());
		GeneticComparator compare = new GeneticComparator();
		int k = 100;
		ArrayList<Board> population = genPopulation(k, n, rand);
		population.sort(compare);
		int i = 0;
		while (true)
		{
			ArrayList<Board> selectedPop = select(population, rand);
			ArrayList<Board> crossOver = crossOver(selectedPop, rand, n);
			crossOver.sort(compare);
			Board bestBoard = crossOver.get(0);
			if(bestBoard.getAttackingQueens() == 0)
			{
				retBoard = bestBoard;
				break;
			}
			population = crossOver;
			
		}
		return retBoard;
	}
	public String generateRandomPuzzle(int n, Random random)
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < n; i++)
		{
			String str = (1 + random.nextInt(n)) + " ";
			builder.append(str);
			
		}
		return builder.toString();
	}
	public ArrayList<Board> genPopulation(int k, int n, Random random)
	{
		ArrayList<Board> population = new ArrayList<>(k);
		for(int i = 0; i < k; i++)
		{
			String boardStr = generateRandomPuzzle(n, random);
			Board randBoard = new Board(boardStr, boardStr.split(" ").length);
			population.add(randBoard);
		}
		return population;
	}
	public ArrayList<Board> select(ArrayList<Board> population, Random rand)
	{
		int populationSize = population.size();
		ArrayList<Board> selectedPop = new ArrayList<>(populationSize);
		int lastIndex = (int) (0.1 * population.size());
		selectedPop.addAll(population.subList(0, lastIndex));
		
		for(int i = lastIndex; i < populationSize; i++)
		{
			//System.out.println("Select: " + i);
			int index = Math.abs(rand.nextInt(lastIndex));
			selectedPop.add(population.get(index));
		}
		
		return selectedPop;
	}
	public ArrayList<Board> crossOver(ArrayList<Board> population, Random rand, int n)
	{
		int popSize = population.size();
		ArrayList<Board> crossOver = new ArrayList<>(popSize);
		for(int i = 0; i < population.size() / 2; i++)
		{
			//System.out.println("Crossover: " + i);
			//Choose two random parent
			Board parent1 = population.get(rand.nextInt(popSize));
			Board parent2 = population.get(rand.nextInt(popSize));
			// Randomly get crossOverPoint
			int crossOverPoint = rand.nextInt(n);
			while(crossOverPoint <= 0)
			{
				crossOverPoint = rand.nextInt(n);
			}
			//Get first parent board and split up with random cross over point
			String parent1Board = parent1.getBoard();
			String []parent1Values = parent1Board.split(" ");
			String parent1Front = arrayToString(parent1Values, 0, crossOverPoint);
			String parent1Back = arrayToString(parent1Values, crossOverPoint, parent1Values.length);
			//Get Second parent board and split up with random cross over point
			String parent2Board = parent2.getBoard();
			String []parent2Values = parent2Board.split(" ");
			String parent2Front = arrayToString(parent2Values, 0, crossOverPoint);
			String parent2Back = arrayToString(parent2Values, crossOverPoint, parent1Values.length);
			//Merge two parent boards into two need child boards
			String child1 = parent1Front + parent2Back;
			String child2 = parent2Front + parent1Back;
			child1 = mutation(child1, rand, n);
			child2 = mutation(child2, rand, n);
			Board childBoard1 = new Board(child1, child1.split(" ").length);
			Board childBoard2 = new Board(child2, child2.split(" ").length);
			crossOver.add(childBoard1);
			crossOver.add(childBoard2);
		}
		
		return crossOver;
	}
	public String mutation(String originalString, Random rand, int n)
	{
		String []data = originalString.split(" ");
		int randIndex = rand.nextInt(data.length);
		String oldString = data[randIndex];
		String mutation = (1+rand.nextInt(n)) +"";
		data[randIndex] = mutation;
		return arrayToString(data, 0, data.length);
		
	}
	public String arrayToString(String []values, int begin, int end)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = begin; i < end; i++)
		{
			sb.append(values[i] + " ");
		}
		return sb.toString();
	}
	public static void main(String []args)
	{
		GeneticAlgorithms ga = new GeneticAlgorithms();
		for(int i = 0; i < 1000; i++)
		{
			Board finalBoard = ga.geneticAlgorithm(20);
			finalBoard.printBoard();
			System.out.println(finalBoard.getBoard());
		}
	}

}
