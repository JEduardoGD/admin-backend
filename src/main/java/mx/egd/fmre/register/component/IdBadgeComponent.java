package mx.egd.fmre.register.component;

import java.math.BigDecimal;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;

import mx.egd.fmre.register.component.exception.IdBadgeComponentException;
import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.dto.Persona;

public interface IdBadgeComponent {

    void putBaseImage(Document document) throws IdBadgeComponentException;

    void setMiembreActivoParagrph(Document document) throws IdBadgeComponentException;

    void addGreka(Document document);

    void addPhoto(Document document, ImageData data);

    void addNombreAfiliado(Document document, Persona persona) throws IdBadgeComponentException;

    void addVigencia(Document document, Afiliacion afiliacion) throws IdBadgeComponentException;

    void addIndicativo(Document document, String indicativo) throws IdBadgeComponentException;

    void addTipoAfiliacion(Document document, String tipoAfiliacion) throws IdBadgeComponentException;

    void addApoyoText(Document document) throws IdBadgeComponentException;

    void addAfiliacionFmreParagraph(Document document) throws IdBadgeComponentException;

    void addGrekaRev(Document document, BigDecimal sizeCredWithCm, BigDecimal equivPixels, BigDecimal equivCm);

    void addEscudos(Document document);

    void addQrcode(PdfDocument pdf, Document document, String url);

    void addSociedadIaru(Document document) throws IdBadgeComponentException;

    void addIndivativoBack(Document document, String indicativo) throws IdBadgeComponentException;
}
