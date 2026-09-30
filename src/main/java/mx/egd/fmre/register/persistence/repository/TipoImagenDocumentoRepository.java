package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mx.egd.fmre.register.persistence.entity.TipoImagenDocumentoEntity;

public interface TipoImagenDocumentoRepository extends JpaRepository<TipoImagenDocumentoEntity, Integer> {
    
    @Query("SELECT t FROM TipoImagenDocumentoEntity t WHERE (t.fechaInicio <= NOW() AND t.fechaFin IS NULL) OR (t.fechaInicio <= NOW() AND NOW() <= t.fechaFin)")
    public List<TipoImagenDocumentoEntity> findAllActive();
}
