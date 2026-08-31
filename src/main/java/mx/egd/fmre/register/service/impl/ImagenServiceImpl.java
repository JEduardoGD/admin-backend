package mx.egd.fmre.register.service.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.ImagenDto;
import mx.egd.fmre.register.exception.StorageException;
import mx.egd.fmre.register.exception.UnsupportedImageTypeException;
import mx.egd.fmre.register.mapper.to_dto.ImagenMapper;
import mx.egd.fmre.register.mapper.to_entity.ImagenEntityMapper;
import mx.egd.fmre.register.persistence.entity.ImagenEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.ImagenRepository;
import mx.egd.fmre.register.service.ImagenService;
import mx.egd.fmre.register.service.StorageService;
import mx.egd.fmre.register.service.exceptions.ServiceException;
import net.coobird.thumbnailator.Thumbnails;

@Service
@RequiredArgsConstructor
public class ImagenServiceImpl implements ImagenService {

    private static final int THUMBNAIL_SIZE = 128;
    private static final String THUMBNAIL_FORMAT = "jpg";
    private static final double THUMBNAIL_QUALITY = 0.8;

    private final ImagenRepository imagenRepository;
    private final StorageService storageService;

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

    @Override
    public byte[] getThumbnail(String uuid) throws ServiceException {
        Resource resource = storageService.loadAsResourceByUuid(uuid);
        BufferedImage original = readRasterImage(resource, uuid);
        BufferedImage rgb = flattenToRgb(original);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Thumbnails.of(rgb)
                    .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                    .outputFormat(THUMBNAIL_FORMAT)
                    .outputQuality(THUMBNAIL_QUALITY)
                    .toOutputStream(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new StorageException("Failed to create thumbnail for: " + uuid, e);
        }
    }

    private BufferedImage readRasterImage(Resource resource, String uuid) throws ServiceException {
        try (InputStream in = resource.getInputStream()) {
            BufferedImage original = ImageIO.read(in);
            if (original == null) {
                throw new UnsupportedImageTypeException("Stored file is not a raster image: " + uuid);
            }
            return original;
        } catch (IOException e) {
            throw new StorageException("Could not read file: " + uuid, e);
        }
    }

    private BufferedImage flattenToRgb(BufferedImage source) {
        if (source.getType() == BufferedImage.TYPE_INT_RGB) {
            return source;
        }
        BufferedImage rgb = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = rgb.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, rgb.getWidth(), rgb.getHeight());
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        return rgb;
    }
}
