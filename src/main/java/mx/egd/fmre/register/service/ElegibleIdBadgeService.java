package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.persistence.entity.ElegibleIdBadgeEntity;

public interface ElegibleIdBadgeService {

    List<ElegibleIdBadgeEntity> findAll();

    List<ElegibleIdBadgeEntity> findByIdPersona(Integer idPersona);

}
