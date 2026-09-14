package mx.egd.fmre.register.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.component.IdBadgeComponent;
import mx.egd.fmre.register.service.IdBadgeService;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdBadgeServiceImpl implements IdBadgeService {
    
    private final IdBadgeComponent idBadgeComponent;

    // 5 cm are 189 pixels
    private static final BigDecimal EQUIV_PIXELS = BigDecimal.valueOf(189);
    private static final BigDecimal EQUIV_CM = BigDecimal.valueOf(5);
    private static final BigDecimal SIZE_CRED_WIDTH_CM = BigDecimal.valueOf(5.40);
    private static final BigDecimal SIZE_CRED_HEIGHT_CM = BigDecimal.valueOf(8.56);

    @Override
    public byte[] createIdBadgeService(Integer idPersona) {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // String dest = "hello_java21.pdf";

        // Initialize PDF writer
        PdfWriter writer = new PdfWriter(baos);

        // Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);
        
        float width = SIZE_CRED_WIDTH_CM.multiply(EQUIV_PIXELS.divide(EQUIV_CM)).floatValue();
        float height = SIZE_CRED_HEIGHT_CM.multiply(EQUIV_PIXELS.divide(EQUIV_CM)).floatValue();

        Rectangle customRectangle = new Rectangle(width, height);
        PageSize customPageSize = new PageSize(customRectangle);

        /*PdfPage page = */pdf.addNewPage(customPageSize);

        try (Document document = new Document(pdf)) {
            
            putBaseImage(document);
            
            miembreActivoParagrph(document);
            
            idBadgeComponent.addPhoto(document, idPersona);
            
            addGreka(document);
            
            idBadgeComponent.addNombreAfiliado(document, idPersona);
            
            idBadgeComponent.addViegencia(document, idPersona);
            
            idBadgeComponent.addIndicativo(document);
        }

        return baos.toByteArray();
    }
    
    private void putBaseImage(Document document) {
        try (InputStream is = getClass().getResourceAsStream("/cred/1PL2.png")) {
            byte[] imageBytes = is.readAllBytes();

            ImageData data = ImageDataFactory.create(imageBytes);

            Image image = new Image(data);
            image.setFixedPosition(0f, 0f);
            image.scale(0.32f, 0.32f);
            image.setAutoScaleHeight(false);
            image.setAutoScaleWidth(false);
            document.add(image);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void addGreka(Document document) {
        try (InputStream is = getClass().getResourceAsStream("/cred/2_anverso_marcaAgua.png")) {
            byte[] imageBytes = is.readAllBytes();

            ImageData data = ImageDataFactory.create(imageBytes);

            Image image = new Image(data);
            image.setFixedPosition(0f, 0f);
            image.scale(1f, 1f);
            image.setAutoScaleHeight(false);
            image.setAutoScaleWidth(false);
            document.add(image);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void miembreActivoParagrph(Document document) {
        PdfFont regularFont;
        try {
            File file = ResourceUtils.getFile("classpath:fonts/Arial Bold/Arial Bold.ttf");
            regularFont = PdfFontFactory.createFont(file.getAbsolutePath(), PdfEncodings.IDENTITY_H);

            Paragraph p = new Paragraph("Miembro Activo")
                    .setFont(regularFont)
                    .setFontSize(12)
                    .setFixedPosition(75, 235, 100);
            p.setTextAlignment(TextAlignment.CENTER);
            document.add(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
