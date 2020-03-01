package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.io.File;
import java.util.ArrayList;

import br.pucrio.inf.les.opus.tomicroservices.metrics.ConvertValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.Minimize;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.CohesionPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.CouplingPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.FunctionalityPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.OverheadMaxPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.ReusePerMicroserviceArchitecture;

public class NSGAIIStarter extends Starter {

	private ArrayList<MetricPerMicroserviceArchitecture> otherMetrics; 
	
	public NSGAIIStarter(File saveExecutions) {
		super(saveExecutions);
	}

	@Override
	public void start() {
		super.setting();
		metrics();
		try {
			NSGAIIMonitor monitor = new NSGAIIMonitor(new EuclideanDistanceRanking(), this.otherMetrics);
			monitor.monitor(graph, metrics, numberOfMicroservices, random, maxPopulation, maxIterations, intervalToMonitor, saveExecutions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void metrics() {
		super.metrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		this.otherMetrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		ConvertValue minimize = new Minimize();
		super.metrics.add(new CouplingPerMicroserviceArchitecture());
		super.metrics.add(new CohesionPerMicroserviceArchitecture(minimize));
		this.otherMetrics.add(new OverheadMaxPerMicroserviceArchitecture());
		this.otherMetrics.add(new FunctionalityPerMicroserviceArchitecture(minimize));
		this.otherMetrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));
	}	
	
	/**
	@Override
	public void start() {
		setting();
		metrics();
		try {
			monitor.monitor(graph, metrics, numberOfMicroservices, random, maxPopulation, maxIterations, 
					intervalToMonitor, saveExecutions);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	**/
	
}
