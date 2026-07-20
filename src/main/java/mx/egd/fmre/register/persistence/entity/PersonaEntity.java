package mx.egd.fmre.register.persistence.entity;

import java.util.Date;

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
@Table(name = "T_PERSONA")
public class PersonaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPERSONA")
    private Integer idPersona;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "PRIMERAPELLIDO")
    private String primerApellido;

    @Column(name = "SEGUNDOAPELLIDO")
    private String segundoApellido;

    @Column(name = "FECNAC")
    private Date fecNac;
}
