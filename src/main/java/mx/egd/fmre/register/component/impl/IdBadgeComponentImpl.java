package mx.egd.fmre.register.component.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.component.IdBadgeComponent;
import mx.egd.fmre.register.component.exception.ComponentException;
import mx.egd.fmre.register.component.exception.IdBadgeComponentException;
import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.util.IdBadgeComponentStaticValues;

@Component
@RequiredArgsConstructor
@Slf4j
public class IdBadgeComponentImpl extends IdBadgeComponentStaticValues implements IdBadgeComponent {

    private ImageData escudoTranslucidoImageData;
    private ImageData fmreImageData;
    private ImageData iaruImageData;
    private ImageData plecaData;
    private ImageData anversoMarcaAguaImageData;

    DateTimeFormatter formatter;
    
    @PostConstruct
    private void init() throws ComponentException {
        formatter = DateTimeFormatter
                .ofPattern("dd 'de' MMMM 'de' yyyy")
                .withLocale(SPANISH_MEXICO_LOCALE);
        
        escudoTranslucidoImageData = getImageDataFromResources(ESCUDO_FMRE_TRANSLUCIDO_IMAGE_PATH);
        this.fmreImageData = getImageDataFromResources(FMRE_IMAGE_PATH);
        this.iaruImageData = getImageDataFromResources(IARU_IMAGE_PATH);
        this.plecaData = getImageDataFromResources(PLECA_IMAGE_PATH);
        this.anversoMarcaAguaImageData = getImageDataFromResources(ANVERSO_MARCA_AGUA_IMAGE_PATH);
    }
    
    private PdfFont loadFont(String path) throws IdBadgeComponentException {
        try {
            File file = ResourceUtils.getFile(String.format("classpath:%s", path));
            return PdfFontFactory.createFont(file.getAbsolutePath(), PdfEncodings.IDENTITY_H);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IdBadgeComponentException(e);
        }
    }
    
    private ImageData getImageDataFromResources(String path) throws IdBadgeComponentException {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            byte[] imageBytes = is.readAllBytes();
            return ImageDataFactory.create(imageBytes);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IdBadgeComponentException(e);
        }
    }
    
    @Override
    public void addBaseImagen(Document document) {
        Image image = new Image(escudoTranslucidoImageData);
        image.setFixedPosition(0f, 0f);
        image.scale(0.15f, 0.15f);
        image.setAutoScaleHeight(false);
        image.setAutoScaleWidth(false);
        document.add(image);
    }

    @Override
    public void addPleca(Document document) throws IdBadgeComponentException {
            Image image = new Image(plecaData);
            image.setFixedPosition(0f, 0f);
            image.scale(0.32f, 0.32f);
            image.setAutoScaleHeight(false);
            image.setAutoScaleWidth(false);
            document.add(image);
    }
    
    @Override
    public void setMiembreActivoParagrph(Document document) throws IdBadgeComponentException {
        PdfFont pdfFont = loadFont(ARIAL_BOLD_FONT_FILE);
        Paragraph p = new Paragraph(MIEMBRO_ACTIVO)
                .setFont(pdfFont)
                .setFontSize(12)
                .setFixedPosition(75, 235, 100);
        p.setTextAlignment(TextAlignment.CENTER);
        document.add(p);
    }
    
    @Override
    public void addGreka(Document document) {
        Image image = new Image(anversoMarcaAguaImageData);
        image.setFixedPosition(0f, 0f);
        image.scale(1f, 1f);
        image.setAutoScaleHeight(false);
        image.setAutoScaleWidth(false);
        document.add(image);

    }
    
    @Override
    public void addPhoto(Document document, ImageData data) {
        BigDecimal scaleBD = FOTO_HEIGH_PX.divide(BigDecimal.valueOf(data.getHeight()), 6, RoundingMode.HALF_UP);
        
        BigDecimal tempPosX = BigDecimal.valueOf(data.getWidth()).multiply(scaleBD);
        tempPosX = tempPosX.divide(BigDecimal.valueOf(2), 6, RoundingMode.HALF_UP);
        tempPosX = BigDecimal.valueOf(127f).subtract(tempPosX);
        
        Image image = new Image(data);
        image.setFixedPosition(tempPosX.floatValue(), 130f);
        image.scale(scaleBD.floatValue(), scaleBD.floatValue());
        image.setAutoScaleHeight(false);
        image.setAutoScaleWidth(false);
        document.add(image);
    }
    
    @Override
    public void addNombreAfiliado(Document document, Persona persona) throws IdBadgeComponentException {
        String nombreCompleto = "";
        if (persona.getNombre() != null) {
            nombreCompleto += persona.getNombre() + TEXTO_SPACE;
        }
        if (persona.getPrimerApellido() != null) {
            nombreCompleto += persona.getPrimerApellido() + TEXTO_SPACE;
        }
        if (persona.getSegundoApellido() != null) {
            nombreCompleto += persona.getSegundoApellido() + TEXTO_SPACE;
        }
        nombreCompleto = nombreCompleto.trim();

        PdfFont pdfFont = loadFont(ARIAL_BOLD_FONT_FILE);
        Paragraph p = new Paragraph(nombreCompleto)
                .setFont(pdfFont)
                .setFontSize(12)
                .setFixedPosition(65, 75, 120);
        p.setTextAlignment(TextAlignment.CENTER);
        document.add(p);
    }
    
    @Override
    public void addVigencia(Document document, Afiliacion afiliacion) throws IdBadgeComponentException {
        String vigencia;
        if(afiliacion.isVitalicia()) {
            vigencia = "Vigencia:\nvitalicia";
        } else {
            vigencia = String.format(VIGENCIA_FORMAT, afiliacion.getFechaInicio().format(formatter),
                    afiliacion.getFechaFin().format(formatter));
        }
        PdfFont pdfFont = loadFont(ARIAL_BOLD_FONT_FILE);
        Paragraph p = new Paragraph(vigencia)
                .setFont(pdfFont)
                .setFontSize(12)
                .setFontColor(ColorConstants.RED)
                .setFixedPosition(50, 10, 150);
        p.setTextAlignment(TextAlignment.CENTER);
        document.add(p);
    }
    
    @Override
    public void addIndicativo(Document document, String indicativo) throws IdBadgeComponentException {
        double rotationAngle = Math.PI / 2;
        int fontSize = 27;
        int posX = 67;
        int posY = 20;
        int width = 350;
        
        PdfFont pdfFont = loadFont(AVITIA_BLACK_FONT_FILE);
        Paragraph pBack = new Paragraph(indicativo)
                .setFont(pdfFont)
                .setFontSize(fontSize)
                .setFontColor(ColorConstants.WHITE)
                .setFixedPosition(posX, posY, width)
                .setRotationAngle(rotationAngle);
        pBack.setTextAlignment(TextAlignment.LEFT);
        document.add(pBack);
        
        PdfFont pdfFontB = loadFont(AVITIA_OUTLINE_BLACK_FONT_FILE);
        Paragraph pOutline = new Paragraph(indicativo)
                .setFont(pdfFontB)
                .setFontSize(fontSize)
                .setFontColor(ColorConstants.BLACK)
                .setFixedPosition(posX, posY, width)
                .setRotationAngle(rotationAngle);
        pOutline.setTextAlignment(TextAlignment.LEFT);
        document.add(pOutline);
    }
    
    @Override
    public void addTipoAfiliacion(Document document, String tipoAfiliacion) throws IdBadgeComponentException {
        double rotationAngle = Math.PI / 2;
        int fontSize = 22;
        int posX = 40;
        int posY = 20;
        int width = 350;
        
        PdfFont pdfFont = loadFont(AVITIA_BLACK_FONT_FILE);
        Paragraph pBack = new Paragraph(tipoAfiliacion)
                .setFont(pdfFont)
                .setFontSize(fontSize)
                .setFontColor(ColorConstants.WHITE)
                .setFixedPosition(posX, posY, width)
                .setRotationAngle(rotationAngle);
        pBack.setTextAlignment(TextAlignment.LEFT);
        document.add(pBack);
    }

    @Override
    public void addApoyoText(Document document) throws IdBadgeComponentException {
        PdfFont pdfFont = loadFont(ARIAL_ROUNDED_MT_REGULAR_FONT_FILE);
        Paragraph p = new Paragraph(TEXTO_APOYO)
                .setFont(pdfFont)
                .setFontSize(10)
                .setFontColor(ColorConstants.RED)
                .setFixedLeading(17)
                .setMultipliedLeading(1f)
                .setFixedPosition(23, 240, 155);
        p.setTextAlignment(TextAlignment.CENTER);
        document.add(p);
    }
    
    @Override
    public void addAfiliacionFmreParagraph(Document document) throws IdBadgeComponentException {
        PdfFont pdfFont = loadFont(ARIAL_BOLD_FONT_FILE);
        Paragraph p = new Paragraph(TEXTO_AFILIACION_FMRE)
                .setFont(pdfFont)
                .setFontSize(12)
                .setFixedPosition(55, 210, 100);
        p.setTextAlignment(TextAlignment.CENTER);
        document.add(p);
    }
    
    @Override
    public void addGrekaRev(Document document, BigDecimal sizeCredWithCm, BigDecimal equivPixels, BigDecimal equivCm) {
        BigDecimal posY = sizeCredWithCm.multiply(equivPixels.divide(equivCm));
        Image image = new Image(anversoMarcaAguaImageData);
        image.setFixedPosition(posY.floatValue(), 0f);
        image.scale(-1f, 1f);
        image.setAutoScaleHeight(false);
        image.setAutoScaleWidth(false);
        document.add(image);
    }
    
    @Override
    public void addEscudos(Document document) {
        Image fmreImage = new Image(fmreImageData);
        fmreImage.setFixedPosition(15f, 125f);
        fmreImage.scale(0.06f, 0.06f);
        fmreImage.setAutoScaleHeight(false);
        fmreImage.setAutoScaleWidth(false);
        document.add(fmreImage);
        
    
        Image iaruImage = new Image(iaruImageData);
        iaruImage.setFixedPosition(140f, 120f);
        iaruImage.scale(0.35f, 0.35f);
        iaruImage.setAutoScaleHeight(false);
        iaruImage.setAutoScaleWidth(false);
        document.add(iaruImage);
    }
    
    @Override
    public void addQrcode(PdfDocument pdf, Document document, String url) {
        BarcodeQRCode qrCode = new BarcodeQRCode(url);

        // 2. Convert the barcode to a Form XObject
        PdfFormXObject qrCodeObject = qrCode.createFormXObject(pdf);

        // 3. Convert to a Layout Image object and scale it
        Image qrCodeImage = new Image(qrCodeObject);
        qrCodeImage.setHeight(85f);
        qrCodeImage.setWidth(85f);
        qrCodeImage.setFixedPosition(63f, 125f);

        // 4. Add to document
        document.add(qrCodeImage);
    }
    
    @Override
    public void addSociedadIaru(Document document) throws IdBadgeComponentException {
        PdfFont pdfFont = loadFont(ARIAL_ROUNDED_MT_REGULAR_FONT_FILE);
        Paragraph p = new Paragraph(TEXTO_SOCIEDAD_IARU)
                .setFont(pdfFont)
                .setFontSize(10)
                //.setFontColor(ColorConstants.RED)
                .setFixedLeading(17)
                .setMultipliedLeading(1f)
                .setFixedPosition(23, 30, 165);
        p.setTextAlignment(TextAlignment.CENTER);
        document.add(p);
    }
    
    @Override
    public void addIndivativoBack(Document document, String indicativo) throws IdBadgeComponentException {
        PdfFont pdfFont = loadFont(ARIAL_BOLD_FONT_FILE);
        Paragraph p = new Paragraph(indicativo)
                .setFont(pdfFont)
                .setFontSize(18)
                .setFontColor(ColorConstants.RED)
                .setFixedPosition(30, 70, 150);
        p.setTextAlignment(TextAlignment.CENTER);
        document.add(p);
    }
}





































