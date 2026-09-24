package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.egd.fmre.register.persistence.entity.EstadoEntity;

public interface EstadoRepository extends JpaRepository<EstadoEntity, Integer> {
    List<EstadoEntity> findByAbreviado(String abreviado);
}
