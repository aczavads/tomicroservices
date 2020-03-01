package br.pucrio.inf.les.opus.tomicroservices.main;

import java.io.File;

import br.pucrio.inf.les.opus.tomicroservices.optimization.NSGAIIStarter;

public class Main {
	
	public static void main(String[] args) {
		final int executions = 1;
		for (int i = 0; i < executions; ++i) {
			NSGAIIStarter starter = new NSGAIIStarter(new File("/home/luizmatheus/TSE/NSGAII/execution" + i));
			starter.start();
		}
		
		
		/**
		final int executions = 1;
		for (int i = 0; i < executions; ++i) {
			NSGAIIIStarter starter = new NSGAIIIStarter(new File("/home/luizmatheus/TSE/execution" + i));
			starter.start();
		}
		**/
		///home/luizmatheus/TSE/selectedCase/solution32
		/**
		NSGAIIIStarter starter = new NSGAIIIStarterFromModifiedCase(
				new File("/home/luizmatheus/TSE/usingFeatures/fiveMSA/secondExecution/case")
				, new File("/home/luizmatheus/TSE/usingFeatures/fiveMSA/3Execution")
				);
		starter.start();
		**/
	}
	
	/**
	public static void main(String[] args) {
		Options options = new Options();
		final String logFileOption = "logfile";
		final String numberOfMicroservicesOption = "numbermicroservices";
		final String outputFileOption = "outputfile";
		options.addOption(logFileOption, true, "Path to log file generated by toMicroservices agent");
		options.addOption(numberOfMicroservicesOption, true, "Number of microservices generated");
		options.addOption(outputFileOption, true, "Path to Output file");
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			boolean fail = false;
			if (!cmd.hasOption(logFileOption)) {
				System.err.println("There is not " + logFileOption);
				fail = true;
			} 
			if (!cmd.hasOption(numberOfMicroservicesOption)) {
				System.err.println("There is not " + numberOfMicroservicesOption);
				fail = true;
			}
			if (!cmd.hasOption(outputFileOption)) {
				System.err.println("There is not " + outputFileOption);
				fail = true; //TODO - remove code duplication
			}
			if (fail) return;
			String logFilePath = cmd.getOptionValue(logFileOption);
			File logFile = new File(logFilePath);
			String numberMicroservicesStr = cmd.getOptionValue(numberOfMicroservicesOption);
			int nMicroservices = Integer.parseInt(numberMicroservicesStr);
			String outputPath = cmd.getOptionValue(outputFileOption);
			File outputFile = new File(outputPath);
			PrintGraph graph = new PrintGraph();
			graph.print(logFile, outputFile, nMicroservices);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	**/
	
}
