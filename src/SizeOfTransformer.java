import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * 
 *  Insert SizeOf in system under analyze
 *  
 *  Warning: SizeOf class in sizeOfPackage, so change SizeOfTransformer in case renaming sizeOfPackage or class name.
 *  
 *  @author Luiz Carvalho <lmcarvalho@inf.puc-rio.br>
 *  
 * Based on: http://appcrawler.com/wordpress/2013/01/02/simple-byte-code-injection-example-with-javassist/
 * https://stackoverflow.com/questions/20316965/get-a-name-of-a-method-parameter-using-javassist
 * https://stackoverflow.com/questions/43027872/how-to-print-out-all-methods-called-during-runtime-in-java-using-instrumentation
 * http://www.javassist.org/tutorial/tutorial2.html (Add method and use variable)
 * https://www.programcreek.com/java-api-examples/?api=javassist.bytecode.LocalVariableAttribute
*/
public class SizeOfTransformer implements ClassFileTransformer {
	
	private ClassNamePattern pattern;
	
	public SizeOfTransformer(ClassNamePattern pattern, File logFile) {
		super();
		this.pattern = pattern;
		sizeOfPackage.SizeOf.sizeOfLog = logFile;		
	}
	
	public byte[] transform(ClassLoader loader, String className, Class redefiningClass, ProtectionDomain domain,
			byte[] bytes) throws IllegalClassFormatException {
		ClassPool pool = ClassPool.getDefault();
		CtClass cl = null;
		try {
			cl = pool.makeClass(new java.io.ByteArrayInputStream(bytes));
			String clName = cl.getName();
			if (pattern.isAcceptable(clName)) {
				System.out.println("Inject in " + clName.toString());
				CtBehavior[] methods = cl.getDeclaredBehaviors(); //TODO - verficar declared ou apenas methods
				addSizeOfMethods(cl);
				for (int i = 0; i < methods.length; i++) {
			        if (methods[i].isEmpty() == false) {
			        	changeMethod(methods[i], clName);
			        }
			    }
			}
		    bytes = cl.toBytecode();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cl != null) {
				cl.detach();
		    }
		}
		return bytes;
	}

	/**
	 * Add method in original class to call sizeOf
	 * @param ctClass cl class to be transformed
	 */	
	private void addSizeOfMethods(CtClass cl) {
		CtMethod m;
		try {
			String str = "";
			m = CtNewMethod.make("public static long ____sizeOf(Object o) {return sizeOfPackage.SizeOf.sizeOf(o);}", 
					cl);
			cl.addMethod(m);
		} catch (CannotCompileException e) {
			System.err.println("Can not compile sizeOf method - maybe you forgot to add sizeOf in classpath");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Change method to insert a sizeOf call in start
	 * @param method method to change
	 */
	private void changeMethod(CtBehavior method, String className) {
		if (!Modifier.isNative(method.getModifiers())) { 
			List<String> methodParameters = methodParameterName(method);
			if (methodParameters.isEmpty()) { return; }
			try {
				//String sizeOfCall = "System.out.println(";
				final String quote = "\"";
				String sizeOfCall = "sizeOfPackage.SizeOf.saveSizeOfLog(";
				sizeOfCall += quote + className + quote + "," + quote + method.getName() + quote 
						+ ",";
				boolean first = true;
				for (String parameterName: methodParameters) {
					if (first) {
						first = false;
					} else {
						sizeOfCall += " + ";
					}
					sizeOfCall += "____sizeOf(" + parameterName + ")";
				}
				sizeOfCall += ");";
				System.out.println(sizeOfCall);
				method.insertBefore(sizeOfCall);
			} catch (CannotCompileException e) {
				System.err.println("Can not insert ____sizeOf");
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Return parameters names from a method
	 * E.g: methodX(int a, int b) => (a, b)
	 * @param method method
	 * @return parameters names
	 */
	public List<String> methodParameterName(CtBehavior method) {
		List<String> result = new ArrayList<String>();
		MethodInfo methodInfo = method.getMethodInfo();
    	LocalVariableAttribute table = (LocalVariableAttribute) methodInfo.getCodeAttribute().
    			getAttribute(javassist.bytecode.LocalVariableAttribute.tag);
    	int parametersSize;
		try {
			parametersSize = method.getParameterTypes().length;
			String parameterName = null;
	    	int frame;
	    	for (int i = 0; i < parametersSize; ++i) {
	        	frame = table.nameIndex(i); 
	        	parameterName = methodInfo.getConstPool().getUtf8Info(frame);
	        	if (parameterName.equals("this")) {
	        		++parametersSize; //It don't count 'this' like parameter
	        	} else {
	        		result.add(parameterName);
	        	}
	    	}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
    	return result;
	}
	
}
	