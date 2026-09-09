package mx.egd.fmre.register.component.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.component.AfiliacionValidatorComponent;
import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.persistence.entity.AfiliacionEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.AfiliacionRepository;
import mx.egd.fmre.register.util.DateTimeUtil;

@Component
@Slf4j
@RequiredArgsConstructor
public class AfiliacionValidatorComponentImpl implements AfiliacionValidatorComponent {

    private static final String AFILIACION_ES_NULO = "Afiliación es nulo";
    private static final String FECHA_INICIO_REQUERIDO = "La fecha inicio es requerido";
    private static final String FECHA_INICIO_DEBE_SER_ANTERIOR_A_FECHA_FIN = "La fecha inicio debe ser anterior a la fecha fin";
    private static final String FECHA_FIN_SOLO_NULO_EN_VITALICIAS = "La fecha fin solo puede ser nulo en afiliaciones vitalicias";
    private static final String AFILIACION_NO_TIENE_ID_PERSONA = "La afiliación no tiene un IDPERSONA";
    private static final String EXISTE_AFILIACION_VITALICIA = "Existe una afiliación vitalicia para esta persona, no se puede crear nueva afiliación";
    private static final String FECHA_INICIAL_NO_PUEDE_SER_ANTERIOR = "La fecha inicial no puede ser anterior a la fecha inicial o fecha final de otra afiliacion vigente";
    private static final String FECHA_FINAL_NO_PUEDE_SER_ANTERIOR = "La fecha final no puede ser anterior a la fecha inicial o fecha final de otra afiliacion vigente";
    private static final String REGISTRO_NO_SE_PUEDE_MODIFICAR = "El registro no se puede modificar luego de 5 dias de haber sido registrado";

    private final AfiliacionRepository afiliacionRepository;

    @Override
    public String validatePeriodOnAfiliacion(Afiliacion afiliacion) {
        if (afiliacion == null) {
            log.error("Afiliacion is null");
            return AFILIACION_ES_NULO;
        }
        Date fechaInicio = afiliacion.getFechaInicio();
        Date fechaFin = afiliacion.getFechaFin();
        boolean vitalicia = afiliacion.isVitalicia();

        if (fechaInicio == null) {
            return FECHA_INICIO_REQUERIDO;
        }

        if (fechaInicio != null && fechaFin != null) {
            LocalDate fechaInicioLD = DateTimeUtil.toLocalDate(fechaInicio);
            LocalDate fechaFinLD = DateTimeUtil.toLocalDate(fechaFin);
            if (!fechaInicioLD.isBefore(fechaFinLD)) {
                return FECHA_INICIO_DEBE_SER_ANTERIOR_A_FECHA_FIN;
            }
        }

        if (fechaFin == null && !vitalicia) {
            return FECHA_FIN_SOLO_NULO_EN_VITALICIAS;
        }

        return null;
    }

    @Override
    public String validateOverlaps(Afiliacion afiliacion) {
        if (afiliacion == null) {
            log.error("Afiliacion is null");
            return AFILIACION_ES_NULO;
        }

        Date afiliacionafiliacionFechaInicio = afiliacion.getFechaInicio();
        if (afiliacion.getIdPersona() == null) {
            log.error("La afiliación no tiene fechaInicio");
            return AFILIACION_NO_TIENE_ID_PERSONA;
        }

        Integer idPersona = afiliacion.getIdPersona();
        if (afiliacion.getIdPersona() == null) {
            log.error("La afiliación no tiene un IDPERSONA");
            return AFILIACION_NO_TIENE_ID_PERSONA;
        }

        PersonaEntity persona = new PersonaEntity();
        persona.setIdPersona(idPersona);
        List<AfiliacionEntity> afiliaciones = afiliacionRepository.findByPersona(persona);
        if (afiliaciones.isEmpty()) {
            return null;
        }

        // filtrar afiliaciones no eliminadas
        List<AfiliacionEntity> afiliacionesNoEliminadas = afiliaciones.stream().filter(a -> !a.isDeleted()).toList();
        if (afiliacionesNoEliminadas.isEmpty()) {
            return null;
        }

        // Si hay una afiliacion vitalicia activa, no se puede enviar una nueva
        // afiliacion
        AfiliacionEntity afiliacionVitalicia = afiliacionesNoEliminadas.stream()
                .filter(a -> a.isVitalicia())
                .findFirst()
                .orElse(null);
        if (afiliacionVitalicia != null
                && !afiliacionVitalicia.getIdAfiliacion().equals(afiliacion.getIdAfiliacion())) {
            return EXISTE_AFILIACION_VITALICIA;
        }
        
        LocalDate afiliacionFechaInicioLD = DateTimeUtil.toLocalDate(afiliacionafiliacionFechaInicio);
        
        Date afiliacionFechaFin = afiliacion.getFechaFin();
        LocalDate afiliacionFechaFinalLD = afiliacionFechaFin != null ? DateTimeUtil.toLocalDate(afiliacionafiliacionFechaInicio) : null;
        
        //validar que la nueva fecha inicio no es anterior a cualquier otra fecha inicio
        boolean errorOnFechaInicial = afiliacionesNoEliminadas.stream()
        .filter(a -> !a.getIdAfiliacion().equals(afiliacion.getIdAfiliacion()))
        .filter(a -> {
            LocalDate fechaInicioLD = a.getFechaInicio();
            LocalDate fechaFinLD = a.getFechaFin();
            return afiliacionFechaInicioLD.isBefore(fechaInicioLD) || afiliacionFechaInicioLD.isBefore(fechaFinLD);
        }).count() > 0;
        if(errorOnFechaInicial) {
            return FECHA_INICIAL_NO_PUEDE_SER_ANTERIOR;
        }
        
        if(afiliacionFechaFinalLD != null) {
            boolean errorOnFechaFinal = afiliacionesNoEliminadas.stream()
                    .filter(a -> !a.getIdAfiliacion().equals(afiliacion.getIdAfiliacion()))
                    .filter(a -> {
                        LocalDate fechaInicioLD = a.getFechaInicio();
                        LocalDate fechaFinLD = a.getFechaFin();
                        return afiliacionFechaFinalLD.isBefore(fechaInicioLD) || afiliacionFechaFinalLD.isBefore(fechaFinLD);
                    }).count() > 0;
                    if(errorOnFechaFinal) {
                        return FECHA_FINAL_NO_PUEDE_SER_ANTERIOR;
                    }
        }
        
        return null;
    }
    
    

    @Override
    public String validatePeriodForEditing(Afiliacion afiliacion) {
        if (afiliacion == null) {
            log.error("Afiliacion is null");
            return AFILIACION_ES_NULO;
        }

        if (afiliacion.getIdAfiliacion() == null) {
            // no es edicion sino almacenamiento nuevo
            return null;
        }

        Integer idAfiliacion = afiliacion.getIdAfiliacion();
        AfiliacionEntity afiliacionEntity = afiliacionRepository.findById(idAfiliacion).orElse(null);
        if (afiliacionEntity == null) {
            log.error("Afiliacion is null");
            return AFILIACION_ES_NULO;
        }

        long hours = DateTimeUtil.diffInHours(afiliacionEntity.getModifiedAt(), DateTimeUtil.getLocalDateTime());

        if (hours > (5 * 24)) {
            return REGISTRO_NO_SE_PUEDE_MODIFICAR;
        }

        return null;
    }
}


































