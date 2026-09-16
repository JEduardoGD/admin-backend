package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mx.egd.fmre.register.persistence.entity.DatoContactoEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

public interface DatoContactoRepository extends JpaRepository<DatoContactoEntity, Integer> {
    @Query("SELECT d FROM DatoContactoEntity d WHERE d.persona = ?1")
    public List<DatoContactoEntity> findByPersonaEntity(PersonaEntity personaEntity);
}
