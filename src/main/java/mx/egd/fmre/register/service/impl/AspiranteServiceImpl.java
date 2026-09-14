package mx.egd.fmre.register.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Aspirante;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.mapper.to_dto.AspiranteEntityMapper;
import mx.egd.fmre.register.mapper.to_entity.AspiranteMapper;
import mx.egd.fmre.register.persistence.entity.AspiranteEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.AspiranteRepository;
import mx.egd.fmre.register.service.AspiranteService;
import mx.egd.fmre.register.service.exceptions.AspiranteServiceException;

@Service
@RequiredArgsConstructor
public class AspiranteServiceImpl implements AspiranteService {

    private final AspiranteRepository aspiranteRepository;

    @Override
    public Aspirante save(Aspirante aspirante) throws AspiranteServiceException {
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
}
