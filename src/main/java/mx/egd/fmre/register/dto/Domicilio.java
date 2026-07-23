package mx.egd.fmre.register.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Domicilio implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2873208807126467245L;
    private Integer idDomicilio;
    private Integer idPersona;
    private String cp;
    private String domicilio;
    private String entidadFederativa;
    private String municipio;
    private String localidad;
    private String pais;
}
