package com.task.repositories;

import com.task.entities.ListObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<ListObject, Long> {
    ListObject findByTitle(String title);

    List<ListObject> findByUserId(Long id);
    ListObject findByDescription(String description);

    ListObject findByTitleAndDescription(String title, String description);

    ListObject findByDescriptionAndTitle(String description, String title);

}
