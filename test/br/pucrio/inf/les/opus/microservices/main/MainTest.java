package br.pucrio.inf.les.opus.microservices.main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

public class MainTest {

	@Test
	public void mainTest() throws Exception {
		File logFile = new File("___logFile___");
		File outputFile = new File("______output");
		logFile.deleteOnExit();
		outputFile.deleteOnExit();
		FileUtils.writeStringToFile(logFile, "Class:A#Method:a#SizeOf:100\n"
				+ "Class:A#Method:b#SizeOf:300\n"
				+ "Class:B#Method:c#SizeOf:120\n"
				+ "Class:A#Method:a#SizeOf:8\n"
				+ "Class:A#Method:x#SizeOf:0\n", "UTF-8");
		String[] args = new String[6];
		args[0] = "-logfile";
		args[1] = logFile.getAbsolutePath();
		args[2] = "-numbermicroservices";
		args[3] = "2";
		args[4] = "-outputfile";
		args[5] = outputFile.getAbsolutePath();
		br.pucrio.inf.les.opus.tomicroservices.main.Main main = new br.pucrio.inf.les.opus.tomicroservices.main.Main();
		main.main(args);
		assertEquals("Method Graph:\n" + 
				"strict graph G {\n" + 
				"  _star_t__star_t_ [ label=\"_star_t_._star_t_\" ];\n" + 
				"  Aa [ label=\"A.a\" ];\n" + 
				"  Ab [ label=\"A.b\" ];\n" + 
				"  Bc [ label=\"B.c\" ];\n" + 
				"  Ax [ label=\"A.x\" ];\n" + 
				"  _star_t__star_t_ -- Aa [ label=\"100\" ];\n" + 
				"  Aa -- Ab [ label=\"300\" ];\n" + 
				"  Ab -- Bc [ label=\"120\" ];\n" + 
				"  Bc -- Aa [ label=\"8\" ];\n" + 
				"  Aa -- Ax [ label=\"0\" ];\n" + 
				"}\n" + 
				"\n" + 
				"Clustering Method Graph:\n" + 
				"strict graph G {\n" + 
				"  Aa [ label=\"A.a\" ];\n" + 
				"  Ax [ label=\"A.x\" ];\n" + 
				"  Bc [ label=\"B.c\" ];\n" + 
				"  _star_t__star_t_ [ label=\"_star_t_._star_t_\" ];\n" + 
				"  Ab [ label=\"A.b\" ];\n" + 
				"  Aa -- Ax [ label=\"0\" ];\n" + 
				"  Bc -- Aa [ label=\"8\" ];\n" + 
				"  _star_t__star_t_ -- Aa [ label=\"100\" ];\n" + 
				"}\n\n"
				, FileUtils.readFileToString(outputFile, "UTF-8"));
		outputFile.deleteOnExit();
		logFile.deleteOnExit();
	}
	
}
