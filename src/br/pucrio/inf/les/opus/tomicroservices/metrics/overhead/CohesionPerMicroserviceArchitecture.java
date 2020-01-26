package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.metrics.ConvertValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.MicroservicesSolution;

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
			System.out.println(m);
			if (Double.isNaN(result)) {
				System.out.println("IT'S IT");
			}
		}
		return this.convertValue.convert(result);
	}

	@Override
	public double printableValue(MicroservicesSolution microservicesSolution) {
		return this.convertValue.convert(getValue(microservicesSolution));
	}

	@Override
	public double printableValue(double value) {
		return this.convertValue.convert(value);
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
