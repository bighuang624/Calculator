package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Read {

	private static int currentLine = 0;

	@SuppressWarnings("resource")
	public static String[] readInput(String filePath) throws Exception {

		File file = new File(filePath);
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
		BufferedReader bf = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String str = null;
		while ((str = bf.readLine()) != null) {
			sb.append(str);
			currentLine++;
			if (!sb.toString().endsWith(String.valueOf(Operator.SEMICOLON))) {
				throw new MyException(ErrMsg.END_WITHOUT_SEM, currentLine, str.length());
			}
		}

		String iContent = sb.toString();
		return iContent.split(String.valueOf(Operator.SEMICOLON));
	}

}
