package mx.egd.fmre.register.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class Estado implements Serializable {

    private static final long serialVersionUID = -2915680462047613147L;
    
    private Integer idEstado;
    private String abreviado;
    private String nombre;

}
