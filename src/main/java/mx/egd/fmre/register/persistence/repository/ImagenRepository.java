package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mx.egd.fmre.register.persistence.entity.ImagenEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

public interface ImagenRepository extends JpaRepository<ImagenEntity, Integer> {
    @Query("SELECT i FROM ImagenEntity i WHERE i.persona = ?1")
    public List<ImagenEntity> findByPersonaEntity(PersonaEntity personaEntity);
}
