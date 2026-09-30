package mx.egd.fmre.register.component;

import mx.egd.fmre.register.dto.Aficionado;
import mx.egd.fmre.register.persistence.entity.AficionadoEntity;

public interface AficionadoComponent {

    String validatePeriodOnAficionado(Aficionado aficionado);

    String validateOverlaps(Aficionado aficionado);

    String validatePeriodForEditing(Aficionado aficionado);

    String validateDeletion(AficionadoEntity aficionadoEntity);

    AficionadoEntity capturarImagenCertificado(Aficionado aficionado, AficionadoEntity aficionadoEntity);
}
