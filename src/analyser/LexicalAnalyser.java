package analyser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import system.ErrMsg;
import system.MyException;
import system.Operator;
import system.Variables;

public class LexicalAnalyser {

	private static StringBuffer varRegister = new StringBuffer(); // 变量名寄存器
	private static StringBuffer digitRegister = new StringBuffer(); // 数字寄存器
	private static boolean spaceRegister = false; // 记录是否有空白符

	private static boolean isDigit(char ch) {
		if (ch >= '0' && ch <= '9')
			return true;
		return false;
	}

	private static boolean isAlpha(char ch) {
		if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
			return true;
		return false;
	}

	private static boolean isOperator(char ch) {
		for (int i = 0; i < Operator.OPERATOR.length; i++) {
			if (Operator.OPERATOR[i] == ch)
				return true;
		}
		return false;
	}

	private static boolean isPoint(char ch) {
		if (ch == '.')
			return true;
		return false;
	}

	private static boolean isSpace(char ch) {
		if (ch == 10 || ch == 32)
			return true;
		return false;
	}
	
	// 词法分析
	public static List<String> lexAnalysis(String str, int line) throws MyException {

		List<String> result = new ArrayList<String>();
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);

			if (isDigit(ch)) { // 数字
				if (!varRegister.toString().equals("")) {
					if (spaceRegister) {
						varRegister.setLength(0);
						digitRegister.setLength(0);
						spaceRegister = false;
						throw new MyException(ErrMsg.LETTER_SPACE_NUMBER, line, i + 1);
					} else {
						varRegister.append(ch);
						continue;
					}
				}
				if (!digitRegister.toString().equals("")) {
					if (spaceRegister) {
						varRegister.setLength(0);
						digitRegister.setLength(0);
						spaceRegister = false;
						throw new MyException(ErrMsg.NUMBER_SPACE_NUMBER, line, i + 1);
					} else {
						digitRegister.append(ch);
						continue;
					}
				}
				digitRegister.append(ch);
				continue;
			}
			
			else if (isAlpha(ch)) { // 字母
				if (!varRegister.toString().equals("")) { // 变量名寄存器不为空
					if (spaceRegister) {
						varRegister.setLength(0);
						digitRegister.setLength(0);
						spaceRegister = false;
						throw new MyException(ErrMsg.LETTER_SPACE_LETTER, line, i + 1);
					} else {
						varRegister.append(ch);
						continue;
					}
				}
				if (!digitRegister.toString().equals("")) { // 数字寄存器不为空
					if (spaceRegister) {
						varRegister.setLength(0);
						digitRegister.setLength(0);
						spaceRegister = false;
						throw new MyException(ErrMsg.NUMBER_SPACE_LETTER, line, i + 1);
					} else {
						varRegister.setLength(0);
						digitRegister.setLength(0);
						throw new MyException(ErrMsg.LETTER_AFTER_NUMBER, line, i + 1);
					}
				}
				varRegister.append(ch);
				continue;
			}

			else if (isPoint(ch)) { // 小数点
				if (spaceRegister) {
					varRegister.setLength(0);
					digitRegister.setLength(0);
					spaceRegister = false;
					throw new MyException(ErrMsg.POINT_AFTER_SPACE, line, i + 1);
				} else {
					if (!varRegister.toString().equals("")) {
						varRegister.setLength(0);
						digitRegister.setLength(0);
						throw new MyException(ErrMsg.POINT_AFTER_LETTER, line, i + 1);
					}
					if (!digitRegister.toString().equals("")) {
						if (digitRegister.toString().contains(String.valueOf(Operator.POINT))) {
							varRegister.setLength(0);
							digitRegister.setLength(0);
							throw new MyException(ErrMsg.POINT_AFTER_POINT, line, i + 1);
						}
						digitRegister.append(ch);
						continue;
					}
				}
			}

			else if (isOperator(ch)) { // 操作符
				spaceRegister = false;
				if (!varRegister.toString().equals("")) {
					result.add(varRegister.toString());
					varRegister.setLength(0);
				}
				if (!digitRegister.toString().equals("")) {
					if (digitRegister.toString().endsWith(String.valueOf(Operator.POINT))) {
						varRegister.setLength(0);
						digitRegister.setLength(0);
						throw new MyException(ErrMsg.NUMBER_END_POINT, line, i + 1);
					}
					result.add(digitRegister.toString());
					digitRegister.setLength(0);
				}
				result.add(String.valueOf(ch));
				continue;
			}
			
			else if (isSpace(ch)) { // 空白符
				spaceRegister = true;
				continue;
			}

			varRegister.setLength(0);
			digitRegister.setLength(0);
			throw new MyException(ErrMsg.INPUT_UNKNOWN_CAHR, line, i + 1);
		}
		// 变量寄存器中存有变量
		if (!varRegister.toString().equals("")) {
			result.add(varRegister.toString());
			varRegister.setLength(0);
		}
		// 数字寄存器中存有数字
		if (!digitRegister.toString().equals("")) {
			if (digitRegister.toString().endsWith(String.valueOf(Operator.POINT))) {
				varRegister.setLength(0);
				digitRegister.setLength(0);
				throw new MyException(ErrMsg.NUMBER_END_POINT, line, str.length() - 1);
			}
			result.add(digitRegister.toString());
			digitRegister.setLength(0);
		}

		return result;
	}

	// 中缀表达式转后缀表达式
	private static List<String> fixChange(List<String> tokenList, int line) throws MyException {
		Stack<String> opStack = new Stack<String>(); // 运算符号栈
		List<String> result = new ArrayList<String>(); // 存储得到的后缀表达式
		int strIndex = 0;

		while (strIndex < tokenList.size()) {
			String str = tokenList.get(strIndex);
			if (str.matches("[\\d]+") || str.matches("[\\d]+\\.[\\d]+")) { // 整数和浮点数加入后缀表达式
				result.add(str);
			} else if (str.matches("[a-zA-Z]+[a-zA-Z0-9]*")) {
				if (SentenceAnalyser.isKeyWord(str)) { // 判断是否保留字
					int position = 0;
					for (int j = 0; j < strIndex; j++) {
						position += tokenList.get(j).length();
					}
					throw new MyException(ErrMsg.NAME_WITH_KEYWORD, line, position + 2);
				}
				else if (Variables.varsMap.containsKey(str)) { // varsMap有该变量则入栈，否则报错
					String value = Variables.varsMap.get(str);
					result.add(value);
				} else {
					int position = 0;
					for (int j = 0; j < strIndex; j++) {
						position += tokenList.get(j).length();
					}
					throw new MyException(ErrMsg.UNDEFINED_IDENTIFIER, line, position + 2);
				}
			}

			else if (str.equals("+") || str.equals("-")) {
				if (opStack.empty()) {
					opStack.push(str);
				} else {
					boolean flag = true;
					while (flag && !opStack.empty()) {
						String top = opStack.peek();
						if (top.equals("+") || top.equals("-") || top.equals("*") || top.equals("/")
								|| top.equals("^")) {
							result.add(opStack.pop());
						} else {
							flag = false;
						}
					}
					opStack.push(str);
				}
			}

			else if (str.equals("*") || str.equals("/")) {
				if (opStack.empty()) {
					opStack.push(str);
				} else {
					boolean flag = true;
					while (flag && !opStack.empty()) {
						String top = opStack.peek();
						if (top.equals("*") || top.equals("/") || top.equals("^")) {
							result.add(opStack.pop());
						} else {
							flag = false;
						}
					}
					opStack.push(str);
				}
			}

			else if (str.equals("^")) {
				if (opStack.empty()) {
					opStack.push(str);
				} else {
					boolean flag = true;
					while (flag && !opStack.empty()) {
						String top = opStack.peek();
						if (top.equals("^")) {
							result.add(opStack.pop());
						} else {
							flag = false;
						}
					}
					opStack.push(str);
				}
			}

			else if (str.equals("(")) {
				opStack.push(str);
			}

			else if (str.equals(")")) {
				boolean flag = true;
				while (flag && !opStack.empty()) {
					if (opStack.peek().equals("(")) { // 遇到左括号停止弹出栈顶元素，左括号不输出
						opStack.pop();
						flag = false;
					} else { // 非左括号则弹出栈顶元素并将其加入后缀表达式
						result.add(opStack.pop());
					}
				}
			}

			else {
				int position = 0;
				for (int j = 0; j < strIndex; j++) {
					position += tokenList.get(j).length();
				}
				throw new MyException(ErrMsg.CANNOT_FIND_TYPE, line, position + 2);
			}

			strIndex++;
		}

		while (!opStack.empty()) { // 运算符号栈中剩余运算符全部加入后缀表达式
			result.add(opStack.pop());
		}
		return result;
	}

	// 后缀表达式运算
	private static String postEvaluation(List<String> tokenList, int line) throws MyException, EmptyStackException {
		Stack<String> stack = new Stack<String>();
		for (int i = 0; i < tokenList.size(); i++) {
			String str = tokenList.get(i);
			if (str.matches("[\\d]+") || str.matches("[\\d]+\\.[\\d]+")) {
				stack.push(str);
			} else {
				double num1 = Double.parseDouble(stack.pop());
				double num2 = Double.parseDouble(stack.pop());
				switch (str) {
				case "+":
					stack.push(String.valueOf(num2 + num1));
					break;
				case "-":
					stack.push(String.valueOf(num2 - num1));
					break;
				case "*":
					stack.push(String.valueOf(num2 * num1));
					break;
				case "/":
					if (num1 == 0) {
						throw new MyException(ErrMsg.DIVISOR_BE_ZERO, line);
					} else {
						stack.push(String.valueOf(num2 / num1));
					}
					break;
				case "^":
					stack.push(String.valueOf(Math.pow(num2, num1)));
					break;
				default:
					break;
				}
			}
		}
		if (stack.size() > 1) {
			throw new MyException(ErrMsg.WRONG_FORMAT_OF_EXPRESSION, line);
		}

		double evaResult = Double.parseDouble(stack.pop());
		DecimalFormat decimalFormat = new DecimalFormat("0.00"); // 如果小数不足2位,会以0补足
		return decimalFormat.format(evaResult);

	}

	public static String evaluate(List<String> tokenList, int line) throws MyException, EmptyStackException {
		List<String> result = fixChange(tokenList, line);
		return postEvaluation(result, line);
	}

}
