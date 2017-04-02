package analyser;

import java.util.List;

import system.ErrMsg;
import system.KeyWord;
import system.MyException;
import system.Variables;

public class SentenceAnalyser {

	public static boolean isKeyWord(String str) {
		if (str.equals(KeyWord.PRINT))
			return true;
		return false;
	}

	public static void sentenceFun(List<String> list, int line) throws MyException {

		String str = list.get(0);
		if (str.matches("[a-zA-Z]+[a-zA-Z0-9]*")) {
			if (str.equals(KeyWord.PRINT)) { // 输出语句
				if (list.get(1).equals("(") && list.get(list.size() - 1).equals(")")) {
					list.remove(0);
					System.out.println(LexicalAnalyser.evaluate(list, line));
				} else {
					if (!list.get(1).equals("("))
						throw new MyException(ErrMsg.WRONG_FORMAT_OF_EXPRESSION, line, KeyWord.PRINT.length());
					else
						throw new MyException(ErrMsg.WRONG_FORMAT_OF_EXPRESSION, line, str.length() - 1);
				}
			} else { // 赋值语句
				if (list.get(1).equals("=")) {
					list.remove(0);
					list.remove(0);
					Variables.varsMap.put(str, LexicalAnalyser.evaluate(list, line));
				} else {
					throw new MyException(ErrMsg.WRONG_ASSIGNMENT, line, list.get(0).length());
				}
			}
		} else {
			throw new MyException(ErrMsg.WRONG_FORMAT_OF_EXPRESSION, line, 0);
		}

	}

}
