package br.pucrio.inf.les.opus.tomicroservices.analysis.ast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ASTAnalysis {
	
	public ASTAnalysis() {
		
	}
	
	public void parser(File file) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setResolveBindings(true);
        parser.setStatementsRecovery(true);
        parser.setBindingsRecovery(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        java.nio.file.Path sourcePath = Paths.get(file.toURI());
        String sourceString;
		try {
			sourceString = new String(Files.readAllBytes(sourcePath));
	        char[] source = sourceString.toCharArray();
	        parser.setSource(source);
	        
	        //IJavaProject myJavaProject = JavaCore.create(project);
	        //parser.setProject(myJavaProject);
	        
	        parser.setUnitName(sourcePath.toAbsolutePath().toString());
	        CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
	        astRoot.accept(new ASTAnalysisVisitor());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
