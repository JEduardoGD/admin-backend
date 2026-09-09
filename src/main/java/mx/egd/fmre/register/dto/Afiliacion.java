package mx.egd.fmre.register.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Afiliacion implements Serializable {
    private static final long serialVersionUID = 6188298910323180043L;

    private Integer idAfiliacion;
    private Integer idPersona;
    private Integer idEstado;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean vitalicia;
    private boolean deleted;
    private Date modifiedAt;

}