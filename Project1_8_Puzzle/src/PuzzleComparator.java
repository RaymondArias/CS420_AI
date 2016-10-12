import java.util.Comparator;

public class PuzzleComparator implements Comparator<PuzzleBoard> {

	@Override
	public int compare(PuzzleBoard p0, PuzzleBoard p1) {
		if(p0.getAStar() < p1.getAStar())
		{
			return -1;
		}
		return 1;
	}

}
