package mx.egd.fmre.register.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.dto.Estado;
import mx.egd.fmre.register.mapper.EstadoMapper;
import mx.egd.fmre.register.persistence.entity.EstadoEntity;
import mx.egd.fmre.register.persistence.repository.EstadoRepository;
import mx.egd.fmre.register.service.EstadoService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstadoServiceImpl implements EstadoService {

    private final EstadoRepository estadoRepository;

    @Override
    public List<Estado> getEstadoList() {
        List<EstadoEntity> estadoList = estadoRepository.findAll();
        if (estadoList == null) {
            return null;
        }
        return estadoList.stream().map(EstadoMapper.INSTANCE::map).toList();
    }

    @Override
    public Estado getEstadoByIdEstado(Integer idEstado) {
        if(idEstado == null) {
            log.error("idEstado is null");
            return null;
        }
        EstadoEntity esatdo = estadoRepository.findById(idEstado).orElse(null);
        return EstadoMapper.INSTANCE.map(esatdo);
    }
}
