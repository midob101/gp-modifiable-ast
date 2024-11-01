package language_definitions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PredefinedLanguagesMap {
    public BufferedReader getReader(PredefinedLanguages languages) {
        String name = null;
        switch (languages) {
            case MINIJAVA -> name = "/languages/minijava.txt";
        }
        InputStream in = getClass().getResourceAsStream(name);
        return new BufferedReader(new InputStreamReader(in));
    }
}