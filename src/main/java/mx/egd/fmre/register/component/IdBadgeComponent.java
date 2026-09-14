package mx.egd.fmre.register.component;

import com.itextpdf.layout.Document;

public interface IdBadgeComponent {

    void addPhoto(Document document, Integer idPersona);

    void addNombreAfiliado(Document document, Integer idPersona);

    void addViegencia(Document document, Integer idPersona);

    void addIndicativo(Document document);

}
