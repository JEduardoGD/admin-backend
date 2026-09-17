package mx.egd.fmre.register.service.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.properties.AreaBreakType;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.component.IdBadgeComponent;
import mx.egd.fmre.register.component.exception.IdBadgeComponentException;
import mx.egd.fmre.register.dto.Aficionado;
import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.dto.Aspirante;
import mx.egd.fmre.register.dto.ImagenDto;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.record.TipoImagen;
import mx.egd.fmre.register.service.AficionadoService;
import mx.egd.fmre.register.service.AfiliacionService;
import mx.egd.fmre.register.service.AspiranteService;
import mx.egd.fmre.register.service.IdBadgeService;
import mx.egd.fmre.register.service.ImagenService;
import mx.egd.fmre.register.service.PersonaService;
import mx.egd.fmre.register.service.TipoImagenService;
import mx.egd.fmre.register.service.exceptions.AficionadoServiceException;
import mx.egd.fmre.register.service.exceptions.AfiliacionServiceException;
import mx.egd.fmre.register.service.exceptions.AspiranteServiceException;
import mx.egd.fmre.register.service.exceptions.CredencialControllerSeviceException;
import mx.egd.fmre.register.service.exceptions.ServiceException;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdBadgeServiceImpl implements IdBadgeService {
    
    private static final String URL_CHECK_FORMAT = "https://fmre.mx/afiliados?id=%s&rnd=%s";
    
    private final IdBadgeComponent  idBadgeComponent;
    private final ImagenService     imagenService;
    private final TipoImagenService tipoImagenService;
    private final PersonaService    personaService;
    private final AfiliacionService afiliacionService;
    private final AficionadoService aficionadoService;
    private final AspiranteService  aspiranteService;

    // 5 cm are 189 pixels
    private static final BigDecimal EQUIV_PIXELS = BigDecimal.valueOf(189);
    private static final BigDecimal EQUIV_CM = BigDecimal.valueOf(5);
    private static final BigDecimal SIZE_CRED_WIDTH_CM = BigDecimal.valueOf(5.40);
    private static final BigDecimal SIZE_CRED_HEIGHT_CM = BigDecimal.valueOf(8.56);
    private static final String PERSONAL_FOTO = "PERSONAL FOTO";
    
    private TipoImagen tipoImagenDocumentoFotoPersonal;
    
    @PostConstruct
    private void init() {
        tipoImagenDocumentoFotoPersonal = tipoImagenService.findAllActive().stream()
                .filter(ti -> ti.tipo().equals(PERSONAL_FOTO))
                .findAny()
                .orElse(null);
    }

    @Override
    public byte[] createIdBadgeService(Integer idPersona) throws CredencialControllerSeviceException {
        ImageData personalPhotoImageData = getPersonalPhotoImageData(idPersona);
        Persona persona = personaService.findByIdPersona(idPersona);
        Afiliacion afiliacion;
        String indicativo;
        String tipoAfiliacion;
        String url;
        
        try {
            afiliacion = getAfiliacionByPersona(persona);
            indicativo = getIndicativoAficionadoOAspirante(persona);
            tipoAfiliacion = getTipoAfiliacion(afiliacion);
            url = createCheckUrl(afiliacion);
        } catch (IdBadgeComponentException e) {
            log.error(e.getMessage());
            throw new CredencialControllerSeviceException(e);
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

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
            
            idBadgeComponent.addPleca(document);
            
            idBadgeComponent.setMiembreActivoParagrph(document);
            
            idBadgeComponent.addPhoto(document, personalPhotoImageData);
            
            idBadgeComponent.addGreka(document);
            
            idBadgeComponent.addNombreAfiliado(document, persona);
            
            idBadgeComponent.addVigencia(document, afiliacion);
            
            idBadgeComponent.addIndicativo(document, indicativo);
            
            idBadgeComponent.addTipoAfiliacion(document, tipoAfiliacion);
            
            pdf.addNewPage(customPageSize);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            
            idBadgeComponent.addBaseImagen(document);
            
            idBadgeComponent.addApoyoText(document);
            
            //idBadgeComponent.addEscudos(document);
            
            idBadgeComponent.addAfiliacionFmreParagraph(document);
            
            idBadgeComponent.addGrekaRev(document, SIZE_CRED_WIDTH_CM, EQUIV_PIXELS, EQUIV_CM);
            
            idBadgeComponent.addQrcode(pdf, document, url);
            
            idBadgeComponent.addSociedadIaru(document);
            
            idBadgeComponent.addIndivativoBack(document, indicativo);
        } catch (IdBadgeComponentException e) {
            log.error(e.getMessage());
            throw new CredencialControllerSeviceException(e);
        }

        return baos.toByteArray();
    }
    
    
    private ImageData getPersonalPhotoImageData(Integer idPersona) {
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
        
        return ImageDataFactory.create(imagenFotoPersonalByteArray);
    }
    
    private Afiliacion getAfiliacionByPersona(Persona persona) throws IdBadgeComponentException {
        if(persona == null) {
            throw new IdBadgeComponentException("Persona is null");
        }
        Afiliacion afiliacion;
        try {
            afiliacion = afiliacionService.findActiveByPersona(persona);
        } catch (AfiliacionServiceException e) {
            throw new IdBadgeComponentException("Error: no se localizaron afiliaciones");
        }
        if(afiliacion == null) {
            throw new IdBadgeComponentException("Error: no se localizaron afiliaciones");
        }
        return afiliacion;
    }
    
    private String getIndicativoAficionadoOAspirante(Persona persona) throws IdBadgeComponentException {
        Aficionado aficionado = null;
        List<Aficionado> aficionadoList;
        try {
            aficionadoList = aficionadoService.findActiveByPersona(persona);
        } catch (AficionadoServiceException e) {
            throw new IdBadgeComponentException("Error al consultar aficionado por persona");
        }
        if(aficionadoList != null && !aficionadoList.isEmpty()) {
            aficionado =  aficionadoList.get(0);
        }
        
        String indicativo;
        
        if (aficionado != null) {
            indicativo = aficionado.getIndicativo();

            if (indicativo.startsWith("XE")) {
                return indicativo.substring(0, 3) + "-" + indicativo.substring(3, indicativo.length());
            } else {
                return indicativo;
            }
        }
        Aspirante aspirante = null;
        List<Aspirante> aspiranteList = null;
        try {
            aspiranteList = aspiranteService.findByPersona(persona);
        } catch (AspiranteServiceException e) {
            throw new IdBadgeComponentException("Error al consultar aspirante por persona");
        }
        if(aspiranteList != null && !aspiranteList.isEmpty()) {
            aspirante = aspiranteList.get(0);
        }
        
        if(aspirante == null) {
            return null;
        }
        
        Integer idEstado = aspirante.getIdEstado();
        int contador = aspirante.getContadorEstado();
        indicativo = "XE-SWL-" + String.format("%02d", idEstado) + "-" + String.format("%02d", contador);
        return indicativo;
    }

    private String getTipoAfiliacion(Afiliacion afiliacion) {
        if (afiliacion == null) {
            log.error("Afiliacion is null");
        }
        switch (afiliacion.getIdTipoAfiliacion()) {
        case 1:
        case 3:
            return "Radioaficionado";
        case 2:
            return "Radioescucha SWL";
        default:
            return null;
        }
    }
    
    private String createCheckUrl(Afiliacion afiliacion) {
        return String.format(URL_CHECK_FORMAT, afiliacion.getIdAfiliacion(), afiliacion.getIdAfiliacion());
    }
}
