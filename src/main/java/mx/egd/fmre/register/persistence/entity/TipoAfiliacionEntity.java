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
@Table(name = "C_TIPOAFILIACION")
public class TipoAfiliacionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDTIPOAFILIACION")
    private Integer idTipoAfiliacion;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "DESCRIPCION")
    private String descripcion;

}
