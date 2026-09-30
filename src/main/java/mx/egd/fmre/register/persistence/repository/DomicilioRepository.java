package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mx.egd.fmre.register.persistence.entity.DomicilioEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

public interface DomicilioRepository extends JpaRepository<DomicilioEntity, Integer> {
    @Query("SELECT d FROM DomicilioEntity d WHERE d.persona = ?1")
    public List<DomicilioEntity> findByPersonaEntity(PersonaEntity personaEntity);
}
