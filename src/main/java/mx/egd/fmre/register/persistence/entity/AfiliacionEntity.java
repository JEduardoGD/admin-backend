package mx.egd.fmre.register.persistence.entity;

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
@Table(name = "T_AFILIACION")
public class AfiliacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDAFILIACION")
    private Integer idAfiliacion;

    @ManyToOne 
    @JoinColumn(name = "IDPERSONA")
    private PersonaEntity persona;

    @Column(name = "FECHAINICIO")
    private String fechaInicio;

    @Column(name = "FECHAFIN")
    private String fechaFin;

    @Column(name = "VITALICIA")
    private boolean vitalicia;
}
