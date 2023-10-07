package br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.randomsearch;

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
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.Starter;
import br.pucrio.inf.les.opus.tomicroservices.optimization.ranking.EuclideanDistanceRanking;

public class RandomSearchStarter extends Starter {

	protected List<MetricPerMicroserviceArchitecture> additionalMetrics;
	
	public RandomSearchStarter(File saveExecutions) {
		super(saveExecutions);
	}

	@Override
	public void metrics() {
		ConvertValue minimize = new Minimize();

		super.metrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		super.metrics.add(new CouplingPerMicroserviceArchitecture());
		super.metrics.add(new CohesionPerMicroserviceArchitecture(minimize));
		super.metrics.add(new OverheadMaxPerMicroserviceArchitecture());
		super.metrics.add(new FunctionalityPerMicroserviceArchitecture(minimize));
		super.metrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));
		
		this.additionalMetrics = new ArrayList<MetricPerMicroserviceArchitecture>(); 
		this.additionalMetrics.add(new CouplingPerMicroserviceArchitecture());
		this.additionalMetrics.add(new CohesionPerMicroserviceArchitecture(minimize));
		this.additionalMetrics.add(new OverheadMaxPerMicroserviceArchitecture());
		this.additionalMetrics.add(new FunctionalityPerMicroserviceArchitecture(minimize));
		this.additionalMetrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));
	}

	@Override
	public void start() {
		super.setting();
		metrics();
		RandomSearchMonitor rs = new RandomSearchMonitor(new EuclideanDistanceRanking(), 
				this.additionalMetrics);
		try {
			final int rsIntervalToMonitor = 1;
			rs.monitor(graph, metrics, numberOfMicroservices, random, maxPopulation, 
					maxIterations, rsIntervalToMonitor, saveExecutions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
