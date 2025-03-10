package ru.students.resourseservicediplomproject.service;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.students.resourseservicediplomproject.entity.Resource;
import ru.students.resourseservicediplomproject.repository.ResourceRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public String processFiles(MultipartFile image) {

        String extension = getExtension(image);
        String uuid = getUUID();
        String name = uuid + extension;
        Path path =
                Paths.get("./images/", name);

        try {
            Files.write(path, image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        saveResource(uuid, name);

        log.info("Registered new resource {}", name);

        return uuid;
    }

    @Override
    public void saveResource(String uuid, String name) {
        Resource resource = new Resource(uuid, name);
        resourceRepository.save(resource);
    }

    @Override
    @Nullable
    public String base64Image(String uuid) {
        Optional<Resource> resource = resourceRepository.findById(uuid);
        if (resource.isEmpty()) {
            return null;
        }

        try {
            byte[] bytes = Files.readAllBytes(
                    Paths.get("./images/"
                              + resource.get().getFileName()));
            Base64.Encoder base64 = Base64.getEncoder();
            return base64.encodeToString(bytes);
        } catch (IOException e) {
            return null;
        }


    }

    @Override
    public boolean deleteResource(String uuid) {
        Optional<Resource> resource = resourceRepository.findById(uuid);
        if (resource.isEmpty()) {
            log.warn("Ресурс {} не найден", uuid);
            return false;
        }
        boolean deleted = false;
        for (int i = 1; i <= 3 & !deleted; i++) {
            try {
                deleted = Files.deleteIfExists(
                        Paths.get("./images/"
                                  + resource.get().getFileName()));
            } catch (IOException e) {
                log.error("Ошибка при удалении ресурса {}. Попытка {}", uuid, i, e);
            }
        }
        if (!deleted) {
            log.error("Файл ресурса {} так и не был удален. Запись из базы данных удалена. Может файл был перемещен?", uuid);
        }
        return true;
    }

    private String getUUID() {
        String uuid = String.valueOf(UUID.randomUUID());
        while (resourceRepository.existsById(uuid)) {
            log.debug("uuid {} exist, generated new", uuid);
            uuid = String.valueOf(UUID.randomUUID());

        }
        return uuid;
    }

    private String getExtension(MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            return ".jpg";
        }
        String extension;
        StringBuilder stringBuilder = new StringBuilder(file.getOriginalFilename());
        int indexOfPoint = stringBuilder.lastIndexOf("." );
        extension = stringBuilder.delete(0, indexOfPoint).toString();

        if (validateExtension(extension)) {
            return extension;
        } else {
            log.warn("Расширение файла не соответствует поддерживаемым" );
            return ".jpg";
        }
    }

    private boolean validateExtension(String extension) {
        return extension.equals(".jpg" )
               || extension.equals(".jpeg" )
               || extension.equals(".png" );
    }
}
