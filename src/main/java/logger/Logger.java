package logger;

public class Logger {
    public enum LOG_LEVELS {
        DEBUG, WARN, ERROR
    }

    public final static LOG_LEVELS LOG_LEVEL = LOG_LEVELS.ERROR;

    public static void debug(LoggerComponents component, String message) {
        if(LOG_LEVEL == LOG_LEVELS.DEBUG) {
            System.out.println("["+component+"] " + message);
        }
    }

    public static void warn(LoggerComponents component, String message) {
        if(LOG_LEVEL == LOG_LEVELS.DEBUG || LOG_LEVEL == LOG_LEVELS.WARN) {
            System.err.println("["+component+"] " + message);
        }
    }

    public static void err(LoggerComponents component, String message) {
        if(LOG_LEVEL == LOG_LEVELS.DEBUG || LOG_LEVEL == LOG_LEVELS.WARN || LOG_LEVEL == LOG_LEVELS.ERROR) {
            System.err.println("["+component+"] " + message);
        }
    }
}
