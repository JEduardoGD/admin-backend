package mx.egd.fmre.register.component.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.dto.ImagenDto;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.record.TipoImagen;
import mx.egd.fmre.register.service.AfiliacionService;
import mx.egd.fmre.register.service.ImagenService;
import mx.egd.fmre.register.service.PersonaService;
import mx.egd.fmre.register.service.TipoImagenService;
import mx.egd.fmre.register.service.exceptions.AfiliacionServiceException;
import mx.egd.fmre.register.service.exceptions.ServiceException;

@Component
@RequiredArgsConstructor
@Slf4j
public class IdBadgeComponentImpl implements IdBadgeComponent {
    
    private final ImagenService imagenService;
    private final TipoImagenService tipoImagenService;
    private final PersonaService personaService;
    private final AfiliacionService afiliacionService;
    
    private TipoImagen tipoImagenDocumentoFotoPersonal;
    
    private static final BigDecimal FOTO_HEIGH_PX = BigDecimal.valueOf(95.0);
    
    @PostConstruct
    private void init() {
        tipoImagenDocumentoFotoPersonal = tipoImagenService.findAllActive().stream()
                .filter(ti -> ti.tipo().equals("PERSONAL FOTO"))
                .findAny()
                .orElse(null);
    }
    
    @Override
    public void addPhoto(Document document, Integer idPersona) {
        List<ImagenDto> imagensList = imagenService.findByIdPersona(idPersona);
        ImagenDto imagenFotoPersonal = imagensList.stream()
                .filter(i -> i.getIdTipoImagenDocumento().equals(tipoImagenDocumentoFotoPersonal.idTipoImagen()))
                .findFirst()
                .orElse(null);
        byte[] imagenFotoPersonalByteArray = null;
        try {
            imagenFotoPersonalByteArray = imagenService.get(imagenFotoPersonal.getUuid());
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        ImageData data = ImageDataFactory.create(imagenFotoPersonalByteArray);
        
        BigDecimal scaleBD = FOTO_HEIGH_PX.divide(BigDecimal.valueOf(data.getHeight()), 6, RoundingMode.HALF_UP);
        
        BigDecimal tempPosX = BigDecimal.valueOf(data.getWidth()).multiply(scaleBD);
        tempPosX = tempPosX.divide(BigDecimal.valueOf(2), 6, RoundingMode.HALF_UP);
        tempPosX = BigDecimal.valueOf(127f).subtract(tempPosX);
        
        //String width = ().divide(BigDecimal.TWO).plus(BigDecimal.valueOf(75));

        Image image = new Image(data);
        image.setFixedPosition(tempPosX.floatValue(), 130f);
        image.scale(scaleBD.floatValue(), scaleBD.floatValue());
        image.setAutoScaleHeight(false);
        image.setAutoScaleWidth(false);
        document.add(image);
    }
    
    @Override
    public void addNombreAfiliado(Document document, Integer idPersona) {
        Persona persona = personaService.findByIdPersona(idPersona);
        String nombreCompleto = "";
        if(persona.getNombre() != null) {
            nombreCompleto += persona.getNombre() + " ";
        }
        if(persona.getPrimerApellido() != null) {
            nombreCompleto += persona.getPrimerApellido() + " ";
        }
        if(persona.getSegundoApellido() != null) {
            nombreCompleto += persona.getSegundoApellido() + " ";
        }
        nombreCompleto = nombreCompleto.trim();
        PdfFont regularFont;
        try {
            File file = ResourceUtils.getFile("classpath:fonts/Arial Bold/Arial Bold.ttf");
            regularFont = PdfFontFactory.createFont(file.getAbsolutePath(), PdfEncodings.IDENTITY_H);

            Paragraph p = new Paragraph(nombreCompleto)
                    .setFont(regularFont)
                    .setFontSize(12)
                    .setFixedPosition(65, 75, 120);
            p.setTextAlignment(TextAlignment.CENTER);
            document.add(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void addVigencia(Document document, Integer idPersona) {
        Persona persona = personaService.findByIdPersona(idPersona);
        Afiliacion afiliacion = null;
        try {
            afiliacion = afiliacionService.findActiveByPersona(persona);
        } catch (AfiliacionServiceException e) {
            // TODO Auto-generated catch block 6, RoundingMode.HALF_UP);
            e.printStackTrace();
        }
        if(afiliacion == null) {
            log.error("Error: no se localizaron afiliaciones");
            return;
        }
        PdfFont regularFont;
        try {
            Locale mexicanSpanish = Locale.of("es", "MX");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", mexicanSpanish);

            File file = ResourceUtils.getFile("classpath:fonts/Arial Bold/Arial Bold.ttf");
            regularFont = PdfFontFactory.createFont(file.getAbsolutePath(), PdfEncodings.IDENTITY_H);
            String s = String.format("Vigencia:\n%s\n%s", dateFormat.format(afiliacion.getFechaInicio()), dateFormat.format(afiliacion.getFechaFin()));
            Paragraph p = new Paragraph(s)
                    .setFont(regularFont)
                    .setFontSize(12)
                    .setFontColor(ColorConstants.RED)
                    .setFixedPosition(50, 10, 150);
            p.setTextAlignment(TextAlignment.CENTER);
            document.add(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void addIndicativo(Document document) {
        String indicativo = "XE-SWL-36-05";
        double rotationAngle = Math.PI / 2;
        int fontSize = 27;
        int posX = 67;
        int posY = 20;
        int width = 350;
        
        PdfFont regularFont;
        PdfFont outlineFont;
        try {
            File regularFontFile = ResourceUtils.getFile("classpath:fonts/avita/Avita-Black.otf");
            regularFont = PdfFontFactory.createFont(regularFontFile.getAbsolutePath(), PdfEncodings.IDENTITY_H);

            Paragraph pBack = new Paragraph(indicativo)
                    .setFont(regularFont)
                    .setFontSize(fontSize)
                    .setFontColor(ColorConstants.WHITE)
                    .setFixedPosition(posX, posY, width)
                    .setRotationAngle(rotationAngle);
            pBack.setTextAlignment(TextAlignment.LEFT);
            document.add(pBack);
            

            File outlineFontFile = ResourceUtils.getFile("classpath:fonts/avita/Avita-OutlineBlack.otf");
            outlineFont = PdfFontFactory.createFont(outlineFontFile.getAbsolutePath(), PdfEncodings.IDENTITY_H);
            Paragraph pOutline = new Paragraph(indicativo)
                    .setFont(outlineFont)
                    .setFontSize(fontSize)
                    .setFontColor(ColorConstants.BLACK)
                    .setFixedPosition(posX, posY, width)
                    .setRotationAngle(rotationAngle);
            pOutline.setTextAlignment(TextAlignment.LEFT);
            document.add(pOutline);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void addTipoAficionado(Document document) {
        String indicativo = "Radioescucha SWL";
        double rotationAngle = Math.PI / 2;
        int fontSize = 22;
        int posX = 40;
        int posY = 20;
        int width = 350;
        
        PdfFont regularFont;
        try {
            File regularFontFile = ResourceUtils.getFile("classpath:fonts/avita/Avita-Black.otf");
            regularFont = PdfFontFactory.createFont(regularFontFile.getAbsolutePath(), PdfEncodings.IDENTITY_H);

            Paragraph pBack = new Paragraph(indicativo)
                    .setFont(regularFont)
                    .setFontSize(fontSize)
                    .setFontColor(ColorConstants.WHITE)
                    .setFixedPosition(posX, posY, width)
                    .setRotationAngle(rotationAngle);
            pBack.setTextAlignment(TextAlignment.LEFT);
            document.add(pBack);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void addApoyoText(Document document) {
        String textApoyo = "Se solicita a las autoridades CIVILES y MILITARES todo el apoyo que puedan brindar para  el óptimo desempeño de sus funciones";
        PdfFont regularFont = null;
        File file;
        try {
            file = ResourceUtils.getFile("classpath:fonts/Arial Rounded MT Regular/Arial Rounded MT Regular.ttf");
            regularFont = PdfFontFactory.createFont(file.getAbsolutePath(), PdfEncodings.IDENTITY_H);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Paragraph p = new Paragraph(textApoyo)
                .setFont(regularFont)
                .setFontSize(10)
                .setFontColor(ColorConstants.RED)
                .setFixedLeading(17)
                .setMultipliedLeading(1f)
                .setFixedPosition(23, 240, 155);
        p.setTextAlignment(TextAlignment.CENTER);
        document.add(p);
    }
    
    @Override
    public void addAfiliacionFmreParagraph(Document document) {
        PdfFont regularFont;
        try {
            File file = ResourceUtils.getFile("classpath:fonts/Arial Bold/Arial Bold.ttf");
            regularFont = PdfFontFactory.createFont(file.getAbsolutePath(), PdfEncodings.IDENTITY_H);

            Paragraph p = new Paragraph("Afiliación FMRE.")
                    .setFont(regularFont)
                    .setFontSize(12)
                    .setFixedPosition(55, 210, 100);
            p.setTextAlignment(TextAlignment.CENTER);
            document.add(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void addEscudos(Document document) {
        try (InputStream is = getClass().getResourceAsStream("/cred/FMRE.png")) {
            byte[] imageBytes = is.readAllBytes();

            ImageData data = ImageDataFactory.create(imageBytes);
            

            Image image = new Image(data);
            image.setFixedPosition(15f, 125f);
            image.scale(0.06f, 0.06f);
            image.setAutoScaleHeight(false);
            image.setAutoScaleWidth(false);
            document.add(image);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        try (InputStream is = getClass().getResourceAsStream("/cred/IARU.png")) {
            byte[] imageBytes = is.readAllBytes();

            ImageData data = ImageDataFactory.create(imageBytes);
            

            Image image = new Image(data);
            image.setFixedPosition(140f, 120f);
            image.scale(0.35f, 0.35f);
            image.setAutoScaleHeight(false);
            image.setAutoScaleWidth(false);
            document.add(image);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void addQrcode(PdfDocument pdf, Document document) {
     // 1. Create the QR code data instance
        String myText = "https://itextpdf.com";
        BarcodeQRCode qrCode = new BarcodeQRCode(myText);

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
    public void addSociedadIaru(Document document) {
        String textApoyo = "Sociedad miembro de IARU Internacional Amateur Radio Union";
        PdfFont regularFont = null;
        File file;
        try {
            file = ResourceUtils.getFile("classpath:fonts/Arial Rounded MT Regular/Arial Rounded MT Regular.ttf");
            regularFont = PdfFontFactory.createFont(file.getAbsolutePath(), PdfEncodings.IDENTITY_H);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Paragraph p = new Paragraph(textApoyo)
                .setFont(regularFont)
                .setFontSize(10)
                //.setFontColor(ColorConstants.RED)
                .setFixedLeading(17)
                .setMultipliedLeading(1f)
                .setFixedPosition(23, 30, 165);
        p.setTextAlignment(TextAlignment.CENTER);
        document.add(p);
    }
    
    @Override
    public void addIndivativoBack(Document document, Integer idPersona) {
        Persona persona = personaService.findByIdPersona(idPersona);
        Afiliacion afiliacion = null;
        try {
            afiliacion = afiliacionService.findActiveByPersona(persona);
        } catch (AfiliacionServiceException e) {
            // TODO Auto-generated catch block 6, RoundingMode.HALF_UP);
            e.printStackTrace();
        }
        if(afiliacion == null) {
            log.error("Error: no se localizaron afiliaciones");
            return;
        }
        PdfFont regularFont;
        try {
            File file = ResourceUtils.getFile("classpath:fonts/Arial Bold/Arial Bold.ttf");
            regularFont = PdfFontFactory.createFont(file.getAbsolutePath(), PdfEncodings.IDENTITY_H);
            String s = String.format("XE-SWL-36-05");
            Paragraph p = new Paragraph(s)
                    .setFont(regularFont)
                    .setFontSize(18)
                    .setFontColor(ColorConstants.RED)
                    .setFixedPosition(30, 70, 150);
            p.setTextAlignment(TextAlignment.CENTER);
            document.add(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}





































