package mx.egd.fmre.register.persistence.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.egd.fmre.register.util.DateTimeUtil;

@Entity
@Data
@NoArgsConstructor
@Table(name = "T_AFILIACION")
public class AfiliacionEntity implements Serializable {
    private static final long serialVersionUID = 2302012202772999884L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDAFILIACION")
    private Integer idAfiliacion;

    @ManyToOne 
    @JoinColumn(name = "IDPERSONA")
    private PersonaEntity persona;

    @ManyToOne 
    @JoinColumn(name = "IDESTADO")
    private EstadoEntity estado;

    @Column(name = "FECHAINICIO")
    private LocalDate fechaInicio;

    @Column(name = "FECHAFIN")
    private LocalDate fechaFin;

    @Column(name = "VITALICIA", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean vitalicia;

    @Column(name = "DELETED", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean deleted;

    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @PrePersist
    @PreUpdate
    protected void onCreate() {
        this.modifiedAt = DateTimeUtil.getLocalDateTime();
    }
    
    
}
