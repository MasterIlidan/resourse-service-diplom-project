package ru.students.resourseservicediplomproject.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.students.resourseservicediplomproject.service.ResourceService;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-development.properties")
class ResourceControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ResourceService resourceService;

    @Test
    @Transactional
    @Rollback
    void registerNewResource_returnWithStatusCreated() throws Exception {

        Resource fileResource = new ClassPathResource("./testImage.png");
        MockMultipartFile firstFile = new MockMultipartFile(
                "image", fileResource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE,
                fileResource.getInputStream());
        AtomicReference<String> uuidTest = new AtomicReference<>();
        try {
            mockMvc.perform(multipart("/resource")
                            .file(firstFile))
                    .andExpectAll(
                            status().isCreated(),
                            (res) -> {
                                assertDoesNotThrow(() -> {
                                    UUID.fromString(res.getResponse().getContentAsString());
                                });
                            },
                            (res) -> {
                                uuidTest.set(res.getResponse().getContentAsString());
                            });
        } finally {
            //убрать созданный файл
            resourceService.deleteResource(uuidTest.get());
        }
    }

    @Test
    void getResource_returnWithStatusOk() {

    }

    @Test
    void getResource_returnWithStatusNotFound() {
    }


    @Test
    void deleteResource_returnWithStatusOk() {

    }

    @Test
    void deleteResource_returnWithStatusNotFound() {

    }
}