package mx.egd.fmre.register.persistence.entity;

import java.io.Serializable;

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
@Table(name = "C_TIPOARCHIVO")
public class TipoArchivoEntity implements Serializable {
    private static final long serialVersionUID = 2301369543744324609L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "IDTIPOARCHIVO")
    private Integer idTipoArchivo;

    @Column(name = "TIPO")
    private String tipo;
}
