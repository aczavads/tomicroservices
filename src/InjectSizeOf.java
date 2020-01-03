import java.io.File;
import java.lang.instrument.Instrumentation;

/**
 * 
 * Caller transformer to insert sizeOf in system under analyze. 
 * Useful to simulate the microservices extraction and communication by network
 * 
 * @author Luiz Carvalho <lmcarvalho@inf.puc-rio.br>
 *
 */
public class InjectSizeOf {

	public static void agentmain(String agentArgs, Instrumentation inst) {
		System.err.println("Agentmain did not implemented");
		//inst.addTransformer(new SizeOfTransformer());
	}
	
	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("Premain");
		try {
			String args[] = agentArgs.split("@");
			final boolean acceptArg = args[0].equals("a");
			final File fileWithPatternArg = new File(args[1]);
			final File logFile = new File(args[2]);
			final File featureFile = new File(args[3]);
			ClassNamePattern pattern = new ClassNamePattern(fileWithPatternArg, acceptArg);
			Class.forName("java.lang.invoke.CallSite");
			inst.addTransformer(new SizeOfTransformer(pattern, logFile, featureFile));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
