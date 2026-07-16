package mx.egd.fmre.register.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Persona implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3308258917758991156L;
    private long id;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private Date fecnac;
}
