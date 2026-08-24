package mx.egd.fmre.register.service.impl;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import mx.egd.fmre.register.persistence.entity.ImagenEntity;
import mx.egd.fmre.register.persistence.repository.ImagenRepository;
import mx.egd.fmre.register.service.ImagenService;

@Service
@AllArgsConstructor
public class ImagenServiceImpl implements ImagenService {
    private final ImagenRepository imagenRepository;

    public ImagenEntity save(ImagenEntity imagenEntity) {
        return imagenRepository.save(imagenEntity);
    }
}
