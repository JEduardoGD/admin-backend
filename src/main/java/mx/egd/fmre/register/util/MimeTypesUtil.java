package mx.egd.fmre.register.util;

import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.util.exception.FileUtilException;
import mx.egd.fmre.register.util.exception.MimeTypesUtilException;

@Slf4j
public abstract class MimeTypesUtil {
    
    private static final String MIME_TYPES_PROPERTIES_FILE_NAME = "mime-types.properties";
    
    public static String getExtension(String mimeType) throws MimeTypesUtilException {
        Properties props;
        try {
            props = FileUtil.loadFromResources(MIME_TYPES_PROPERTIES_FILE_NAME);
        } catch (FileUtilException e) {
            throw new MimeTypesUtilException(e.getMessage());
        }
        return props.getProperty(mimeType);
    }
}
