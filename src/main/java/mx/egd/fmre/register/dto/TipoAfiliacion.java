package mx.egd.fmre.register.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class TipoAfiliacion implements Serializable {
    private static final long serialVersionUID = -785091265273814546L;

    private Integer idTipoAfiliacion;
    private String tipo;
    private String descripcion;
}
