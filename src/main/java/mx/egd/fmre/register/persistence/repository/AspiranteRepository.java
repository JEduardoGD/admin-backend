package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.egd.fmre.register.persistence.entity.AspiranteEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

public interface AspiranteRepository extends JpaRepository<AspiranteEntity, Integer> {
    public List<AspiranteEntity> findByPersona(PersonaEntity personaEntity);
}
