package ru.students.resourseservicediplomproject.service;

import org.springframework.core.io.Resource;

public interface ResourceService {

    String processFiles(Resource fileResource, String fileName);

    void saveResource(String uuid, String name);

    String base64Image(String uuid);

    boolean deleteResource(String uuid);
}
