package mx.egd.fmre.register.service.impl;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import mx.egd.fmre.register.dto.AspiranteOAficionadoDTO;
import mx.egd.fmre.register.persistence.entity.AfiliacionEntity;
import mx.egd.fmre.register.persistence.entity.DatoContactoEntity;
import mx.egd.fmre.register.persistence.entity.EstadoEntity;
import mx.egd.fmre.register.persistence.entity.ImagenEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

@Data
public class AfiliadoDTO implements Serializable {
    private static final long serialVersionUID = -4775169652993029318L;
    private PersonaEntity personaEntity;
    private EstadoEntity estadoEntity;
    private List<DatoContactoEntity> datoContactoEntityList;
    AspiranteOAficionadoDTO aspiranteOAficionadoDTO;
    private ImagenEntity imagenFotoPersonal;
    private AfiliacionEntity afiliacionEntity;
}
