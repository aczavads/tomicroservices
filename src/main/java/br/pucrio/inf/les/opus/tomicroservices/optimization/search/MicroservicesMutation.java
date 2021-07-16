package br.pucrio.inf.les.opus.tomicroservices.optimization.search;

import java.util.List;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;

public class MicroservicesMutation implements MutationOperator<MicroservicesSolution> {

	private PseudoRandomGenerator randomGenerator;
	
	public MicroservicesMutation(PseudoRandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}
	
	@Override
	//other possible implementation: move the first vertex;
	public MicroservicesSolution execute(MicroservicesSolution solution) {
		//System.out.println("Mutation");
		//System.out.println(solution);
		MicroservicesSolution mutated = solution.copy(); //solution.copy();
		List<Microservice> microservices = mutated.getMicroservices();
		int limit = microservices.size() - 1;
		int index1 = randomGenerator.nextInt(0, limit);
		int index2 = randomGenerator.nextInt(0, limit);
		if (index1 == index2) {
			++index2;
			if (index2 > limit) {
				index2 = 0;
			}
		}
		Microservice microservice1 = microservices.get(index1);
		Microservice microservice2 = microservices.get(index2);
		limit = microservice1.getMutableVerticies().size() - 1;
		if (limit < 1) {
			System.out.println("Not mutated");
			return mutated;
		}
		Vertex vertexFromMs1 = microservice1.getMutableVerticies().get(randomGenerator.nextInt(0, limit));
		limit = microservice2.getMutableVerticies().size() - 1;
		if (limit < 1) {
			System.out.println("Not mutated");
			return mutated;
		}
		Vertex vertexFromMs2 = microservice2.getMutableVerticies().get(randomGenerator.nextInt(0, limit));
		microservice1.removeAndAddVerticies(vertexFromMs1, vertexFromMs2);
		microservice2.removeAndAddVerticies(vertexFromMs2, vertexFromMs1);
		return mutated;
	}

}
