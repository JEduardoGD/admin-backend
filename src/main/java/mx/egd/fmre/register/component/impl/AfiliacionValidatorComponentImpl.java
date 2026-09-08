package mx.egd.fmre.register.component.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class AfiliacionValidatorComponentImpl implements AfiliacionValidatorComponent {

    private final AfiliacionRepository afiliacionRepository;

    @Override
    public String validatePeriodOnAfiliacion(Afiliacion afiliacion) {
        if (afiliacion == null) {
            log.error("Afiliacion is null");
            return "Afiliación es nulo";
        }
        Date fechaInicio = afiliacion.getFechaInicio();
        Date fechaFin = afiliacion.getFechaFin();
        boolean vitalicia = afiliacion.isVitalicia();

        if (fechaInicio == null) {
            return "La fecha inicio es requerido";
        }

        if (fechaInicio != null && fechaFin != null) {
            LocalDate fechaInicioLD = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fechaFinLD = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!fechaInicioLD.isBefore(fechaFinLD)) {
                return "La fecha inicio debe ser anterior a la fecha fin";
            }
        }

        if (fechaFin == null && !vitalicia) {
            return "La fecha fin solo puede ser nulo en afiliaciones vitalicias";
        }

        return null;
    }

    @Override
    public String validateOverlaps(Afiliacion afiliacion) {
        if (afiliacion == null) {
            log.error("Afiliacion is null");
            return "Afiliación es nulo";
        }

        Date afiliacionafiliacionFechaInicio = afiliacion.getFechaInicio();
        if (afiliacion.getIdPersona() == null) {
            log.error("La afiliación no tiene fechaInicio");
            return "La afiliación no tiene un IDPERSONA";
        }

        Integer idPersona = afiliacion.getIdPersona();
        if (afiliacion.getIdPersona() == null) {
            log.error("La afiliación no tiene un IDPERSONA");
            return "La afiliación no tiene un IDPERSONA";
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
            return "Existe una afiliación vitalicia para esta persona, no se puede crear nueva afiliación";
        }
        
        LocalDate afiliacionFechaInicioLD = afiliacionafiliacionFechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        Date afiliacionFechaFin = afiliacion.getFechaFin();
        LocalDate afiliacionFechaFinalLD = afiliacionFechaFin != null ? afiliacionafiliacionFechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        
        //validar que la nueva fecha inicio no es anterior a cualquier otra fecha inicio
        boolean errorOnFechaInicial = afiliacionesNoEliminadas.stream()
        .filter(a -> !a.getIdAfiliacion().equals(afiliacion.getIdAfiliacion()))
        .filter(a -> {
            LocalDate fechaInicioLD = a.getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fechaFinLD = a.getFechaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return afiliacionFechaInicioLD.isBefore(fechaInicioLD) || afiliacionFechaInicioLD.isBefore(fechaFinLD);
        }).count() > 0;
        if(errorOnFechaInicial) {
            return "La fecha inicial no puede ser anterior a la fecha inicial o fecha final de otra afiliacion vigente";
        }
        
        if(afiliacionFechaFinalLD != null) {
            boolean errorOnFechaFinal = afiliacionesNoEliminadas.stream()
                    .filter(a -> !a.getIdAfiliacion().equals(afiliacion.getIdAfiliacion()))
                    .filter(a -> {
                        LocalDate fechaInicioLD = a.getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate fechaFinLD = a.getFechaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return afiliacionFechaFinalLD.isBefore(fechaInicioLD) || afiliacionFechaFinalLD.isBefore(fechaFinLD);
                    }).count() > 0;
                    if(errorOnFechaFinal) {
                        return "La fecha final no puede ser anterior a la fecha inicial o fecha final de otra afiliacion vigente";
                    }
        }
        
        return null;
    }
    
    

    @Override
    public String validatePeriodForEditing(Afiliacion afiliacion) {
        if (afiliacion == null) {
            log.error("Afiliacion is null");
            return "Afiliación es nulo";
        }

        if (afiliacion.getIdAfiliacion() == null) {
            // no es edicion sino almacenamiento nuevo
            return null;
        }

        Integer idAfiliacion = afiliacion.getIdAfiliacion();
        AfiliacionEntity afiliacionEntity = afiliacionRepository.findById(idAfiliacion).orElse(null);
        if (afiliacionEntity == null) {
            log.error("Afiliacion is null");
            return "Afiliación es nulo";
        }

        LocalDateTime modifiedAt = afiliacionEntity.getModifiedAt();

        long hours = ChronoUnit.HOURS.between(modifiedAt, LocalDateTime.now());

        if (hours > (5 /* 24*/)) {
            return "El registro no se puede modificar luego de 5 dias de haber sido registrado";
        }

        return null;
    }
}


































