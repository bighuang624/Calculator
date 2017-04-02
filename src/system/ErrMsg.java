package system;

public class ErrMsg {

	public static final String CANNOT_FIND_TYPE = "The operator cannot be recognized."; // 计算表达式时找不到匹配该变量的符号类型
	public static final String DIVISOR_BE_ZERO = "The divisor be zero."; // 除数为0
	public static final String END_WITHOUT_SEM = "Program ended without a semicolon."; // 结尾没有结束符
	public static final String INPUT_UNKNOWN_CAHR = "Character cannot be recognized by the system."; // 输入中包含未知字符
	public static final String LETTER_SPACE_LETTER = "Space between variables."; // 字母间有空白符
	public static final String LETTER_SPACE_NUMBER = "Space between numbers and variables."; // 字母和数字间有空白符
	public static final String LETTER_AFTER_NUMBER = "Letter after a number."; // 数字之后紧跟字母，可能是变量名不符合规定
	public static final String NAME_WITH_KEYWORD = "Varible named by keyword."; // 使用保留字进行命名
	public static final String NUMBER_END_POINT = "Number ended with a point."; // 一个数字以小数点结尾
	public static final String NUMBER_SPACE_NUMBER = "Space between the two numbers."; // 数字和数字间有空白符
	public static final String NUMBER_SPACE_LETTER = "Space between numbers and variables."; // 数字和字母间有空白符
	public static final String POINT_AFTER_LETTER = "Letter before a point."; // 小数点前面有字母
	public static final String POINT_AFTER_POINT = "Illegal points in a number."; // 一个数字中有多个小数点
	public static final String POINT_AFTER_SPACE = "Space before a point."; // 小数点前面有空格
	public static final String UNDEFINED_IDENTIFIER = "Undefined identifier."; // 使用未初始化的变量进行运算
	public static final String WRONG_FORMAT_OF_EXPRESSION = "Error in expression."; // 表达式有误
	public static final String WRONG_ASSIGNMENT = "Error in assignment statement."; // 赋值语句有误
	public static final String WRONG_FILE_PATH = "The file path cannot be recognized."; // 文件路径有误

}
