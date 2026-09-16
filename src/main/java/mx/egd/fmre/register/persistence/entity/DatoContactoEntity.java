package mx.egd.fmre.register.persistence.entity;

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
@Table(name = "T_DATOCONTACTO")
public class DatoContactoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDDATOCONTACTO")
    private Integer idDatoContacto;

    @ManyToOne
    @JoinColumn(name = "IDPERSONA")
    private PersonaEntity persona;

    @ManyToOne
    @JoinColumn(name = "IDTIPODATOCONTACTO")
    private TipoDatoContactoEntity tipoDatoContacto;

    @Column(name = "DATO")
    private String dato;

    @Column(name = "INICIO")
    private LocalDate inicio;

    @Column(name = "FIN")
    private LocalDate fin;
}
