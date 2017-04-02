import system.ErrMsg;
import system.MyException;

public class Calculator {

	// 判断错误位置

	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			System.out.println("Usage:\n\tjava -jar [Result.jar] [sourceFile]");
		} else if (args.length == 1) {
			Parser.parse(args[0]);
		} else {
			throw new MyException(ErrMsg.WRONG_FILE_PATH);
		}

	}

}
