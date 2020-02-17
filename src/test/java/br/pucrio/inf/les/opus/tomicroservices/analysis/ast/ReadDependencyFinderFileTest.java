package br.pucrio.inf.les.opus.tomicroservices.analysis.ast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;

class ReadDependencyFinderFileTest {

	File patternFile;
	ClassNamePattern pattern;
	
	@BeforeEach
	public void pattern() {
		this.patternFile = new File("_______________PATTERNnnsss_________");
		this.pattern = new ClassNamePattern(patternFile, true);
		try {
			String patternContent = "Main.*\n" + "java.lang.*";
			//String patternContent = "csbase.*\n" + "csgrid.*\n" + "ibase.*\n";
			FileUtils.writeStringToFile(this.patternFile, patternContent, "UTF-8");
		} catch (IOException e) {
			fail("Can no write patternFile");
		}
	}


	@Test
	void testWithOneBounded() {
		ReadDependencyFinderFile dependencyFinder = new ReadDependencyFinderFile();
		File xmlFile = new File("___xmlFileFromDependencyFinder______");
		try {
			String xmlContent = "<feature confirmed=\"yes\">\n" + 
					"                <name>Main.Main()</name>\n" + 
					"                <outbound type=\"feature\" confirmed=\"no\">java.lang.Object.Object()</outbound>\n" + 
					"            </feature>";
			FileUtils.writeStringToFile(xmlFile, xmlContent, "UTF-8");
			Graph graph = new Graph();
			dependencyFinder.insertInGraphFromFile(xmlFile, graph, this.pattern);
			assertTrue(graph.containsVertexByLabel("Main.Main()"));
			assertTrue(graph.containsVertexByLabel("java.lang.Object.Object()"));
			assertFalse(graph.containsVertexByLabel("Main.MainX()"));
		} catch (IOException e) {
			fail("Can not write xmlFile");
			e.printStackTrace();
		}
	}
	
	@Test
	void testWithThreeBounded() {
		ReadDependencyFinderFile dependencyFinder = new ReadDependencyFinderFile();
		File xmlFile = new File("___xmlFileFromDependencyFinder______");
		try {
			String xmlContent = "<feature confirmed=\"yes\">\n" + 
					"                <name>Main.Main()</name>\n" + 
					"                <outbound type=\"feature\" confirmed=\"no\">java.lang.Object.Object()</outbound>\n" + 
					"                <outbound type=\"feature\" confirmed=\"no\">java.lang.Object.Object2()</outbound>\n" + 
					"                <inbound type=\"feature\" confirmed=\"no\">java.lang.Object.Object3()</outbound>\n" + 
					"            </feature>" + 
					"            <otherline>"; 
			FileUtils.writeStringToFile(xmlFile, xmlContent, "UTF-8");
			Graph graph = new Graph();
			dependencyFinder.insertInGraphFromFile(xmlFile, graph, this.pattern);
			assertTrue(graph.containsVertexByLabel("Main.Main()"));
			assertTrue(graph.containsVertexByLabel("java.lang.Object.Object()"));
			assertFalse(graph.containsVertexByLabel("Main.MainY()"));
		} catch (IOException e) {
			fail("Can not write xmlFile");
			e.printStackTrace();
		}
	}
	
	@Test
	void testWithDependency() {
		ReadDependencyFinderFile dependencyFinder = new ReadDependencyFinderFile();
		File xmlFile = new File("___xmlFileFromDependencyFinder______");
		try {
			String xmlContent = "<dependencies>\n" + 
					"    <package confirmed=\"yes\">\n" + 
					"        <name></name>\n" + 
					"        <class confirmed=\"yes\">\n" + 
					"            <name>Main</name>\n" + 
					"            <outbound type=\"class\" confirmed=\"no\">java.lang.Object</outbound>\n" + 
					"            <feature confirmed=\"yes\">\n" + 
					"                <name>Main.Main()</name>\n" + 
					"                <outbound type=\"feature\" confirmed=\"no\">java.lang.Object.Object()</outbound>\n" + 
					"            </feature>"; 
			FileUtils.writeStringToFile(xmlFile, xmlContent, "UTF-8");
			Graph graph = new Graph();
			dependencyFinder.insertInGraphFromFile(xmlFile, graph, this.pattern);
			assertTrue(graph.containsVertexByLabel("Main.Main()"));
			assertTrue(graph.containsVertexByLabel("java.lang.Object.Object()"));
			assertFalse(graph.containsVertexByLabel("Main.MainX()"));
			String strGraph = graph.toString();
			assertEquals("Main.Main() to java.lang.Object.Object()\n", strGraph);
		} catch (IOException e) {
			fail("Can not write xmlFile");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWithIgnoreCase() {
		ReadDependencyFinderFile dependencyFinder = new ReadDependencyFinderFile();
		File xmlFile = new File("___xmlFileFromDependencyFinder______");
		try {
			String xmlContent = "<dependencies>\n" + 
					"    <package confirmed=\"yes\">\n" + 
					"        <name></name>\n" + 
					"        <class confirmed=\"yes\">\n" + 
					"            <name>Main</name>\n" + 
					"            <outbound type=\"class\" confirmed=\"no\">java.lang.Object</outbound>\n" + 
					"            <feature confirmed=\"yes\">\n" + 
					"                <name>Main.Main()</name>\n" + 
					"                <outbound type=\"feature\" confirmed=\"no\">lang.Object.Object()</outbound>\n" + 
					"            </feature>"; 
			FileUtils.writeStringToFile(xmlFile, xmlContent, "UTF-8");
			Graph graph = new Graph();
			dependencyFinder.insertInGraphFromFile(xmlFile, graph, this.pattern);
			assertFalse(graph.containsVertexByLabel("Main.Main()"));
			assertFalse(graph.containsVertexByLabel("java.lang.Object.Object()"));
		} catch (IOException e) {
			fail("Can not write xmlFile");
			e.printStackTrace();
		}
	}
	
	@Test
	void test() {
		this.patternFile = new File("/home/luizmatheus/tecgraf/patterns");
		this.pattern = new ClassNamePattern(patternFile, true);
		
		ReadDependencyFinderFile dependencyFinder = new ReadDependencyFinderFile();
		File xmlFile = new File("/home/luizmatheus/tecgraf/csbaseDependency");
		Graph graph = new Graph();
		dependencyFinder.insertInGraphFromFile(xmlFile, graph, this.pattern);
		System.out.println(graph.getVerticesSize());
		System.out.println("LISTS");
		List[] lists = new List[2000];
		for (int i = 0; i < 2000; ++i) {
			System.out.println(i);
			lists[i] = new ArrayList<>();
			lists[i].addAll(graph.getVerticies());
		}
		//String strGraph = graph.toString();
		//System.out.println(strGraph);
	}

}
