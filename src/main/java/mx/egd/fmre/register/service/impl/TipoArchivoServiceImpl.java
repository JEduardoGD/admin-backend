package mx.egd.fmre.register.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.TipoArchivoDto;
import mx.egd.fmre.register.mapper.to_dto.TipoArchivoEntityMapper;
import mx.egd.fmre.register.persistence.entity.TipoArchivoEntity;
import mx.egd.fmre.register.persistence.repository.TipoArchivoRepository;
import mx.egd.fmre.register.service.TipoArchivoService;

@Service
@RequiredArgsConstructor
public class TipoArchivoServiceImpl implements TipoArchivoService {
    private final TipoArchivoRepository tipoArchivoRepository;

    @Override
    public List<TipoArchivoDto> findAll() {
        List<TipoArchivoEntity> tipoArchivoEntityList = tipoArchivoRepository.findAll();
        if (tipoArchivoEntityList != null) {
            return tipoArchivoEntityList.stream().map(ta -> TipoArchivoEntityMapper.INSTANCE.map(ta)).toList();
        }
        return null;
    }
}
