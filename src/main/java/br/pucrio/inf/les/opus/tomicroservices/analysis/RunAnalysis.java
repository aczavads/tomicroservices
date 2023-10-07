package br.pucrio.inf.les.opus.tomicroservices.analysis;

import java.io.File;

import br.pucrio.inf.les.opus.tomicroservices.analysis.ast.ASTAnalysis;

public class RunAnalysis {
	
	public static void main(String[] args) {
		File fileToAnalysis = new File(args[0]);
		System.out.println(args[0]);
		ASTAnalysis analysis = new ASTAnalysis();
		analysis.parser(fileToAnalysis);
	}
	
}
