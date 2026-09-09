package mx.egd.fmre.register.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Estado;
import mx.egd.fmre.register.mapper.EstadoMapper;
import mx.egd.fmre.register.persistence.entity.EstadoEntity;
import mx.egd.fmre.register.persistence.repository.EstadoRepository;
import mx.egd.fmre.register.service.EstadoService;

@Service
@RequiredArgsConstructor
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
}
