import analyser.LexicalAnalyser;
import analyser.SentenceAnalyser;
import system.Read;

public class Parser {

	public static void parse(String filePath) throws Exception {
		String[] input = Read.readInput(filePath);
		for (int i = 0; i < input.length; i++) {
			SentenceAnalyser.sentenceFun(LexicalAnalyser.lexAnalysis(input[i], i + 1), i + 1);
		}
	}

}
