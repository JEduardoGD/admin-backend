package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mx.egd.fmre.register.persistence.entity.BusquedaEntity;

public interface BusquedaRepository extends JpaRepository<BusquedaEntity, Integer> {

    @Query("""
            SELECT b
            FROM BusquedaEntity b
            WHERE
                 b.nombre LIKE %:term% OR
                 b.primerApellido LIKE %:term% OR
                 b.segundoApellido LIKE %:term% OR
                 b.indicativo LIKE %:term%
            """)
    List<BusquedaEntity> searchByTerm(String term, Pageable pageable);
}
