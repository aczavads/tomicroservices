package br.pucrio.inf.les.opus.tomicroservices.optimization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class EuclideanDistanceRankingTest {

	@Test
	public void solutionWithTwoObj() {
		EuclideanDistanceRanking edRanking = new EuclideanDistanceRanking();
		List<MicroservicesSolution> solutions = new ArrayList<MicroservicesSolution>();
		MicroservicesSolution solution1, solution2;
		solution1 = mock(MicroservicesSolution.class);
		solution2 = mock(MicroservicesSolution.class);
		solutions.add(solution1);
		solutions.add(solution2);
		double[] obj1 = {2.0, 2.0, 4.0, 4.0};
		double[] obj2 = {1.0, 1.0, 2.0, 3.0};
		when(solution1.getObjectives()).thenReturn(obj1);
		when(solution1.getNumberOfObjectives()).thenReturn(obj1.length);
		when(solution2.getObjectives()).thenReturn(obj2);
		when(solution2.getNumberOfObjectives()).thenReturn(obj2.length);
		int[] indexs = edRanking.bestSolutions(solutions);
		assertEquals(1, indexs[0]);
	}
	
}
