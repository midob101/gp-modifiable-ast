package generic;

import java.io.File;

public class FileUtils {

    public static String getExtension(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
