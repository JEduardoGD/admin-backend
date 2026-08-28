package mx.egd.fmre.register.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.ImagenDto;
import mx.egd.fmre.register.mapper.to_dto.ImagenMapper;
import mx.egd.fmre.register.mapper.to_entity.ImagenEntityMapper;
import mx.egd.fmre.register.persistence.entity.ImagenEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.ImagenRepository;
import mx.egd.fmre.register.service.ImagenService;

@Service
@RequiredArgsConstructor
public class ImagenServiceImpl implements ImagenService {

    private final ImagenRepository imagenRepository;

    @Override
    public ImagenDto save(ImagenDto imagenDto) {
        ImagenEntity imagenEntity = ImagenEntityMapper.INSTANCE.imagenDtoToImagenEntity(imagenDto);
        ImagenEntity savedImagenEntity = imagenRepository.save(imagenEntity);
        return ImagenMapper.INSTANCE.imagenEntityToImagenDto(savedImagenEntity);
    }

    @Override
    public List<ImagenDto> findByIdPersona(int idPersona) {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setIdPersona(idPersona);
        List<ImagenEntity> imagenEntityList = imagenRepository.findByPersonaEntity(personaEntity);
        return imagenEntityList.stream()
                .map(i -> ImagenMapper.INSTANCE.imagenEntityToImagenDto(i))
                .collect(Collectors.toList());
    }
}
