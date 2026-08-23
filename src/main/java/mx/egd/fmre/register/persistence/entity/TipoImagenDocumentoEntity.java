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
@Table(name = "C_TIPOIMAGENDOCUMENTO")
public class TipoImagenDocumentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "IDTIPOIMAGENDOCUMENTO")
	private Integer idTipoImagenDocumento;

    @Column(name = "TIPO")
	private String tipo;

    @Column(name = "DESCRIPCION")
	private String descripcion;

    @Column(name = "FECHAINICIO")
	private Date fechaInicio;

    @Column(name = "FECHAFIN")
	private Date fechaFin;
}
