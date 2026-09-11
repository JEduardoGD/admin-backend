package mx.egd.fmre.register.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.dto.TipoAfiliacion;
import mx.egd.fmre.register.mapper.TipoAfiliacionEntityMapper;
import mx.egd.fmre.register.persistence.entity.TipoAfiliacionEntity;
import mx.egd.fmre.register.persistence.repository.TipoAfiliacionRepository;
import mx.egd.fmre.register.service.TipoAfiliacionService;

@Service
@RequiredArgsConstructor
@Slf4j
public class TipoAfiliacionServiceImpl implements TipoAfiliacionService {

    private final TipoAfiliacionRepository tipoAfiliacionRepository;

    @Override
    public List<TipoAfiliacion> findAll() {
        List<TipoAfiliacionEntity> tipoAfiliacionList = tipoAfiliacionRepository.findAll();
        if (tipoAfiliacionList == null) {
            log.error("tipoAfiliacionList is null");
            return null;
        }
        return tipoAfiliacionList.stream().map(TipoAfiliacionEntityMapper.INSTANCE::map).toList();
    }
}
