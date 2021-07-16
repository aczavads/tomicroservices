package br.pucrio.inf.les.opus.tomicroservices.metrics;

public interface HasMetric<M extends Metric> {

	public void addOrUpdateMetric(M metric);
	
	
}
