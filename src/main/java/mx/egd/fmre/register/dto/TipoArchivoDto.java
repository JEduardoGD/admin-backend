package mx.egd.fmre.register.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class TipoArchivoDto implements Serializable {
    private static final long serialVersionUID = -1361190650001396746L;
    private Integer idTipoArchivo;
    private String tipo;
}
