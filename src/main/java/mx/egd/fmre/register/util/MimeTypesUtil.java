package mx.egd.fmre.register.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.util.exception.MimeTypesUtilException;

@Slf4j
public abstract class MimeTypesUtil {
    public static String getExtension(String mimeType) throws MimeTypesUtilException {
        Properties props = new Properties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream("mime-types.properties")) {
            props.load(is);
        } catch (IOException e) {
            throw new MimeTypesUtilException(e);
        }
        return props.getProperty(mimeType); // .pdf
    }
}
