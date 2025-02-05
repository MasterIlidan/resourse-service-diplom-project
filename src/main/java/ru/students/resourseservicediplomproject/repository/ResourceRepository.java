package ru.students.resourseservicediplomproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.students.resourseservicediplomproject.entity.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {
}
