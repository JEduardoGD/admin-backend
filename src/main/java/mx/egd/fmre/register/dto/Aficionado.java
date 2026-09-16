package mx.egd.fmre.register.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Aficionado implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idAficionado;
    private Integer idPersona;
    private String indicativo;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer idImagen;
    private String uuid;
    private Date modifiedAt;

}
