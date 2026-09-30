package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import mx.egd.fmre.register.persistence.entity.ElegibleIdBadgeEntity;

public interface ElegibleIdBadgeRepository extends Repository<ElegibleIdBadgeEntity, Integer> {

    List<ElegibleIdBadgeEntity> findAll();

    List<ElegibleIdBadgeEntity> findByIdPersona(Integer idPersona);

}
