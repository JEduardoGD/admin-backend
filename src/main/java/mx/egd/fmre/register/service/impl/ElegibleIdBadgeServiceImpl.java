package mx.egd.fmre.register.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.persistence.entity.ElegibleIdBadgeEntity;
import mx.egd.fmre.register.persistence.repository.ElegibleIdBadgeRepository;
import mx.egd.fmre.register.service.ElegibleIdBadgeService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElegibleIdBadgeServiceImpl implements ElegibleIdBadgeService {

    private final ElegibleIdBadgeRepository elegibleIdBadgeRepository;

    @Override
    public List<ElegibleIdBadgeEntity> findAll() {
        return elegibleIdBadgeRepository.findAll();
    }

    @Override
    public List<ElegibleIdBadgeEntity> findByIdPersona(Integer idPersona) {
        if (idPersona == null) {
            log.error("idPersona is null");
            return null;
        }
        return elegibleIdBadgeRepository.findByIdPersona(idPersona);
    }

}
