package ru.students.resourseservicediplomproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import ru.students.resourseservicediplomproject.config.ImagePathExtractor;
import ru.students.resourseservicediplomproject.repository.ResourceRepository;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock
    ResourceRepository resourceRepository;

    @Mock
    ImagePathExtractor imagePathExtractor;

    @InjectMocks
    ResourceServiceImpl resourceService;

    @Test
    void processFiles() throws IOException {
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
        String resourceUUID = resourceService.processFiles(testResource, testResource.getFilename());
        //verify
        Mockito.verify(resourceRepository, Mockito.atLeastOnce()).save(Mockito.any(ru.students.resourseservicediplomproject.entity.Resource.class));
        assertDoesNotThrow(() -> UUID.fromString(resourceUUID));
        assertArrayEquals(testResource.getContentAsByteArray(), new FileSystemResource("%s%s.png".formatted(testPath, resourceUUID)).getContentAsByteArray());
        //assertEquals(testResource.getContentAsByteArray(), new FileSystemResource("%s%s.png".formatted(testPath,resourceUUID)).getContentAsByteArray());
    }

    @Test
    void saveResource() {
    }

    @Test
    void base64Image() {
    }

    @Test
    void deleteResource() {
    }
}