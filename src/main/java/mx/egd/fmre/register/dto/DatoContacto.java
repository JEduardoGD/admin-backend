package mx.egd.fmre.register.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DatoContacto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idDatoContacto;
    private Integer idPersona;
    private Integer idTipoDatoContacto;
    private String dato;
    private LocalDate inicio;
    private LocalDate fin;
}
