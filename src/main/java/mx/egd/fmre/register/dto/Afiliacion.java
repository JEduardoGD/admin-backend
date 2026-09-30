package mx.egd.fmre.register.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Afiliacion implements Serializable {
    private static final long serialVersionUID = 6188298910323180043L;

    private Integer idAfiliacion;
    private Integer idPersona;
    private Integer idEstado;
    private Integer idTipoAfiliacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean vitalicia;
    private boolean deleted;
    private LocalDateTime modifiedAt;

}