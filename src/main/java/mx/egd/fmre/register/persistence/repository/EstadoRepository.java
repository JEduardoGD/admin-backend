package mx.egd.fmre.register.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.egd.fmre.register.persistence.entity.EstadoEntity;

public interface EstadoRepository extends JpaRepository<EstadoEntity, Integer> {

}
