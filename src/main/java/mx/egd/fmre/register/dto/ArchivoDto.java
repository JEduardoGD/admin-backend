package mx.egd.fmre.register.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ArchivoDto implements Serializable {
    private static final long serialVersionUID = 5305169590405190823L;

    private Integer idArchivo;
    private String uuid;
    private Integer idTipoArchivo;
    private Integer idPersona;
    private Integer idAfiliacion;
}
