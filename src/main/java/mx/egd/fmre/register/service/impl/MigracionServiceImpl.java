package mx.egd.fmre.register.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.egd.fmre.register.persistence.repository.AficionadoRepository;
import mx.egd.fmre.register.persistence.repository.AspiranteRepository;
import mx.egd.fmre.register.persistence.repository.DatoContactoRepository;
import mx.egd.fmre.register.persistence.repository.ImagenRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.dto.AspiranteOAficionadoDTO;
import mx.egd.fmre.register.persistence.entity.AficionadoEntity;
import mx.egd.fmre.register.persistence.entity.AfiliacionEntity;
import mx.egd.fmre.register.persistence.entity.AfiliadoEntity;
import mx.egd.fmre.register.persistence.entity.AspiranteEntity;
import mx.egd.fmre.register.persistence.entity.DatoContactoEntity;
import mx.egd.fmre.register.persistence.entity.EstadoEntity;
import mx.egd.fmre.register.persistence.entity.ImagenEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.entity.RegistroEntity;
import mx.egd.fmre.register.persistence.entity.TipoAfiliacionEntity;
import mx.egd.fmre.register.persistence.entity.TipoDatoContactoEntity;
import mx.egd.fmre.register.persistence.entity.TipoImagenDocumentoEntity;
import mx.egd.fmre.register.persistence.repository.AfiliacionRepository;
import mx.egd.fmre.register.persistence.repository.AfiliadoRepository;
import mx.egd.fmre.register.persistence.repository.EstadoRepository;
import mx.egd.fmre.register.persistence.repository.PersonaRepository;
import mx.egd.fmre.register.persistence.repository.RegistroRepository;
import mx.egd.fmre.register.persistence.repository.TipoDatoContactoRepository;
import mx.egd.fmre.register.service.MigracionService;
import mx.egd.fmre.register.util.DateTimeUtil;
import mx.egd.fmre.register.util.FileInputUtil;
import mx.egd.fmre.register.util.exception.FileInputUtilException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigracionServiceImpl extends FileInputUtil implements MigracionService {

    private final AspiranteRepository aspiranteRepository;
    private final AficionadoRepository aficionadoRepository;
    private final ImagenRepository imagenRepository;
    private final RegistroRepository registroRepository;
    private final AfiliadoRepository afiliadoRepository;
    private final EstadoRepository estadoRepository;
    private final TipoDatoContactoRepository tipoDatoContactoRepository;
    private final PersonaRepository personaRepository;
    private final AfiliacionRepository afiliacionRepository;
    private final DatoContactoRepository datoContactoRepository;
    
    private static final String EMAIL_REGEX_PAT = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"; 
    private static final String SWL_REGEX_PAT = "^(XESWL)\\d{2}\\d{1,3}$";
    private static final String RADIOESCUCHA_SWL = "Radioescucha SWL";
    private static final String RADIOESCUCHA_SWL_B = "Radioescucha (Short Wave Listeing)";
    private static final String RADIOAFICIONADO = "Radioaficionado";
    private static final String RADIOAFICIONADA = "Radioaficionada";
    private static final String PHOTO_PATH = "/home/geduardo/Descargas/inst/fotos";
    private static final String DEST_FILES = "/home/geduardo/adminstracion files/upload files";
    
    private List<EstadoEntity> estadosList;
    private TipoDatoContactoEntity tipoDatoContactoEntityEmail;
    private Pattern patSWLPat;
    
    @PostConstruct
    private void init() {
        estadosList = estadoRepository.findAll();
        tipoDatoContactoEntityEmail = tipoDatoContactoRepository.findById(1).orElse(null);
        patSWLPat = Pattern.compile(SWL_REGEX_PAT);
    }

    @Override
    public void migrate() {
        List<RegistroEntity> registroList = registroRepository.findAll();
        if (registroList == null || registroList.isEmpty()) {
            log.error("registroList is null or empty");
            return;
        }
        List<AfiliadoDTO> afiliadoDTOList = new ArrayList<>();
        for (RegistroEntity registroEntity : registroList) {
            
            
            AfiliadoDTO afiliadoDTO = parseAfiliado(registroEntity.getAfiliado(), registroEntity);
            if(afiliadoDTO == null) {
                log.warn("No afiliacion for {}", registroEntity.getRegId());
                continue;
            }
            

            afiliadoDTOList.add(afiliadoDTO);
        }
        
        for (AfiliadoDTO afiliadoDTO : afiliadoDTOList) {
            persist(afiliadoDTO);
        }
    }
    
    @Transactional
    private void persist(AfiliadoDTO afiliadoDTO) {
        PersonaEntity personaEntity = afiliadoDTO.getPersonaEntity();
        personaEntity = personaRepository.save(personaEntity);
        log.debug(personaEntity.toString());
        
        AfiliacionEntity afiliacionEntity = afiliadoDTO.getAfiliacionEntity();
        afiliacionEntity.setPersona(personaEntity);
        afiliacionEntity = afiliacionRepository.save(afiliacionEntity);
        
        ImagenEntity imagenEntity = afiliadoDTO.getImagenFotoPersonal();
        if (imagenEntity != null) {
            imagenEntity.setPersona(personaEntity);
            imagenEntity.setAfiliacion(afiliacionEntity);
            imagenEntity = imagenRepository.save(imagenEntity);
        }
        
        AspiranteOAficionadoDTO apiranteOAficionadoDTO = afiliadoDTO.getAspiranteOAficionadoDTO();
        AficionadoEntity aficionadoEntity = apiranteOAficionadoDTO.getAficionadoEntity();
        AspiranteEntity aspiranteEntity = apiranteOAficionadoDTO.getAspiranteEntity();
        
        if (aficionadoEntity != null) {
            aficionadoEntity.setPersona(personaEntity);
            aficionadoEntity = aficionadoRepository.save(aficionadoEntity);
        }
        
        if (aspiranteEntity != null) {
            aspiranteEntity.setPersona(personaEntity);
            aspiranteEntity = aspiranteRepository.save(aspiranteEntity);
        }
        
        for (DatoContactoEntity datoContacto : afiliadoDTO.getDatoContactoEntityList()) {
            datoContacto.setPersona(personaEntity);
            datoContactoRepository.save(datoContacto);
        }
        
        
        log.debug("");
    }

    private AfiliadoDTO parseAfiliado(AfiliadoEntity afiliado, RegistroEntity registroEntity) {
        if(afiliado == null) {
            return null;
        }
        Integer afiliadoId = afiliado.getAfilId();
        if (afiliadoId == null || afiliadoId <= 0) {
            log.error("afiliadoId is null or zero");
            return null;
        }
        afiliado = afiliadoRepository.findById(afiliadoId).orElse(null);
        if (afiliado == null) {
            log.error("afiliado is null or zero");
            return null;
        }
        
        PersonaEntity personaEntity = parsePersona(afiliado);
        EstadoEntity estadoEntity = parseEstado(afiliado);
        List<DatoContactoEntity> datoContactoCorreoList = parseDatoContactoCorreo(personaEntity, afiliado);
        ImagenEntity imagenEntityFotoPersonal = parseImagenPersonal(PHOTO_PATH, registroEntity);
        AspiranteOAficionadoDTO aspiranteOAficionadoDTO = parseIndicativo(registroEntity, personaEntity, estadoEntity);
        TipoAfiliacionEntity tipoAfiliacionEntity = parseTipoAfiliacion(registroEntity, registroEntity.getDistintivo());
        AfiliacionEntity afiliacionEntity = parseAfiliacion(registroEntity, personaEntity, estadoEntity, tipoAfiliacionEntity);
        
        AfiliadoDTO afiliadoDTO = new AfiliadoDTO();
        afiliadoDTO.setPersonaEntity(personaEntity);
        afiliadoDTO.setEstadoEntity(estadoEntity);
        afiliadoDTO.setDatoContactoEntityList(datoContactoCorreoList);
        afiliadoDTO.setAspiranteOAficionadoDTO(aspiranteOAficionadoDTO);
        afiliadoDTO.setImagenFotoPersonal(imagenEntityFotoPersonal);
        afiliadoDTO.setAfiliacionEntity(afiliacionEntity);
        
        return afiliadoDTO;
    }
    
    private AfiliacionEntity parseAfiliacion(
            RegistroEntity registroEntity,
            PersonaEntity personaEntity,
            EstadoEntity estadoEntity,
            TipoAfiliacionEntity tipoAfiliacionEntity) {
        LocalDate fechaInicio = registroEntity.getFechaIni();
        LocalDate fechaFin = registroEntity.getFechaFin();
        
        AfiliacionEntity afiliacionEntity = new AfiliacionEntity();
        afiliacionEntity.setPersona(personaEntity);
        afiliacionEntity.setEstado(estadoEntity);
        afiliacionEntity.setTipoAfiliacion(tipoAfiliacionEntity);
        afiliacionEntity.setFechaInicio(fechaInicio);
        afiliacionEntity.setFechaFin(fechaFin);
        afiliacionEntity.setVitalicia(false);
        afiliacionEntity.setDeleted(false);
        return afiliacionEntity;
    }
    
    private TipoAfiliacionEntity parseTipoAfiliacion(RegistroEntity registroEntity, String indicativo) {
        String categoria = registroEntity.getCategoria();
        if (categoria == null) {
            return null;
        }
        TipoAfiliacionEntity tipoAfiliacionEntity = new TipoAfiliacionEntity();
        switch (categoria) {
        case RADIOAFICIONADO:
        case RADIOAFICIONADA: {
            if (indicativo != null && indicativo.toUpperCase().startsWith("XE")) {
                tipoAfiliacionEntity.setIdTipoAfiliacion(1);
            } else {
                tipoAfiliacionEntity.setIdTipoAfiliacion(3);
            }
        }
            break;
        case RADIOESCUCHA_SWL:
        case RADIOESCUCHA_SWL_B:
            tipoAfiliacionEntity.setIdTipoAfiliacion(2);
            break;
        default:
            log.error("No pudo parsearse la categoria '{}'", categoria);
            tipoAfiliacionEntity = null;
        }
        return tipoAfiliacionEntity;
    }
    
    
    private AspiranteOAficionadoDTO parseIndicativo(RegistroEntity registroEntity, PersonaEntity personaEntity,
            EstadoEntity estadoEntity) {
        AspiranteOAficionadoDTO aspiranteOAficionadoDTO = null;
        String indicativoStr = registroEntity.getDistintivo();
        if (indicativoStr == null || indicativoStr.isEmpty()) {
            log.error("indicativo is null or empty");
            return null;
        }
        String indicativo = null;
        if (indicativoStr != null) {
            indicativo = indicativoStr.trim().toUpperCase().replaceAll("\\-", "");
        }
        String categoria = registroEntity.getCategoria();
        if (categoria == null || categoria.isEmpty()) {
            log.error("Categoria is null or empty");
            return null;
        }
        
        Matcher swlMattcher = patSWLPat.matcher(indicativo);
        if (RADIOESCUCHA_SWL.equals(registroEntity.getCategoria()) ||
                RADIOESCUCHA_SWL_B.equals(registroEntity.getCategoria())) {
            if (swlMattcher.matches()) {
                String contadorEstadoStr = indicativo.substring(7, indicativo.length());
                
                int contadorEstado = Integer.valueOf(contadorEstadoStr);
                
                AspiranteEntity aspiranteEntity = new AspiranteEntity();
                aspiranteEntity.setPersona(personaEntity);
                aspiranteEntity.setEstado(estadoEntity);
                aspiranteEntity.setContadorEstado(contadorEstado);
                if (aspiranteOAficionadoDTO == null) {
                    aspiranteOAficionadoDTO = new AspiranteOAficionadoDTO();
                }
                aspiranteEntity.setFechaInicio(DateTimeUtil.getLocalDate());
                aspiranteOAficionadoDTO.setAspiranteEntity(aspiranteEntity);
            } else {
                log.error("Categoria de radioescucha pero no se puede parsear indicativo: '{}'", indicativoStr);
            }
        }
        if (RADIOAFICIONADO.equals(registroEntity.getCategoria()) ||
                RADIOAFICIONADA.equals(registroEntity.getCategoria()) ) {
            AficionadoEntity aficionadoEntity = new AficionadoEntity();
            aficionadoEntity.setPersona(personaEntity);
            aficionadoEntity.setIndicativo(indicativo);
            aficionadoEntity.setFechaInicio(DateTimeUtil.getLocalDate());
            if (aspiranteOAficionadoDTO == null) {
                aspiranteOAficionadoDTO = new AspiranteOAficionadoDTO();
            }
            aspiranteOAficionadoDTO.setAficionadoEntity(aficionadoEntity);
        }
        return aspiranteOAficionadoDTO;
    }
    
    private ImagenEntity parseImagenPersonal(String rootLocation, RegistroEntity registroEntity) {
        if(registroEntity == null) {
            log.error("registroEntity is null");
            return null;
        }
        String fotografiaFileName = registroEntity.getFotografia();
        if(fotografiaFileName == null || "".equals(fotografiaFileName)) {
            //log.warn("fotografiaFileName is empty or null");
            return null;
        }
        
        Path path = load(PHOTO_PATH, fotografiaFileName);
        if(!isReadableFile(path)) {
            log.error("Can't read {}", path.toAbsolutePath());
            return null;
        }
        
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            return null;
        }
        
        String uuid = UUID.randomUUID().toString();
        // Detect the MIME type (e.g., "image/jpeg")
        String extension = null;
        try {
            extension = getExtension(resource.getInputStream());
        } catch (FileInputUtilException e) {
            log.error(e.getMessage());
            return null;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
        
        if(extension == null) {
            extension = "";
        }
        
        String fileName = uuid + extension;
        
        Path destinationFile = Paths.get(DEST_FILES + File.separator + fileName);
        try (InputStream inputStream = resource.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
        
        TipoImagenDocumentoEntity tipoImagenDocumentoFotoPersonal = new TipoImagenDocumentoEntity();
        tipoImagenDocumentoFotoPersonal.setIdTipoImagenDocumento(1);
        
        ImagenEntity imagenEntity = new ImagenEntity();
        imagenEntity.setPersona(null);
        imagenEntity.setAfiliacion(null);
        imagenEntity.setUuid(fileName);
        imagenEntity.setTipoImagenDocumento(tipoImagenDocumentoFotoPersonal);
        return imagenEntity;
    }
    
    private List<DatoContactoEntity> parseDatoContactoCorreo(PersonaEntity persona, AfiliadoEntity afiliado) {
        if (afiliado == null) {
            return null;
        }
        
        List<DatoContactoEntity> datoContactoEntityList = new ArrayList<>();
        
        String correoStr1 = afiliado.getCorreo1();
        if (testEmailAddress(correoStr1)) {
            DatoContactoEntity datoContactoEntity = new DatoContactoEntity();
            datoContactoEntity.setPersona(persona);
            datoContactoEntity.setTipoDatoContacto(tipoDatoContactoEntityEmail);
            datoContactoEntity.setDato(correoStr1.trim());
            datoContactoEntity.setInicio(DateTimeUtil.getLocalDate());
            datoContactoEntityList.add(datoContactoEntity);
        }

        String correoStr2 = afiliado.getCorreo2();
        if (testEmailAddress(correoStr2)) {
            DatoContactoEntity datoContactoEntity = new DatoContactoEntity();
            datoContactoEntity.setPersona(persona);
            datoContactoEntity.setTipoDatoContacto(tipoDatoContactoEntityEmail);
            datoContactoEntity.setDato(correoStr2.trim());
            datoContactoEntity.setInicio(DateTimeUtil.getLocalDate());
            datoContactoEntityList.add(datoContactoEntity);
        }
        
        return datoContactoEntityList;
    }
    
    private boolean testEmailAddress(String mail) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX_PAT);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }
    
    private EstadoEntity parseEstado(AfiliadoEntity afiliado) {
        String estado = afiliado.getEstado();
        if (estado == null) {
            return null;
        }
        EstadoEntity estadoEntity = new EstadoEntity();
        switch (estado.trim()) {
        case "NLE":
            estadoEntity.setIdEstado(19);
            return estadoEntity;
        case "QUI":
            estadoEntity.setIdEstado(23);
            return estadoEntity;
        case "BAC":
            estadoEntity.setIdEstado(2);
            return estadoEntity;
        case "TLX":
            estadoEntity.setIdEstado(29);
            return estadoEntity;
        case "EMX":
            estadoEntity.setIdEstado(15);
            return estadoEntity;
        case "CMX":
            estadoEntity.setIdEstado(9);
            return estadoEntity;
        case "DGO":
            estadoEntity.setIdEstado(10);
            return estadoEntity;
        case "CHI":
            estadoEntity.setIdEstado(7);
            return estadoEntity;
        case "UNA":
            estadoEntity.setIdEstado(36);
            return estadoEntity;
        }
        
        estadoEntity = estadosList.stream()
                .filter(e -> e.getAbreviado().equals(estado))
                .findFirst()
                .orElse(null);
        if(estadoEntity == null) {
            log.error("No se encontro estado para: '{}'", estado);
        }
        return estadoEntity;
    }

    private PersonaEntity parsePersona(AfiliadoEntity afiliado) {
        String nombre = afiliado.getNombres().trim();
        String[] apellidosArray = separateApellidos(afiliado.getApellidos());
        LocalDateTime fechaNacimiento = afiliado.getFechaNac() != null ? afiliado.getFechaNac() : null;
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setNombre(nombre);
        if (apellidosArray != null) {
            personaEntity.setPrimerApellido(apellidosArray[0]);
            personaEntity.setSegundoApellido(apellidosArray[1]);
        }
        personaEntity.setFecNac(fechaNacimiento != null ? fechaNacimiento.toLocalDate() : null);
        return personaEntity;
    }
    
    

    private String[] separateApellidos(String apellidos) {
        if (apellidos == null || apellidos.isBlank()) {
            return null;
        }
            
        apellidos = apellidos.trim();
        
        if (apellidos.isBlank()) {
            return null;
        }
        
        switch (apellidos) {
        case "Curmina López   ":
            return new String[] { "Curmina", "López" };
        case "De La Rosa Cardenas":
            return new String[] { "De La Rosa", "Cardenas" };
        case "De La Rosa Herrera":
            return new String[] { "De La Rosa", "Herrera" };
        case "De La Rosa Chávez":
            return new String[] { "De La Rosa", "Chávez" };
        case "Lopez De La Rosa":
            return new String[] { "Lopez", "De La Rosa" };
        case "Del Valle Pérez":
            return new String[] { "Del Valle", "Pérez" };
        case "De La Cruz Valdez":
            return new String[] { "De La Cruz", "Valdez" };
        case "De La Peña Laurent":
            return new String[] { "De La Peña", "Laurent" };
        case "De Dios Solís":
            return new String[] { "De Dios", "Solís" };
        case "Escobar Del Barco":
            return new String[] { "Escobar", "Del Barco" };
        case "Mejía De La Peña":
            return new String[] { "Mejía", "De La Peña" };
        case "De La Campa Paez":
            return new String[] { "De La Campa", "Paez" };
        case "De La Mora Trigo":
            return new String[] { "De La Mora", "Trigo" };
        case "Maynez Del Real":
            return new String[] { "Maynez", "Del Real" };
        case "Zuñiga De La Mora":
            return new String[] { "Zuñiga", "De La Mora" };
        case "Gasca Cid Del Prado":
            return new String[] { "Gasca", "Cid Del Prado" };
        case "Martín Del Campo":
            return new String[] { "Martín", "Del Campo" };
        case "Pérez De Castro":
            return new String[] { "Pérez", "De Castro" };
        case "Segura Millan Blake":
            return new String[] { "Segura", "Millan Blake" };
        case "Bistein De Keller":
            return new String[] { "Bistein", "De Keller" };
        case "González Lopez Portillo":
            return new String[] { "González", "Lopez Portillo" };
        case "De La Paz García":
            return new String[] { "De La Paz", "García" };
        case "Sánchez De La Vega":
            return new String[] { "Sánchez", "De La Vega" };
        case "Canela De Mazutti":
            return new String[] { "Canela", "De Mazutti" };
        case "Jandete De Ortega":
            return new String[] { "Jandete", "De Ortega" };
        case "Alvarez Loyo Yates":
            return new String[] { "Alvarez", "Loyo Yates" };
        case "De La O Almazan":
            return new String[] { "De La O", "Almazan" };
        case "Vazquez Del Mercado":
            return new String[] { "Vazquez", "Del Mercado" };
        case "Díaz González Ulibarri":
            return new String[] { "Díaz González", "Ulibarri" };
        case "Sainz De Aja Ceron":
            return new String[] { "Sainz De Aja", "Ceron" };
        case "Del Hoyo Briones":
            return new String[] { "Del Hoyo", "Briones" };
        case "Gomez Crespo Y Carballo":
            return new String[] { "Gomez Crespo", "Y Carballo" };
        case "Diaz De Leon Zapata":
            return new String[] { "Diaz De Leon", "Zapata" };
        case "De La Torre Segura":
            return new String[] { "De La Torre", "Segura" };
        case "Herrera De La Rosa":
            return new String[] { "Herrera", "De La Rosa" };
        case "De Anda Ramírez":
            return new String[] { "De Anda", "Ramírez" };
        case "Curmina López   ":
            return new String[] { "Curmina", "López" };
        case "De La Cruz Arias":
            return new String[] { "De La Cruz", "Arias" };
        case "Díaz De León Camou":
            return new String[] { "Díaz De León", "Camou" };
        case "León De Los Santos":
            return new String[] { "León", "De Los Santos" };
        case "Cabrales De Los Santos":
            return new String[] { "Cabrales", "De Los Santos" };
        case "Garza De La Fuente":
            return new String[] { "Garza", "De La Fuente" };
        case "Silva De Jesus":
            return new String[] { "Silva", "De Jesus" };
        case "Perez Bolde Hernández":
            return new String[] { "Perez Bolde", "Hernández" };
        case "Aranda De Alba":
            return new String[] { "Aranda", "De Alba" };
        case "Fuentes Centeno Hidalgo":
            return new String[] { "Fuentes", "Centeno Hidalgo" };
        case "Rivero Rivero Cruz":
            return new String[] { "Rivero", "Rivero Cruz" };
        case "De León Alemán":
            return new String[] { "De León", "Alemán" };
        case "De La Cruz Gomez":
            return new String[] { "De La Cruz", "Gomez" };
        case "De León Méndez":
            return new String[] { "De León", "Méndez" };
        case "De La Cruz Aguilar":
            return new String[] { "De La Cruz", "Aguilar" };
        case "Cuesta Y Méndez":
            return new String[] { "Cuesta", "Y Méndez" };
        case "Leon Bello Alcazar":
            return new String[] { "Leon", "Bello Alcazar" };
        case "Gutierrez Peña Lopez":
            return new String[] { "Gutierrez", "Peña Lopez" };
        case "Montes De Oca Bustos":
            return new String[] { "Montes De Oca", "Bustos" };
        case "Rodrguez De Los Santos":
            return new String[] { "Rodrguez", "De Los Santos" };
        case "De La Mora Ruíz":
            return new String[] { "De La Mora", "Ruíz" };
        case "Madrigal De La Cruz":
            return new String[] { "Madrigal", "De La Cruz" };
        case "De León Ávila":
            return new String[] { "De León", "Ávila" };
        case "González De La Garza":
            return new String[] { "González", "De La Garza" };
        case "Rodríguez Niño de Rivera":
            return new String[] { "Rodríguez", "Niño de Rivera" };
        case "Oriz De Lara":
            return new String[] { "Oriz", "De Lara" };
        case "Van Scoit Gutiérrez":
            return new String[] { "Van Scoit", "Gutiérrez" };
        case "Silva De la Garza":
            return new String[] { "Silva", "De la Garza" };
        case "Gutiérrez De La Rosa":
            return new String[] { "Gutiérrez", "De La Rosa" };
        case "Limón de la Rosa":
            return new String[] { "Limón", "de la Rosa" };
        }
        
        
        String[] apellidosArray = new String[2];
        String[] splited = apellidos.split("\\s+");
        if (splited.length == 1) {
            apellidosArray[0] = splited[0].trim();
            return apellidosArray;
        }
        if (splited.length == 2) {
            apellidosArray[0] = splited[0].trim();
            apellidosArray[1] = splited[1].trim();
            return apellidosArray;
        }
        if (splited.length > 2) {
            log.error("No pudo separarse apellidos: '{}'", apellidos);
        }
        return null;
    }
}
