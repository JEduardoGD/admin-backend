package mx.egd.fmre.register.persistence.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Afiliados")
public class AfiliadoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Afil_id")
    private Integer afilId;

    @Column(name = "Institucion", nullable = false, length = 150)
    private String institucion;

    @Column(name = "Nombres", nullable = false, length = 80)
    private String nombres;

    @Column(name = "Apellidos", nullable = false, length = 80)
    private String apellidos;

    @Column(name = "fecha_nac")
    private LocalDateTime fechaNac;

    @Column(name = "Estado", nullable = false, length = 4)
    private String estado;

    @Column(name = "Referencia", nullable = false, length = 18)
    private String referencia;

    @Column(name = "Fecha_ingreso")
    private LocalDate fechaIngreso;

    @Lob
    @Column(name = "Comentarios")
    private String comentarios;

    @Column(name = "correo1", length = 60)
    private String correo1;

    @Column(name = "correo2", length = 60)
    private String correo2;

    @Column(name = "SK", length = 2)
    private String sk;

    @Column(name = "Ejecuto", length = 15)
    private String ejecuto;

    @Lob
    @Column(name = "capacitacion")
    private String capacitacion;
}
