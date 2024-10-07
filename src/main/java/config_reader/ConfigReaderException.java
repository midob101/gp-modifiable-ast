package config_reader;

public class ConfigReaderException extends Exception {
    public ConfigReaderException(String s) {
        super(s);
    }

    public ConfigReaderException(Exception e) {
        super(e);
    }
}
