package br.pucrio.inf.les.opus.tomicroservices.optimization.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;

public class MicroservicesCrossover implements CrossoverOperator<MicroservicesSolution> {

	private PseudoRandomGenerator random;
	
	private int numberOfParents;
	
	private int numberOfChildren;
	
	private double crossoverProbability;
	
	private double crossoverFraction;
	
	public MicroservicesCrossover(int numberOfParents, int numberOfChildren, PseudoRandomGenerator random, 
			double crossoverProbability, double crossoverFraction) {
		if (numberOfChildren < 0) {
			throw new JMetalException("numberOfChildren is less then zero");
		}
		this.numberOfChildren = numberOfChildren;
		if (numberOfParents < 0) {
			throw new JMetalException("numberOfParents is less then zero");
		}
		this.numberOfParents = numberOfParents;
		if (crossoverProbability <= 0.0f && crossoverProbability >= 1.0f) {
			throw new JMetalException("crossoverProbability is not a probability [0,1]");
		}
		this.crossoverProbability = crossoverProbability;
		if (crossoverFraction <= 0.0f && crossoverFraction >= 1.0f) {
			throw new JMetalException("crossoverProbability is not [0,1]");
		}
		this.crossoverFraction = crossoverFraction;
		this.random = random;
	}
	
	private List<MicroservicesSolution> copySource(List<MicroservicesSolution> source) {
		ArrayList<MicroservicesSolution> lMicroservicesSolutions = new ArrayList<MicroservicesSolution>();
		for (MicroservicesSolution ms : source) {
			lMicroservicesSolutions.add(ms.copy());
		}
		return lMicroservicesSolutions;
	}

	@Override
	public List<MicroservicesSolution> execute(List<MicroservicesSolution> source) {
		boolean doExecute = this.random.nextDouble(0.0f, 1.0f) <= this.crossoverProbability;
		if (doExecute) {
			source = copySource(source);
			int sourceRandom = this.random.nextInt(0, source.size() - 1);
			MicroservicesSolution solution = source.get(sourceRandom);
			List<Microservice> lMicroservice = solution.getMicroservices();
			int limit = lMicroservice.size() - 1;
			int m1Index = this.random.nextInt(0, limit);
			int m2Index = this.random.nextInt(0, limit);
			if (m1Index == m2Index) {
				++m2Index;
				if (m2Index >= limit) {	
					m2Index = 0;
				}
			}
			Microservice m1 = lMicroservice.get(m1Index);
			Microservice m2 = lMicroservice.get(m2Index);
			List<Vertex> m1Verticies = m1.getMutableVerticies();
			int totalVerticies = m1.getVerticies().size();
			double mutableVerticies = totalVerticies * this.crossoverFraction;
			if (m1Verticies.size() < mutableVerticies) {
				mutableVerticies = m1Verticies.size();
			}
			//System.out.println((int) mutableVerticies);
			List<Vertex> selectedVerticies = m1Verticies.subList(0, (int) mutableVerticies);
			selectedVerticies = new ArrayList<Vertex>(selectedVerticies);
			List<Vertex> selectedVerticiesCopy = new ArrayList<Vertex>(selectedVerticies);
			m1.removeAndAddVerticies(selectedVerticies, null);
			m2.removeAndAddVerticies(null, selectedVerticiesCopy);
			
			//garantir que o número total de métodos seja o mesmo.
			//assert solution.getMicroservices().
		}
		return source;
		/**
		boolean doExecute = this.random.nextDouble(0.0f, 1.0f) <= this.crossoverProbability;
		if (doExecute) {
			source = copySource(source);
			int sourceRandom = this.random.nextInt(0, source.size() - 1);
			MicroservicesSolution solution = source.get(sourceRandom);
			//System.out.println(solution.toString());
			List<Microservice> lMicroservice = solution.getMicroservices();
			int limit = lMicroservice.size() - 1;
			int m1Index = this.random.nextInt(0, limit);
			int m2Index = this.random.nextInt(0, limit);
			if (m1Index == m2Index) {
				++m2Index;
				if (m2Index >= limit) {
					m2Index = 0;
				}
			}
			Microservice m1 = lMicroservice.get(m1Index);
			Microservice m2 = lMicroservice.get(m2Index);
			List<Vertex> m1Verticies = m1.getVerticies();
			List<Vertex> selectedVerticies = m1Verticies.subList(0, (int) (m1Verticies.size() * this.crossoverFraction));
			selectedVerticies = new ArrayList<Vertex>(selectedVerticies);
			List<Vertex> selectedVerticiesCopy = new ArrayList<Vertex>(selectedVerticies);
			m1.removeAndAddVerticies(selectedVerticies, null);
			m2.removeAndAddVerticies(null, selectedVerticiesCopy);
			//System.out.println(solution.toString());
		}
		return source;
		**/
	}

	@Override
	public int getNumberOfRequiredParents() {
		return this.numberOfParents;
	}

	@Override
	public int getNumberOfGeneratedChildren() {
		return this.numberOfChildren;
	}

}
