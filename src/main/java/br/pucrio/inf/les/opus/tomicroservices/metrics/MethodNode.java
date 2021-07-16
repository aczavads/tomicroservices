package br.pucrio.inf.les.opus.tomicroservices.metrics;

//TODO: https://jgrapht.org/javadoc/
//TODO: JMetal manual: http://jmetal.sourceforge.net/resources/jMetalUserManual43.pdf
public class MethodNode {

	private String className;
	
	private String methodName;
		
	public MethodNode(String className, String methodName) {
		this.className = className;
		this.methodName = methodName;
	}

	public String getClassName() {
		return this.className;
	}

	public String getMethodName() {
		return this.methodName;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof MethodNode) {
			MethodNode node = (MethodNode) obj;
			return this.className.equals(node.getClassName()) &&
					this.methodName.equals(node.getMethodName());
		}
		return false;
	}
	
	public int hashCode() {
		return toString().hashCode();
	}
	
	public String toString() {
		return this.className + "." + this.methodName;
	}
}
