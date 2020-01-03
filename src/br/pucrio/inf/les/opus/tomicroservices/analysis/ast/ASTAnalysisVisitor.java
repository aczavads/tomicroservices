package br.pucrio.inf.les.opus.tomicroservices.analysis.ast;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * https://help.eclipse.org/2019-12/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjdt%2Fcore%2Fdom%2FMethodInvocation.html
 * 
 * @author luizmatheus
 *
 */
public class ASTAnalysisVisitor extends ASTVisitor {
	
	
	
	public boolean visit(MethodInvocation node) {
		SimpleName methodName = node.getName();
		System.out.println(methodName);
		ASTNode parent = node.getParent();
		System.out.println(node.getExpression());
		System.out.println(node.toString());
		int i = 1;
		IMethodBinding methodBinding = node.resolveMethodBinding();
		if (methodBinding != null) {
			System.out.println("NAME BIDING:" + methodBinding.getDeclaringClass().getQualifiedName());
		} else {
			System.out.println("IT S NULL");
		}
		
		while ( !(parent instanceof TypeDeclaration) ) {
			System.out.println(parent.getClass().toString());
			System.out.println(i++);
			parent = parent.getParent();
		}
		if (parent != null) {
			TypeDeclaration clazz = (TypeDeclaration) parent;
			System.out.println(clazz.getName());
		}
		return true;
	}
	
}
