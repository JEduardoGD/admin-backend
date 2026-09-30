package mx.egd.fmre.register.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Aficionado implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idAficionado;
    private Integer idPersona;
    private String indicativo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer idImagen;
    private String uuid;
    private LocalDateTime modifiedAt;

}
