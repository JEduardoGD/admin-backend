package mx.egd.fmre.register.component;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;

public interface IdBadgeComponent {

    void addPhoto(Document document, Integer idPersona);

    void addNombreAfiliado(Document document, Integer idPersona);

    void addVigencia(Document document, Integer idPersona);

    void addIndicativo(Document document);

    void addTipoAficionado(Document document);

    void addApoyoText(Document document);

    void addAfiliacionFmreParagraph(Document document);

    void addEscudos(Document document);

    void addQrcode(PdfDocument pdf, Document document);

    void addSociedadIaru(Document document);

    void addIndivativoBack(Document document, Integer idPersona);


}
