package TheDT.utils;

public class StringHelper {
	public static String highlightedTextYellow(String text) {
		return text.replaceAll("(?<=\\s|^)(#(r|y|b|p))?(?=\\S)", "#y");
	}

	public static String highlightedTextPurple(String text) {
		return text.replaceAll("(?<=\\s|^)(#(r|y|b|p))?(?=\\S)", "#p");
	}
}
