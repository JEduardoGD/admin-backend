package mx.egd.fmre.register.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class Afiliacion implements Serializable {
    private static final long serialVersionUID = 6188298910323180043L;

    private Integer idAfiliacion;
    private Integer idPersona;
    private String fechaInicio;
    private String fechaFin;
    private boolean vitalicia;
}
