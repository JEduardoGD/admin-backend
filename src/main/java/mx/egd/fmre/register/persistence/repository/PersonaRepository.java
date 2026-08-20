package mx.egd.fmre.register.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.egd.fmre.register.dto.datatable.QueryObj;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

@Repository
public interface PersonaRepository extends JpaRepository<PersonaEntity, Integer> {
    @Query("SELECT p FROM PersonaEntity p WHERE UPPER(p.nombre) = UPPER(?1)")
    List<PersonaEntity> findByNombre(String nombre);
    
    @Query("SELECT p FROM PersonaEntity p WHERE UPPER(p.primerApellido) = UPPER(?1)")
    List<PersonaEntity> findByPrimerApellido(String primerApellido);
    
    @Query("SELECT p FROM PersonaEntity p WHERE UPPER(p.segundoApellido) = UPPER(?1)")
    List<PersonaEntity> findBySegundoApellido(String segundoApellido);
    
    @Query("SELECT p FROM PersonaEntity p WHERE p.fecNac = ?1")
    List<PersonaEntity> findByFecnac(Date fecnac);
    
    @Query("""
            SELECT p FROM PersonaEntity p WHERE 
            UPPER(p.nombre) = UPPER(?1) AND 
            UPPER(p.primerApellido) = UPPER(?2)
             """)
    List<PersonaEntity> findByNombreAndPrimerApellido(String nombre, String primerApellido);
    
    @Query("""
            SELECT p FROM PersonaEntity p WHERE
                 p.nombre LIKE '%:nombre%' OR
                 p.primerApellido LIKE '%:primerApellido%' OR
                 p.segundoApellido LIKE '%:segundoApellido%' OR
            """)
    List<PersonaEntity> searchByConditions(Pageable pageable);
    
    @Query("""
            SELECT p FROM PersonaEntity p WHERE
                 p.nombre REGEXP :nombre OR
                 p.primerApellido REGEXP :primerApellido OR
                 p.segundoApellido REGEXP :segundoApellido OR
            """)
    List<PersonaEntity> searchByRegexConditions(QueryObj queryObj, Pageable pageable);
}
