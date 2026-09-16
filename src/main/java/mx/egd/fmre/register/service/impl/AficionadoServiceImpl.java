package mx.egd.fmre.register.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.component.AficionadoComponent;
import mx.egd.fmre.register.dto.Aficionado;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.mapper.to_dto.AficionadoEntityMapper;
import mx.egd.fmre.register.mapper.to_entity.AficionadoMapper;
import mx.egd.fmre.register.persistence.entity.AficionadoEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.AficionadoRepository;
import mx.egd.fmre.register.service.AficionadoService;
import mx.egd.fmre.register.service.exceptions.AficionadoServiceException;
import mx.egd.fmre.register.util.DateTimeUtil;

@Service
@RequiredArgsConstructor
public class AficionadoServiceImpl implements AficionadoService {

    private final AficionadoRepository aficionadoRepository;
    private final AficionadoComponent aficionadoComponent;

    @Override
    public Aficionado save(Aficionado aficionado) throws AficionadoServiceException {
        String periodoError = aficionadoComponent.validatePeriodOnAficionado(aficionado);
        if (periodoError != null) {
            throw new AficionadoServiceException(periodoError);
        }
        String overlapsError = aficionadoComponent.validateOverlaps(aficionado);
        if (overlapsError != null) {
            throw new AficionadoServiceException(overlapsError);
        }
        String edicionError = aficionadoComponent.validatePeriodForEditing(aficionado);
        if (edicionError != null) {
            throw new AficionadoServiceException(edicionError);
        }

        AficionadoEntity aficionadoEntity = AficionadoMapper.INSTANCE.map(aficionado);
        if(aficionado.getIdImagen() == null || aficionado.getIdImagen()  < 0) {
            aficionadoEntity.setImagen(null);
        }
        aficionadoEntity = aficionadoComponent.capturarImagenCertificado(aficionado, aficionadoEntity);
        AficionadoEntity savedAficionadoEntity = aficionadoRepository.save(aficionadoEntity);
        Aficionado savedAficionado = AficionadoEntityMapper.INSTANCE.map(savedAficionadoEntity);
        return savedAficionado;
    }

    @Override
    public Aficionado findByIdAficionado(Integer idAficionado) throws AficionadoServiceException {
        AficionadoEntity aficionadoEntity = aficionadoRepository.findById(idAficionado).orElse(null);
        return AficionadoEntityMapper.INSTANCE.map(aficionadoEntity);
    }

    @Override
    public List<Aficionado> findByPersona(Persona persona) throws AficionadoServiceException {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setIdPersona(persona.getIdPersona());
        List<AficionadoEntity> aficionadoEntity = aficionadoRepository.findByPersona(personaEntity);
        return aficionadoEntity.stream().map(a -> AficionadoEntityMapper.INSTANCE.map(a)).collect(Collectors.toList());
    }

    @Override
    public List<Aficionado> findActiveByPersona(Persona persona) throws AficionadoServiceException {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setIdPersona(persona.getIdPersona());
        List<AficionadoEntity> aficionadoEntity = aficionadoRepository.findByPersona(personaEntity);
        LocalDate localDateNow = DateTimeUtil.getLocalDate();
        List<AficionadoEntity> filtered = aficionadoEntity.stream()
                .filter(a -> a.getFechaInicio() != null)
                .filter(a -> {
                    if(a.getFechaFin() == null) {
                        return a.getFechaInicio().isBefore(localDateNow) || a.getFechaInicio().equals(localDateNow);
                    } else {
                        return (a.getFechaInicio().isBefore(localDateNow) || a.getFechaInicio().equals(localDateNow)) && localDateNow.isBefore(a.getFechaFin());
                    }
                }).toList();
        return filtered.stream().map(a -> AficionadoEntityMapper.INSTANCE.map(a)).collect(Collectors.toList());
    }

    @Override
    public Aficionado delete(Integer idAficionado) throws AficionadoServiceException {
        AficionadoEntity aficionadoEntity = aficionadoRepository.findById(idAficionado).orElse(null);
        if (aficionadoEntity == null) {
            throw new AficionadoServiceException("No existe el aficionado con id " + idAficionado);
        }
        String deletionError = aficionadoComponent.validateDeletion(aficionadoEntity);
        if (deletionError != null) {
            throw new AficionadoServiceException(deletionError);
        }
        aficionadoEntity.setFechaFin(DateTimeUtil.getLocalDate());
        AficionadoEntity savedAficionadoEntity = aficionadoRepository.save(aficionadoEntity);
        return AficionadoEntityMapper.INSTANCE.map(savedAficionadoEntity);
    }
}
