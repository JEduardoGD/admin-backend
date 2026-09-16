package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.egd.fmre.register.persistence.entity.AficionadoEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

public interface AficionadoRepository extends JpaRepository<AficionadoEntity, Integer> {
    public List<AficionadoEntity> findByPersona(PersonaEntity personaEntity);
}
