
public class NQueen {
	public static void main(String []args)
	{
		int hillClimbSuccesses = 0;
		int num = 200;
		long deltaTime = 0;
		int searchCostSum = 0;
		int n = 19;
		System.out.println("Hill Climbing");
		for(int i = 0; i < num; i++){
			SteepestHillClimbing hillClimb = new SteepestHillClimbing();
			String puzzle = hillClimb.generateRandomPuzzle(n);
			long begin = System.currentTimeMillis();
			Board board = hillClimb.steepHillClimbing(puzzle);
			long end = System.currentTimeMillis();
			searchCostSum += board.getSearchCost();
			deltaTime += end - begin;
			//board.printBoard();
			if(board.getAttackingQueens() == 0)
				hillClimbSuccesses ++;
		}
		double percentageCorrect = (double)hillClimbSuccesses / (double)num;
		double avgTime = (double)deltaTime / (double)num;
		double avgSearchCost = (double)searchCostSum / (double) num;
		System.out.println("Percent of correct answers: " + percentageCorrect);
		System.out.println("Average execution time in milliseconds " + avgTime);
		System.out.println("Average Search Cost: " + avgSearchCost);
		
		System.out.println("Genetic Algorithm");
		deltaTime = 0;
		searchCostSum = 0;
		int geneticSuccessRate = 0;
		for(int i = 0; i < num; i++){
			GeneticAlgorithm ga = new GeneticAlgorithm();
			long begin = System.currentTimeMillis();
			Board board = ga.geneticAlgorithm(n);
			long end = System.currentTimeMillis();
			searchCostSum += board.getSearchCost();
			deltaTime += end - begin;
			//board.printBoard();
			if(board.getAttackingQueens() == 0)
				geneticSuccessRate ++;
		}
		percentageCorrect = (double)geneticSuccessRate / (double)num;
		avgTime = (double)deltaTime / (double)num;
		avgSearchCost = (double)searchCostSum / (double) num;
		System.out.println("Percent of correct answers: " + percentageCorrect);
		System.out.println("Average execution time in milliseconds " + avgTime);
		System.out.println("Average Search Cost: " + avgSearchCost);

	}

}
