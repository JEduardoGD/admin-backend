package mx.egd.fmre.register.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.DatoContacto;
import mx.egd.fmre.register.mapper.to_entity.DatoContactoEntityMapper;
import mx.egd.fmre.register.persistence.entity.DatoContactoEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.DatoContactoRepository;
import mx.egd.fmre.register.service.DatoContactoService;

@Service
@RequiredArgsConstructor
public class DatoContactoServiceImpl implements DatoContactoService {

    private final DatoContactoRepository datoContactoRepository;

    @Override
    public DatoContacto save(DatoContacto datoContacto) {
        DatoContactoEntity datoContactoEntity = DatoContactoEntityMapper.INSTANCE.map(datoContacto);
        DatoContactoEntity savedDatoContactoEntity = datoContactoRepository.save(datoContactoEntity);
        return DatoContactoEntityMapper.INSTANCE.map(savedDatoContactoEntity);
    }

    @Override
    public DatoContacto findById(int idDatoContacto) {
        DatoContactoEntity datoContactoEntity = datoContactoRepository.findById(idDatoContacto).orElse(null);
        if (datoContactoEntity == null) {
            return null;
        }
        return DatoContactoEntityMapper.INSTANCE.map(datoContactoEntity);
    }

    @Override
    public List<DatoContacto> findByIdPersona(int idPersona) {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setIdPersona(idPersona);
        return datoContactoRepository.findByPersonaEntity(personaEntity).stream()
                .map(DatoContactoEntityMapper.INSTANCE::map)
                .toList();
    }
}
