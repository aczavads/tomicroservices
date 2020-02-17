package br.pucrio.inf.les.opus.tomicroservices.optimization;

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
		MicroservicesSolution mutated = solution; //solution.copy();
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
		limit = microservice1.getVerticies().size() - 1;
		Vertex vertexFromMs1 = microservice1.getVerticies().get(randomGenerator.nextInt(0, limit));
		limit = microservice2.getVerticies().size() - 1;
		Vertex vertexFromMs2 = microservice2.getVerticies().get(randomGenerator.nextInt(0, limit));
		microservice1.removeAndAddVerticies(vertexFromMs1, vertexFromMs2);
		microservice2.removeAndAddVerticies(vertexFromMs2, vertexFromMs1);
		//System.out.println(mutated);
		return mutated;
	}

}
