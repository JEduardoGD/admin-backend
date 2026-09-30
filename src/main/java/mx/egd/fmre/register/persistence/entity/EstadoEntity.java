package mx.egd.fmre.register.persistence.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "C_ESTADO")
public class EstadoEntity implements Serializable{

    private static final long serialVersionUID = 3584212037410398391L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDESTADO")
    private Integer idEstado;

    @Column(name = "ABREVIADO")
    private String abreviado;

    @Column(name = "NOMBRE")
    private String nombre;

}
