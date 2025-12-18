package ru.students.resourseservicediplomproject.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import ru.students.resourseservicediplomproject.config.ImagePathExtractor;
import ru.students.resourseservicediplomproject.repository.ResourceRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ResourceServiceTest.class);
    @Mock
    ResourceRepository resourceRepository;

    @Mock
    ImagePathExtractor imagePathExtractor;

    @InjectMocks
    ResourceServiceImpl resourceService;

    @Test
    void saveResource() throws IOException {
        //given
        Resource testResource = new ClassPathResource("testImage.png");
        String testPath = "./testImages/";

        Mockito.when(imagePathExtractor.getImagePath()).thenReturn(testPath);

        Mockito.when(resourceRepository.existsById(Mockito.anyString())).thenReturn(false);
        Mockito.when(resourceRepository.save(
                Mockito.any(
                        ru.students.resourseservicediplomproject.entity.Resource.class)
        ))
                .thenReturn(
                        new ru.students.resourseservicediplomproject.entity.Resource(
                                UUID.randomUUID().toString(),
                                Mockito.anyString()));

        //when
        String resourceUUID = resourceService.saveResource(testResource, testResource.getFilename());
        //verify
        Mockito.verify(resourceRepository, Mockito.atLeastOnce()).save(Mockito.any(ru.students.resourseservicediplomproject.entity.Resource.class));
        assertDoesNotThrow(() -> UUID.fromString(resourceUUID));
        assertArrayEquals(testResource.getContentAsByteArray(), new FileSystemResource("%s%s.png".formatted(testPath, resourceUUID)).getContentAsByteArray());
        //assertEquals(testResource.getContentAsByteArray(), new FileSystemResource("%s%s.png".formatted(testPath,resourceUUID)).getContentAsByteArray());
    }

    @Test
    void base64Image_getExistingBase64ImageByUUID_returnsBase64Image() throws IOException {
        //given
        Resource testResource = new ClassPathResource("testImage.png");
        String testPath = "./testImages/";
        String testUUID = UUID.randomUUID().toString();
        Mockito.when(imagePathExtractor.getImagePath()).thenReturn(testPath);

        Path testFile = Files.createFile(Path.of(testPath + testUUID + ".png"));
        Files.write(testFile, testResource.getContentAsByteArray());

        //Mockito.when(resourceRepository.existsById(Mockito.anyString())).thenReturn(false);
        Mockito.when(resourceRepository.findById(testUUID))
                .thenReturn(
                        Optional.of(new ru.students.resourseservicediplomproject.entity.Resource(
                                testUUID,
                                testUUID + ".png")));
        //when
        String base64Image = resourceService.base64Image(testUUID);
        //verify
        assertArrayEquals(Files.readAllBytes(testFile), testResource.getContentAsByteArray());
        assertNotNull(base64Image);
        assertDoesNotThrow(() -> Base64.decode(base64Image));
    }

    @Test
    void base64Image_getNotExistingBase64ImageByUUID_returnsNull() {
        //given
        String testPath = "./testImages/";
        String testUUID = UUID.randomUUID().toString();
        Mockito.when(imagePathExtractor.getImagePath()).thenReturn(testPath);

        Mockito.when(resourceRepository.findById(testUUID))
                .thenReturn(
                        Optional.of(new ru.students.resourseservicediplomproject.entity.Resource(
                                testUUID,
                                testUUID + ".png")));
        //when
        String base64Image = resourceService.base64Image(testUUID);
        //verify
        assertNull(base64Image);
    }

    @Test
    void deleteResource() {
    }

    @AfterAll
    static void tearDown() {
        try (Stream<Path> images = Files.list(Path.of("./testImages/"))){
            images.forEach(path -> {
                if (!path.toFile().delete()) {
                    log.warn("Could not delete file {}", path);
                }
            });
        } catch (IOException e) {
            log.error("Error while deleting testImage", e);
        }
    }
}