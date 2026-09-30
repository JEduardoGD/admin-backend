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
@Table(name = "T_AFICIONADO")
public class AficionadoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDAFICIONADO")
    private Integer idAficionado;

    @ManyToOne
    @JoinColumn(name = "IDPERSONA", nullable = false)
    private PersonaEntity persona;

    @Column(name = "INDICATIVO")
    private String indicativo;

    @Column(name = "FECHAINICIO")
    private LocalDate fechaInicio;

    @Column(name = "FECHAFIN")
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "IDIMAGEN")
    private ImagenEntity imagen;

    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @PrePersist
    @PreUpdate
    protected void onCreate() {
        this.modifiedAt = DateTimeUtil.getLocalDateTime();
    }
}
