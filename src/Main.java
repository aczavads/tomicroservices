
public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World");
		System.out.println(test());
	}
	
	public static int test() {
		try {
			int a = 10/0;
			return 10;
		} catch (Throwable e) {
			throw e;
		} finally {
			System.out.println("FINALLY");
		}
	}
	
}
