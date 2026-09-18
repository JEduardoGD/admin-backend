package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.dto.ImagenDto;
import mx.egd.fmre.register.service.exceptions.ImagenServiceException;

public interface ImagenService {

	ImagenDto save(ImagenDto imagenDto);

	List<ImagenDto> findByIdPersona(int idPersona);

	byte[] getThumbnail(String uuid) throws ImagenServiceException;

	byte[] get(String uuid) throws ImagenServiceException;

	ImagenDto findById(int id);

}
