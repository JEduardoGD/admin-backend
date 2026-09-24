package mx.egd.fmre.register.dto;

import java.io.Serializable;

import lombok.Data;
import mx.egd.fmre.register.persistence.entity.AficionadoEntity;
import mx.egd.fmre.register.persistence.entity.AspiranteEntity;

@Data
public class AspiranteOAficionadoDTO implements Serializable {
    private static final long serialVersionUID = 2037546083957786986L;
    private AficionadoEntity aficionadoEntity;
    private AspiranteEntity aspiranteEntity;
}