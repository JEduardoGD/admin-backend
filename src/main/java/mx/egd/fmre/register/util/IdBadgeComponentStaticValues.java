package mx.egd.fmre.register.util;

import java.math.BigDecimal;
import java.util.Locale;

public abstract class IdBadgeComponentStaticValues {

    protected static final BigDecimal FOTO_HEIGH_PX = BigDecimal.valueOf(95.0);

    protected static final String ARIAL_BOLD_FONT_FILE = "fonts/Arial Bold/Arial Bold.ttf";
    protected static final String AVITIA_BLACK_FONT_FILE = "fonts/avita/Avita-Black.otf";
    protected static final String AVITIA_OUTLINE_BLACK_FONT_FILE = "fonts/avita/Avita-OutlineBlack.otf";
    protected static final String ARIAL_ROUNDED_MT_REGULAR_FONT_FILE = "fonts/Arial Rounded MT Regular/Arial Rounded MT Regular.ttf";

    protected static final Locale SPANISH_MEXICO_LOCALE = Locale.of("es", "MX");
    //protected static final String MEXICO_SPANISH_DATE_FORMAT = "dd 'de' MMMM 'de' yyyy";
    
    protected static final String VIGENCIA_FORMAT = "Vigencia:\n%s\n%s";
    protected static final String TEXTO_APOYO = "Se solicita a las autoridades CIVILES y MILITARES todo el apoyo que puedan brindar para  el óptimo desempeño de sus funciones";
    protected static final String TEXTO_AFILIACION_FMRE = "Afiliación FMRE.";
    protected static final String TEXTO_SOCIEDAD_IARU = "Sociedad miembro de IARU Internacional Amateur Radio Union";
    protected static final String TEXTO_SPACE = " ";
    protected static final String MIEMBRO_ACTIVO = "Miembro Activo";

    protected static final String FMRE_IMAGE_PATH = "/cred/FMRE.png";
    protected static final String IARU_IMAGE_PATH = "/cred/IARU.png";
    protected static final String PLECA_IMAGE_PATH = "/cred/1PL2.png";
    protected static final String ANVERSO_MARCA_AGUA_IMAGE_PATH = "/cred/2_anverso_marcaAgua.png";
    protected static final String ESCUDO_FMRE_TRANSLUCIDO_IMAGE_PATH = "/cred/Gemini_Generated_Image_nukpkvnukpkvnukp.jpeg";
}
