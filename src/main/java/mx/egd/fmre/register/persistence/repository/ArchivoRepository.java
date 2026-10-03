package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.egd.fmre.register.persistence.entity.AfiliacionEntity;
import mx.egd.fmre.register.persistence.entity.ArchivoEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

public interface ArchivoRepository extends JpaRepository<ArchivoEntity, Integer> {
    List<ArchivoEntity> findByPersonaAndAfiliacion(PersonaEntity persona, AfiliacionEntity afiliacion);
}
