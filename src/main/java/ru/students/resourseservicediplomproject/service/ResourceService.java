package ru.students.resourseservicediplomproject.service;

import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    String processFiles(MultipartFile image);

    void saveResource(String uuid, String name);

    String base64Image(String uuid);

    boolean deleteResource(String uuid);
}
