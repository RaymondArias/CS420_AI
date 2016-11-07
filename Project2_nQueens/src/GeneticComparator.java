import java.util.Comparator;

public class GeneticComparator implements Comparator<Board>{

	@Override
	public int compare(Board b0, Board b1) {
		if (b0.getFitnessFuction() > b1.getFitnessFuction())
			return -1;
		else if(b0.getFitnessFuction() < b1.getFitnessFuction())
			return 1;
		else
			return 0;
	}

}
