package mx.egd.fmre.register.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.mapper.to_dto.TipoImagenDocumentoMapper;
import mx.egd.fmre.register.persistence.entity.TipoImagenDocumentoEntity;
import mx.egd.fmre.register.persistence.repository.TipoImagenDocumentoRepository;
import mx.egd.fmre.register.record.TipoImagen;
import mx.egd.fmre.register.service.TipoImagenService;
import mx.egd.fmre.register.util.StaticValues;

@AllArgsConstructor
@Service
@Slf4j
public class TipoImagenServiceImpl implements TipoImagenService {
    
    private final TipoImagenDocumentoRepository tipoImagenDocumentoRepository;
    
    private static final List<Integer> idsTipoImagenPersona = Arrays.asList(
            StaticValues.PERSONAL_FOTO,
            StaticValues.INE,
            StaticValues.CERTIFICADO,
            StaticValues.ACTA_ENTREGA
            );
    private static final List<Integer> idsTipoImagenAfiliacion = Arrays.asList(
            StaticValues.PAGO,
            StaticValues.SOLICITUD
            );

    @Override
    public List<TipoImagen> findAllActive() {
        List<TipoImagenDocumentoEntity> allTipoImagenDocumentoList = tipoImagenDocumentoRepository.findAllActive();
        return allTipoImagenDocumentoList.stream()
                .map(td -> {
                    return TipoImagenDocumentoMapper.INSTANCE.map(td);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TipoImagen> getImageTypeForGroup(int tipo) {
        List<TipoImagenDocumentoEntity> allTipoImagenDocumentoList;
        if(tipo == StaticValues.FOR_PERSONA) {
        allTipoImagenDocumentoList = tipoImagenDocumentoRepository.findAllById(idsTipoImagenPersona);
        }
        else if(tipo == StaticValues.FOR_AFILIACION) {
            allTipoImagenDocumentoList = tipoImagenDocumentoRepository.findAllById(idsTipoImagenAfiliacion);
        }else {
            log.error("Error on tipoGrupo: {}", tipo);
            return null;
        }
        
        return allTipoImagenDocumentoList.stream()
                .map(td -> {
                    return TipoImagenDocumentoMapper.INSTANCE.map(td);
                })
                .collect(Collectors.toList());
    }
}
