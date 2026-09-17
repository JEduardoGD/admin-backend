package mx.egd.fmre.register.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Aspirante implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idAspirante;
    private Integer idPersona;
    private Integer idEstado;
    private Integer contadorEstado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
