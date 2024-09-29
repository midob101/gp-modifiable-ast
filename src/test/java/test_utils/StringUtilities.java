package test_utils;

public class StringUtilities {
    /**
     * Simple function to replace all newlines with CR LF.
     * Avoids issues with comparing outputs with test files where the IDE changed the newline characters.
     * This issue does not occur in the main program, as it uses the original newlines and does not adjust those.
     */
    public static String useCRLF(String in) {
        return in.replaceAll("\\r\\n|\\r|\\n", "\r\n");
    }
}
