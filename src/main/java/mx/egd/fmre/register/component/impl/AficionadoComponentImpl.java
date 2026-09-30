package mx.egd.fmre.register.component.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.component.AficionadoComponent;
import mx.egd.fmre.register.dto.Aficionado;
import mx.egd.fmre.register.persistence.entity.AficionadoEntity;
import mx.egd.fmre.register.persistence.entity.ImagenEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.entity.TipoImagenDocumentoEntity;
import mx.egd.fmre.register.persistence.repository.AficionadoRepository;
import mx.egd.fmre.register.persistence.repository.ImagenRepository;
import mx.egd.fmre.register.persistence.repository.TipoImagenDocumentoRepository;
import mx.egd.fmre.register.util.DateTimeUtil;
import mx.egd.fmre.register.util.StaticValues;

@Component
@Slf4j
@RequiredArgsConstructor
public class AficionadoComponentImpl implements AficionadoComponent {

    private static final String AFICIONADO_ES_NULO = "Aficionado es nulo";
    private static final String FECHA_INICIO_REQUERIDO = "La fecha inicio es requerido";
    private static final String FECHA_INICIO_DEBE_SER_ANTERIOR_A_FECHA_FIN = "La fecha inicio debe ser anterior a la fecha fin";
    private static final String AFICIONADO_NO_TIENE_ID_PERSONA = "El aficionado no tiene un IDPERSONA";
    private static final String EXISTE_AFICIONADO_VIGENTE = "Existe un aficionado vigente para esta persona, no se puede crear nuevo registro";
    private static final String FECHA_INICIAL_NO_PUEDE_SER_ANTERIOR = "La fecha inicial no puede ser anterior a la fecha inicial de otro aficionado vigente";
    private static final String FECHA_FINAL_NO_PUEDE_SER_ANTERIOR = "La fecha final no puede ser anterior a la fecha inicial de otro aficionado vigente";
    private static final String REGISTRO_NO_SE_PUEDE_MODIFICAR = "El registro no se puede modificar luego de 5 dias de haber sido registrado";
    private static final String REGISTRO_NO_SE_PUEDE_ELIMINAR = "El aficionado ya esta cerrado, no se puede eliminar";
    private static final String AFICIONADO_IS_NULL = "Aficionado is null";

    private final AficionadoRepository aficionadoRepository;
    private final ImagenRepository imagenRepository;
    private final TipoImagenDocumentoRepository tipoImagenDocumentoRepository;

    @Override
    public String validatePeriodOnAficionado(Aficionado aficionado) {
        if (aficionado == null) {
            log.error(AFICIONADO_IS_NULL);
            return AFICIONADO_ES_NULO;
        }
        LocalDate fechaInicio = aficionado.getFechaInicio();
        LocalDate fechaFin = aficionado.getFechaFin();

        if (fechaInicio == null) {
            return FECHA_INICIO_REQUERIDO;
        }

        if (fechaFin != null) {
            if (!fechaInicio.isBefore(fechaFin)) {
                return FECHA_INICIO_DEBE_SER_ANTERIOR_A_FECHA_FIN;
            }
        }

        return null;
    }

    @Override
    public String validateOverlaps(Aficionado aficionado) {
        if (aficionado == null) {
            log.error(AFICIONADO_IS_NULL);
            return AFICIONADO_ES_NULO;
        }

        if (aficionado.getIdPersona() == null) {
            log.error(AFICIONADO_NO_TIENE_ID_PERSONA);
            return AFICIONADO_NO_TIENE_ID_PERSONA;
        }

        PersonaEntity persona = new PersonaEntity();
        persona.setIdPersona(aficionado.getIdPersona());
        List<AficionadoEntity> aficionados = aficionadoRepository.findByPersona(persona);
        if (aficionados.isEmpty()) {
            return null;
        }

        // filtrar aficionados vigentes (sin fecha fin), excluyendo el registro que se esta editando
        List<AficionadoEntity> aficionadosVigentes = aficionados.stream()
                .filter(a -> a.getFechaFin() == null)
                .filter(a -> aficionado.getIdAficionado() == null || !a.getIdAficionado().equals(aficionado.getIdAficionado()))
                .toList();

        // no se puede abrir un registro nuevo si ya existe uno vigente
        if (aficionado.getIdAficionado() == null && !aficionadosVigentes.isEmpty()) {
            return EXISTE_AFICIONADO_VIGENTE;
        }

        if (aficionadosVigentes.isEmpty() || aficionado.getFechaInicio() == null) {
            return null;
        }

        LocalDate fechaFinLD = aficionado.getFechaFin() != null ? aficionado.getFechaFin() : null;

        // validar que la nueva fecha inicio no es anterior a la fecha inicio de cualquier otro vigente
        boolean errorOnFechaInicial = aficionadosVigentes.stream()
                .anyMatch(a -> aficionado.getFechaInicio().isBefore(a.getFechaInicio()));
        if (errorOnFechaInicial) {
            return FECHA_INICIAL_NO_PUEDE_SER_ANTERIOR;
        }

        if (fechaFinLD != null) {
            boolean errorOnFechaFinal = aficionadosVigentes.stream()
                    .anyMatch(a -> fechaFinLD.isBefore(a.getFechaInicio()));
            if (errorOnFechaFinal) {
                return FECHA_FINAL_NO_PUEDE_SER_ANTERIOR;
            }
        }

        return null;
    }

    @Override
    public String validatePeriodForEditing(Aficionado aficionado) {
        if (aficionado == null) {
            log.error(AFICIONADO_IS_NULL);
            return AFICIONADO_ES_NULO;
        }

        if (aficionado.getIdAficionado() == null) {
            // no es edicion sino almacenamiento nuevo
            return null;
        }

        Integer idAficionado = aficionado.getIdAficionado();
        AficionadoEntity aficionadoEntity = aficionadoRepository.findById(idAficionado).orElse(null);
        if (aficionadoEntity == null) {
            log.error(AFICIONADO_IS_NULL);
            return AFICIONADO_ES_NULO;
        }

        if (aficionadoEntity.getModifiedAt() == null) {
            return null;
        }

        long hours = DateTimeUtil.diffInHours(aficionadoEntity.getModifiedAt(), DateTimeUtil.getLocalDateTime());

        if (hours > (5 * 24)) {
            return REGISTRO_NO_SE_PUEDE_MODIFICAR;
        }

        return null;
    }

    @Override
    public String validateDeletion(AficionadoEntity aficionadoEntity) {
        if (aficionadoEntity == null) {
            log.error(AFICIONADO_IS_NULL);
            return AFICIONADO_ES_NULO;
        }

        if (aficionadoEntity.getFechaFin() != null) {
            return REGISTRO_NO_SE_PUEDE_ELIMINAR;
        }

        return null;
    }

    @Override
    public AficionadoEntity capturarImagenCertificado(Aficionado aficionado, AficionadoEntity aficionadoEntity) {
        if (aficionadoEntity.getImagen() != null || aficionado.getUuid() == null || aficionado.getUuid().isBlank()) {
            return aficionadoEntity;
        }

        TipoImagenDocumentoEntity certificado = tipoImagenDocumentoRepository.findById(StaticValues.CERTIFICADO).orElse(null);
        if (certificado == null) {
            log.error("No existe el tipo de imagen documento CERTIFICADO ({})", StaticValues.CERTIFICADO);
            return aficionadoEntity;
        }

        ImagenEntity imagenEntity = new ImagenEntity();
        imagenEntity.setUuid(aficionado.getUuid());
        imagenEntity.setPersona(aficionadoEntity.getPersona());
        imagenEntity.setTipoImagenDocumento(certificado);
        ImagenEntity savedImagenEntity = imagenRepository.save(imagenEntity);

        aficionadoEntity.setImagen(savedImagenEntity);
        return aficionadoEntity;
    }
}
