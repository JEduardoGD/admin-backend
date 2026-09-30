package mx.egd.fmre.register.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.dto.TipoDatoContacto;
import mx.egd.fmre.register.mapper.to_dto.TipoDatoContactoEntityMapper;
import mx.egd.fmre.register.persistence.entity.TipoDatoContactoEntity;
import mx.egd.fmre.register.persistence.repository.TipoDatoContactoRepository;
import mx.egd.fmre.register.service.TipoDatoContactoService;

@Service
@RequiredArgsConstructor
@Slf4j
public class TipoDatoContactoServiceImpl implements TipoDatoContactoService {
    private final TipoDatoContactoRepository tipoDatoContactoRepository;
    
    @Override
    public List<TipoDatoContacto> getTipoDatoContacto() {
        List<TipoDatoContactoEntity> tipoDatoContactoList = tipoDatoContactoRepository.findAll();
        if(tipoDatoContactoList == null) {
            log.error("tipoDatoContactoList is null");
        }
        return tipoDatoContactoList.stream().map(TipoDatoContactoEntityMapper.INSTANCE::map).toList();
    }
}
