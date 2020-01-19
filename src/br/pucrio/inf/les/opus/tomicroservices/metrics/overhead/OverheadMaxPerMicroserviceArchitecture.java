package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.ArrayList;
import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.MicroservicesSolution;

public class OverheadMaxPerMicroserviceArchitecture implements MetricPerMicroserviceArchitecture {

	private OverheadMaxPerMicroservice overheadMaxPerMicroservice = new OverheadMaxPerMicroservice();

	private int objectiveIndexInProblem;
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public double getValue(MicroservicesSolution microservicesSolution) {
		double result = 0l;
		List<Microservice> lMicroservices = microservicesSolution.getMicroservices();
		for (Microservice m: lMicroservices) {
			m.addOrUpdateMetric(this.overheadMaxPerMicroservice);
			result += m.getMetricValue(OverheadMaxPerMicroservice.class.getName());
		}
		return invertValue(result);
	}
	
	private double invertValue(double value) {
		return value;
		/**
		if (value != 0l) {
			return 1.0f/value;
		} else {
			return Double.MAX_VALUE;
		}
		**/
	}

	@Override
	public List<MetricPerMicroservice> getMetricPerMicroservice() {
		List<MetricPerMicroservice> lMetric = new ArrayList<MetricPerMicroservice>();
		lMetric.add(this.overheadMaxPerMicroservice);
		return lMetric;
	}

	@Override
	public double printableValue(MicroservicesSolution microservicesSolution) {
		return invertValue(getValue(microservicesSolution));
	}
	
	@Override
	public double printableValue(double value) {
		return invertValue(value);
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
