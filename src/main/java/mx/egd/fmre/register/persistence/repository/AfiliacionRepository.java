package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.egd.fmre.register.persistence.entity.AfiliacionEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

public interface AfiliacionRepository extends JpaRepository<AfiliacionEntity, Integer> {
    public List<AfiliacionEntity> findByPersona(PersonaEntity personaEntity);
}
