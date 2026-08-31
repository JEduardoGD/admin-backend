package mx.egd.fmre.register.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import mx.egd.fmre.register.mapper.to_dto.TipoImagenDocumentoMapper;
import mx.egd.fmre.register.persistence.entity.TipoImagenDocumentoEntity;
import mx.egd.fmre.register.persistence.repository.TipoImagenDocumentoRepository;
import mx.egd.fmre.register.record.TipoImagen;
import mx.egd.fmre.register.service.TipoImagenService;

@AllArgsConstructor
@Service
public class TipoImagenServiceImpl implements TipoImagenService {
    private final TipoImagenDocumentoRepository tipoImagenDocumentoRepository;

    @Override
    public List<TipoImagen> findAllActive() {
        List<TipoImagenDocumentoEntity> allTipoImagenDocumentoList = tipoImagenDocumentoRepository.findAllActive();
        return allTipoImagenDocumentoList.stream()
                .map(td -> {
                    return TipoImagenDocumentoMapper.INSTANCE.map(td);
                })
                .collect(Collectors.toList());
    }
}
