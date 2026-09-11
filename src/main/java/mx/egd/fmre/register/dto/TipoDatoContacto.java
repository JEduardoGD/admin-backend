package mx.egd.fmre.register.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class TipoDatoContacto implements Serializable {

    private static final long serialVersionUID = 4217791829354612148L;

    private Integer idTipoDatoContacto;
    private String tipoContacto;
    private String descripcion;

}
