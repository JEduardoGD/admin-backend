package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.dto.ImagenDto;
import mx.egd.fmre.register.service.exceptions.ServiceException;

public interface ImagenService {

    ImagenDto save(ImagenDto imagenDto);

    List<ImagenDto> findByIdPersona(int idPersona);

    byte[] getThumbnail(String uuid) throws ServiceException;
}
