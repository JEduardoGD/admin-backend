package mx.egd.fmre.register.persistence.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

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
    private Date fechaInicio;

    @Column(name = "FECHAFIN")
    private Date fechaFin;

    @Column(name = "VITALICIA", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean vitalicia;

    @Column(name = "DELETED", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean deleted;

    @Column(name = "MODIFIED_AT", insertable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;
}
