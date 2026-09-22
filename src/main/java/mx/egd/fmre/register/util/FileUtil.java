package mx.egd.fmre.register.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import mx.egd.fmre.register.util.exception.FileUtilException;

public abstract class FileUtil {
    public static Properties loadFromResources(String fileName) throws FileUtilException {
        Properties props = new Properties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(fileName)) {

        } catch (IOException e) {
            throw new FileUtilException(e);
        }
        return props;
    }
}
