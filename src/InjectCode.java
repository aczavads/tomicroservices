import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InjectCode {
	
	private int point;
	private int lastModified;
	private char[] contentFile;
	private String result;
	private String logFile;
	private String featureFile;
	
	private int ignoreAnotationWithPoint(int point) {
		char end = ' ';
		while (point < contentFile.length) {
			if (contentFile[point] == '(') {
				end = ')';
			}
			if (contentFile[point] == end) {
				++point;
				return point;
			}
			++point;
		}
		return point;
	}
	
	private void ignoreAnotation() {
		point = ignoreAnotationWithPoint(point);
	}
	
	public String injectCodeInAllFiles(File file) {
		return featureFile;
	}
	
	public String injectCodeMethods(char[] contentFile, String logFile, String featureFile) {
		this.contentFile = contentFile.clone();
		this.logFile = logFile;
		this.featureFile = featureFile;
		result = "";
		point = 0;
		lastModified = 0;
		for (; point < contentFile.length; ++point) {
			if (contentFile[point] == '@') {
				ignoreAnotation();
			} else if (contentFile[point] == '/' ) {
				ignoreComment();
			} else if (contentFile[point] == '\'' || contentFile[point] == '\"') {
				ignoreString();
			} else if (contentFile[point] == '(') {
				int openStart = point;
				if (isMethod()) {
					int startBlock = point;
					List<String> parameters = getParameters(openStart, startBlock);
					injectCodeMethod(parameters);
					injectCodeInBodyAndEndMethod();
				}
			}
		}
		return result + new String(contentFile, lastModified, point - lastModified - 1);
	}

	private boolean isReturn(int point) {
		final int returnWordSize = 6;
		String possibleReturn = new String(contentFile, point, returnWordSize);
		return possibleReturn.equals("return");
	}
	
	/**
	private void injectCodeInReturn() {
		while (point < contentFile.length) {
			if (contentFile[point] == '@') {
				ignoreAnotation();
			} else if (contentFile[point] == '/' ) {
				ignoreComment();
			} else if (contentFile[point] == 'r') {
				if (isReturn(point)) {
					String inject = "sizeOfPackage.SizeOf.decreaseDeep();";
					result += new String(contentFile, lastModified, point - lastModified);
					result += inject;
					lastModified = point + 1;
				}
				++point;
			}
		}
	}
	**/

	private void injectCodeInBodyAndEndMethod() {
		int countOpen = 0;
		while (point < contentFile.length) {
			if (contentFile[point] == '@') {
				ignoreAnotation();
			} else if (contentFile[point] == '/') {
				ignoreComment();
			}  else if (contentFile[point] == '\'' || contentFile[point] == '\"') {
				ignoreString();
			} else if (contentFile[point] == 'r') {
				if (isReturn(point)) {
					String inject = "sizeOfPackage.SizeOf.decreaseDeep();";
					result += new String(contentFile, lastModified, point - lastModified);
					result += inject;
					lastModified = point;
				}
				++point;
			} else if (contentFile[point] == '}') {
				--countOpen;
				if (countOpen == 0) {
					String inject = "} catch (Throwable e) {\n" + 
							"			sizeOfPackage.SizeOf.decreaseDeep();\n throw e;\n" + 
							"		} finally {\n" + 
							"			sizeOfPackage.SizeOf.decreaseDeep();\n" + 
							"		} ";
					result += new String(contentFile, lastModified, point - lastModified);
					result += inject;
					lastModified = point;
					++point;
					return;
				}
				++point;
			} else if (contentFile[point] == '{') {
				++countOpen;
				++point;
			} else {
				++point;
			}
		}
	}
	
	private void ignoreComment() {
		point = ignoreCommentWithPoint(point);
	}

	private int ignoreCommentWithPoint(int point) {
		char first = contentFile[point];
		char second = contentFile[point + 1];
		char firstEnd;
		char secondEnd;
		if (first == '/' && second == '/') {
			firstEnd = '\\';
			secondEnd = 'n';
		} else if (first == '/' && second == '*') {
			firstEnd = '*';
			secondEnd = '/';
		} else {
			return point;
		}
		point += 2;
		while (point < contentFile.length) {
			if (contentFile[point] == firstEnd && contentFile[point + 1] == secondEnd) {
				point += 2;
				return point;
			}
		}
		return point;
	}
	
	private void ignoreString() {
		point = ignoreStringWithPoint(point);
	}
	
	private int ignoreStringWithPoint(int point) {
		char end;
		if (contentFile[point] == '\'') {
			end = '\'';
			++point;
		} else if (contentFile[point] == '\"') {
			++point;
			end = '\"';
		} else {
			return point;
		}
		while (point < contentFile.length) {
			if (contentFile[point] == '\\' && contentFile[point + 1] == end) {
				point += 2;
			} else if (contentFile[point] == end) {
				return point + 1;
			} else {
				++point;
			}
		}
		return point;
	}

	private void injectCodeMethod(List<String> parameters) {
		String callMethod = "sizeOfPackage.SizeOf.sizeOfLog = new java.io.File(" + logFile + "); ";
		callMethod += "sizeOfPackage.SizeOf.featureInfo = new java.io.File(" + featureFile + "); ";
		callMethod += "try { sizeOfPackage.SizeOf.saveSizeOfLog(";
		callMethod += "new Object().getClass().getEnclosingClass().getName(), ";
		callMethod += "new Object().getClass().getEnclosingMethod().getName(), ";
		if (parameters.isEmpty()) {
			callMethod += "0l";
		} else {
			boolean first = true;
			for (String p : parameters) {
				if (first == true) {
					first = false;
					callMethod += "sizeOfPackage.SizeOf.sizeOf(" + p + ")";
				} else {
					callMethod +=  "+ sizeOfPackage.SizeOf.sizeOf(" + p + ")";
				}
			}
		}
		callMethod += "); ";
		result += new String(contentFile, lastModified, point - lastModified + 1);
		result += callMethod;
		lastModified = point + 1; //can be a problem
	}

	private List<String> getParameters(int openStart, int startBlock) {
		List<String> parameters = new ArrayList<String>();
		String str = "";
		for (int i = openStart ; i < startBlock; ++i) {
			if (contentFile[i] == '@') {
				i = ignoreAnotationWithPoint(i);
				if (i >= startBlock) {
					break;
				}
			} else if (contentFile[point] == '/' ) {
				i = ignoreCommentWithPoint(i);
				if (i >= startBlock) {
					break;
				}
			} else if (contentFile[point] == '\'' || contentFile[point] == '\"') {
				i = ignoreStringWithPoint(i);
				if (i >= startBlock) {
					break;
				}
			}
			str += contentFile[i];
		}
		int index = str.indexOf(")");
		str = str.substring(0, index);
		str = str.replace("(", "");
		String typeAndParameters[] = str.split(",");
		for (int i = 0; i < typeAndParameters.length; i = i + 1) {
			char typeAndParameter[] = typeAndParameters[i].toCharArray();
			String parameter = "";
			boolean endFirstString = false;
			boolean startFirstString = false;
			boolean startSecondString = false;
			for (int j = 0; j < typeAndParameter.length; ++j) {
				if (typeAndParameter[j] != ' ') {
					startFirstString = true;
					if (endFirstString) {
						startFirstString = false;
						startSecondString = true;
						parameter += typeAndParameter[j];
					}
				} else if (startFirstString) {
					endFirstString = true;
				} else if (startSecondString) {
					break;
				}
			}
			if (!parameter.equals("")) {
				parameters.add(parameter);
			}
		}
		return parameters;
	}

	private boolean isKeyWord(String word) {
		final String keys[] = {"this", "super", "if", "switch", "while", "for", 
				"synchronized", "catch"};
		for (int i = 0; i < keys.length; ++i) {
			if (keys[i].equals("word")) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isMethod() {
		int ret = point - 1;
		String lastWord = "";
		boolean findWord = false;
		while (ret >= 0) {
			if (contentFile[ret] != ' ') {
				findWord = true;
				lastWord = contentFile[ret] + lastWord;
			} else {
				if (findWord) {
					break;
				}
			}
			--ret;
		}
		if (isKeyWord(lastWord)) {
			return false;
		}
		while (point < contentFile.length) {
			if (contentFile[point] == '@') {
				ignoreAnotation();
			} else if (contentFile[point] == '/' ) {
				ignoreComment();
			} else if (contentFile[point] == '\'' || contentFile[point] == '\"') {
				ignoreString();
			} else if (contentFile[point] == ';') {
				return false;
			} else if (contentFile[point] == '{') {
				return true;
			} else {
				++point;
			}
		}
		return false;
	}

}
