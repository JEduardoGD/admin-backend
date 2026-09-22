package mx.egd.fmre.register.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.egd.fmre.register.persistence.entity.AfiliadoEntity;
import mx.egd.fmre.register.persistence.entity.RegistroEntity;

public interface RegistroRepository extends JpaRepository<RegistroEntity, Integer> {
    public List<RegistroEntity> findByAfiliado(AfiliadoEntity afiliadoEntity);
}
