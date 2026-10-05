package mx.egd.fmre.register.persistence.entity;

import java.io.Serializable;

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
@Table(name = "T_ARCHIVO")
public class ArchivoEntity implements Serializable {
    private static final long serialVersionUID = -3762371721193631829L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDARCHIVO")
    private Integer idArchivo;

    @Column(name = "uuid")
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "IDTIPOARCHIVO")
    private TipoArchivoEntity tipoArchivo;

    @ManyToOne
    @JoinColumn(name = "IDPERSONA")
    private PersonaEntity persona;

    @ManyToOne
    @JoinColumn(name = "IDAFILIACION")
    private AfiliacionEntity afiliacion;

}
