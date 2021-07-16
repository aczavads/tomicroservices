package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.metrics.ConvertValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class ReusePerMicroserviceArchitecture implements MetricPerMicroserviceArchitecture {

	private ConvertValue convertValue;
	
	private int objectiveIndexInProblem;
	
	private String userVertexName;
	
	private int threshold;
	
	public ReusePerMicroserviceArchitecture(String userVertexName, int threshold, ConvertValue convertValue) {
		this.convertValue = convertValue;
		startCommonFields(userVertexName, threshold);
	}
	
	public ReusePerMicroserviceArchitecture(String userVertexName, int threshold) {
		startCommonFields(userVertexName, threshold);
	}
	
	private void startCommonFields(String userVertexName, int threshold) {
		this.userVertexName = userVertexName;
		this.threshold = threshold;
	}
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public double getValue(MicroservicesSolution microservicesSolution) {
		List<Microservice> microservices = microservicesSolution.getMicroservices();
		double msa = microservices.size();
		double result = 0;
		for (Microservice m: microservices) {
			m.addOrUpdateMetric(new ReusePerMicroservice(this.userVertexName, this.threshold));
			result += m.getMetricValue(ReusePerMicroservice.class.getName());
		}
		return this.convertValue.convert(result/msa);
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
