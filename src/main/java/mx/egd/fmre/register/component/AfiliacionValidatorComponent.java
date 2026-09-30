package mx.egd.fmre.register.component;

import mx.egd.fmre.register.dto.Afiliacion;

public interface AfiliacionValidatorComponent {

    String validatePeriodOnAfiliacion(Afiliacion afiliacion);

    String validateOverlaps(Afiliacion afiliacion);

    String validatePeriodForEditing(Afiliacion afiliacion);
}
