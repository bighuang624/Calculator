package system;

public class MyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyException(String errmsg) {
		System.err.println("Error: " + errmsg);
	}

	public MyException(String errmsg, int line) {
		System.err.println("Error(line " + line + "): " + errmsg);
	}

	public MyException(String errmsg, int line, int position) {
		System.err.println("Error(line " + line + ",position " + position + "): " + errmsg);
	}
}
