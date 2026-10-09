package mx.egd.fmre.register.dto.datatable;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import mx.egd.fmre.register.persistence.entity.AficionadoEntity;
import mx.egd.fmre.register.persistence.entity.AfiliacionEntity;
import mx.egd.fmre.register.persistence.entity.AspiranteEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

@Data
@AllArgsConstructor
public class ResQueryWrapperObject implements Serializable {
    private static final long serialVersionUID = -4186294981759137624L;
    private PersonaEntity persona;
    private AficionadoEntity aficionado;
    private AspiranteEntity aspirante;
    private AfiliacionEntity afiliacion;
}
