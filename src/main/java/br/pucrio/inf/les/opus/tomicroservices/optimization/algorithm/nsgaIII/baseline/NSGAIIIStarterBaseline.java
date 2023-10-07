package br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaIII.baseline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.metrics.ConvertValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.Minimize;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.CohesionPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.CouplingPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.FunctionalityPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.OverheadMaxPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.ReusePerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaIII.toMicroservices.NSGAIIIMonitor;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaIII.toMicroservices.NSGAIIIStarter;
import br.pucrio.inf.les.opus.tomicroservices.optimization.ranking.EuclideanDistanceRanking;

public class NSGAIIIStarterBaseline extends NSGAIIIStarter {

	protected List<MetricPerMicroserviceArchitecture> additionalMetrics;
	
	public NSGAIIIStarterBaseline(File saveExecutions) {
		super(saveExecutions);
	}

	@Override
	protected void startMetrics() {
		System.out.println("execute");
		super.metrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		ConvertValue minimize = new Minimize();
		
		super.metrics.add(new CouplingPerMicroserviceArchitecture());
		super.metrics.add(new CohesionPerMicroserviceArchitecture(minimize));
		
		this.additionalMetrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		this.additionalMetrics.add(new OverheadMaxPerMicroserviceArchitecture());
		this.additionalMetrics.add(new FunctionalityPerMicroserviceArchitecture(minimize));
		this.additionalMetrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));
	}
	
	@Override
	public void start() {
		super.setting();
		super.monitor = new NSGAIIIMonitor(new EuclideanDistanceRanking(), this.additionalMetrics);
		try {
			super.monitor.monitor(graph, super.metrics, numberOfMicroservices, random, maxPopulation, maxIterations, 
					intervalToMonitor, saveExecutions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
