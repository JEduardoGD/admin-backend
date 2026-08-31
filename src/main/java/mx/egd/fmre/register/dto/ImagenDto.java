package mx.egd.fmre.register.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImagenDto implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer idImagen;
    private Integer idPersona;
    private String uuid;
    private Integer idTipoImagenDocumento;
}
