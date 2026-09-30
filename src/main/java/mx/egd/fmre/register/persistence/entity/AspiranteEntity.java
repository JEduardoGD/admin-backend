package mx.egd.fmre.register.persistence.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "T_ASPIRANTE")
public class AspiranteEntity implements Serializable {

    private static final long serialVersionUID = -8160792979124336215L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDASPIRANTE")
    private Integer idAspirante;

    @ManyToOne
    @JoinColumn(name = "IDPERSONA", nullable = false)
    private PersonaEntity persona;

    @ManyToOne
    @JoinColumn(name = "IDESTADO", nullable = false)
    private EstadoEntity estado;

    @Column(name = "CONTADORESTADO", nullable = false)
    private Integer contadorEstado;
    
    @Column(name = "FECHAINICIO", nullable = false)
    private LocalDate fechaInicio;
    
    @Column(name = "FECHAFIN")
    private LocalDate fechaFin;
}
