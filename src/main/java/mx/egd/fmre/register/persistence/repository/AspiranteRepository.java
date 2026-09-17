package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mx.egd.fmre.register.persistence.entity.AspiranteEntity;
import mx.egd.fmre.register.persistence.entity.EstadoEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

public interface AspiranteRepository extends JpaRepository<AspiranteEntity, Integer> {
    public List<AspiranteEntity> findByPersona(PersonaEntity personaEntity);
    
    @Query("SELECT MAX(a.contadorEstado) FROM AspiranteEntity a WHERE a.estado = ?1")
    public Integer getCurrentInteger(EstadoEntity estado);
}
