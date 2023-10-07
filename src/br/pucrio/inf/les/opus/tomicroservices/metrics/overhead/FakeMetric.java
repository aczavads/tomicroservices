package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.pucrio.inf.les.opus.tomicroservices.metrics.ConvertValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class FakeMetric implements MetricPerMicroserviceArchitecture {
	private int objectiveIndexInProblem;
	private Map<MicroservicesSolution, Double> valores = new HashMap<>();

	public FakeMetric() {
	}
	
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public double getValue(MicroservicesSolution microservicesSolution) {
		double randomValue = (new Random()).nextDouble();
		if (randomValue == 0.0) {
			return Double.MAX_VALUE;
		}
		double value = 1.0/randomValue;
		valores.put(microservicesSolution, value);
		return value;
	}

	@Override
	public double printableValue(MicroservicesSolution microservicesSolution) {
		return this.valores.get(microservicesSolution);
	}

	@Override
	public double printableValue(double value) {
		return value;
	}

	@Override
	public List<MetricPerMicroservice> getMetricPerMicroservice() {
		return null;
	}

	@Override
	public void setObjectiveIndex(int index) {
		this.objectiveIndexInProblem = index;
	}

	@Override
	public int getObjectiveIndex() {
		return this.objectiveIndexInProblem;
	}

}
