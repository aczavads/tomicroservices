package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.metrics.ConvertValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class CohesionPerMicroserviceArchitecture implements MetricPerMicroserviceArchitecture {

	private ConvertValue convertValue;
	
	private int objectiveIndexInProblem;
	
	public CohesionPerMicroserviceArchitecture(ConvertValue convertValue) {
		this.convertValue = convertValue;
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
			m.addOrUpdateMetric(new CohesionPerMicroservice());
			result += m.getMetricValue(CohesionPerMicroservice.class.getName());
		}
		return this.convertValue.convert(result);
	}

	@Override
	public double printableValue(MicroservicesSolution microservicesSolution) {
		//return this.convertValue.convert(getValue(microservicesSolution));
		return getValue(microservicesSolution);
	}

	@Override
	public double printableValue(double value) {
		//return this.convertValue.convert(value);
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
