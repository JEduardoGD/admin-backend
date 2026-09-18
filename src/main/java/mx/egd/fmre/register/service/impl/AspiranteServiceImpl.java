package mx.egd.fmre.register.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Aspirante;
import mx.egd.fmre.register.dto.Estado;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.mapper.EstadoMapper;
import mx.egd.fmre.register.mapper.to_dto.AspiranteEntityMapper;
import mx.egd.fmre.register.mapper.to_entity.AspiranteMapper;
import mx.egd.fmre.register.persistence.entity.AspiranteEntity;
import mx.egd.fmre.register.persistence.entity.EstadoEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.AspiranteRepository;
import mx.egd.fmre.register.service.AspiranteService;
import mx.egd.fmre.register.service.EstadoService;
import mx.egd.fmre.register.service.exceptions.AspiranteServiceException;

@Service
@RequiredArgsConstructor
public class AspiranteServiceImpl implements AspiranteService {

    private final AspiranteRepository aspiranteRepository;
    private final EstadoService estadoService;
    
    private static Properties contadorEstadosNextValueProperties;
    
    @PostConstruct
    private static void init() throws AspiranteServiceException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "contador_estados_next_value.properties";

        contadorEstadosNextValueProperties = new Properties();
        try (FileInputStream is = new FileInputStream(appConfigPath)){
            contadorEstadosNextValueProperties.load(is);
        } catch (IOException e) {
        	throw new AspiranteServiceException(e);
        }
    }

    @Override
    public Aspirante save(Aspirante aspirante) throws AspiranteServiceException {
        if(aspirante == null) {
            throw new AspiranteServiceException("aspirante is null");
        }
        if(aspirante.getIdAspirante() != null) {
            //si tiene ID aspirante, no es una insercion sino una modificacion
            AspiranteEntity aspiranteE = aspiranteRepository.findById(aspirante.getIdAspirante()).orElse(null);
            if(!aspiranteE.getEstado().getIdEstado().equals(aspirante.getIdEstado())) {
                //como son diferentes estados, se setea el contador en nulo para que sea calculado nuevamente mas abajo
                aspirante.setContadorEstado(null);
            }
        }
        if(aspirante.getContadorEstado() == null || aspirante.getContadorEstado() <= 0) {
            //cuando es almacenamiento, no tiene esto por lo que debe setearse
            Estado estado = estadoService.getEstadoByIdEstado(aspirante.getIdEstado());
            EstadoEntity estadoEntity = EstadoMapper.INSTANCE.map(estado);
            aspirante.setContadorEstado(calculateNextEstadoContador(estadoEntity));
        }
        AspiranteEntity aspiranteEntity = AspiranteMapper.INSTANCE.map(aspirante);
        AspiranteEntity savedAspiranteEntity = aspiranteRepository.save(aspiranteEntity);
        Aspirante savedAspirante = AspiranteEntityMapper.INSTANCE.map(savedAspiranteEntity);
        return savedAspirante;
    }

    @Override
    public Aspirante findByIdAspirante(Integer idAspirante) throws AspiranteServiceException {
        AspiranteEntity aspiranteEntity = aspiranteRepository.findById(idAspirante).orElse(null);
        return AspiranteEntityMapper.INSTANCE.map(aspiranteEntity);
    }

    @Override
    public List<Aspirante> findByPersona(Persona persona) throws AspiranteServiceException {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setIdPersona(persona.getIdPersona());
        List<AspiranteEntity> aspiranteEntity = aspiranteRepository.findByPersona(personaEntity);
        return aspiranteEntity.stream().map(a -> AspiranteEntityMapper.INSTANCE.map(a)).collect(Collectors.toList());
    }
    
    @Override
    public Integer calculateNextEstadoContador(EstadoEntity estado) {
        Integer currentInteger = aspiranteRepository.getCurrentInteger(estado);
        if (currentInteger != null) {
            return currentInteger.intValue() + 1;
        }
        String val = contadorEstadosNextValueProperties.getProperty(estado.getAbreviado());
        currentInteger = val != null ? Integer.valueOf(val) : null;
        if (currentInteger != null) {
            return currentInteger.intValue();
        }
        return 1;
    }
}
