package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class CouplingPerMicroserviceArchitecture implements MetricPerMicroserviceArchitecture {
	
	private int objectiveIndexInProblem;
	
	public CouplingPerMicroserviceArchitecture() {

	}
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public double getValue(MicroservicesSolution microservicesSolution) {
		List<Microservice> microservices = microservicesSolution.getMicroservices();
		double result = 0;
		for (Microservice m: microservices) {
			m.addOrUpdateMetric(new CouplingPerMicroservice());
			result += m.getMetricValue(CouplingPerMicroservice.class.getName());
		}
		return result;
	}

	@Override
	public double printableValue(MicroservicesSolution microservicesSolution) {
		return getValue(microservicesSolution);
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
