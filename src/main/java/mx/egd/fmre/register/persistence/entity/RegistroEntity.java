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
@Table(name = "Registros")
public class RegistroEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reg_id")
    private Integer regId;

    @ManyToOne
    @JoinColumn(name = "fk_afiliados", nullable = true)
    private AfiliadoEntity afiliado;

    @Column(name = "Institucion", nullable = false, length = 20)
    private String institucion;

    @Column(name = "categoria", length = 90)
    private String categoria;

    @Column(name = "Distintivo", length = 30)
    private String distintivo;

    @Column(name = "estado", length = 4)
    private String estado;

    @Column(name = "Ref_legal", length = 200)
    private String refLegal;

    @Column(name = "Fecha_ini")
    private LocalDate fechaIni;

    @Column(name = "Fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "Fecha_alta")
    private LocalDate fechaAlta;

    @Column(name = "Fotografia", length = 128)
    private String fotografia;

    @Column(name = "fk_credencial")
    private Integer fkCredencial;

    @Column(name = "Ejecuto", length = 12)
    private String ejecuto;
}
