package mx.egd.fmre.register.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Persona implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3308258917758991156L;
    private Integer idPersona;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private Date fecNac;
}
