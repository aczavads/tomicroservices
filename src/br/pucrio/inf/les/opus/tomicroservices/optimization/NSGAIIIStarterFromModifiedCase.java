package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.io.File;
import java.util.List;

public class NSGAIIIStarterFromModifiedCase extends NSGAIIIStarter {

	private File microservicesFolder;
	
	public NSGAIIIStarterFromModifiedCase(File targetFolder, File saveNewExecution) {
		super(saveNewExecution);
		this.microservicesFolder = targetFolder;
	}
	
	@Override
	public void start() {
		setting();
		MicroservicesFromFile fromFile = new MicroservicesFromFile();
		try {
			List<MicroservicesSolution> solution = fromFile.read(this.microservicesFolder, 
					super.graph);
			super.monitor.monitor(super.graph, super.metrics, 
					super.numberOfMicroservices, super.random, 
					super.maxPopulation, super.maxIterations, 
					super.intervalToMonitor, solution, super.saveExecutions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void setting() {
		super.setting();
	}
}
