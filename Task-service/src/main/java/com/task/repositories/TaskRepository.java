package com.task.repositories;

import com.task.DTO.ListObjectRequest;
import com.task.entities.ListObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<ListObject, Long> {
    ListObject findByTitle(String title);
    @Query("SELECT l FROM ListObject l WHERE l.user_id = :userId")
    List<ListObject> findByUserId(@Param("userId") long userId);

    ListObject findByTitleAndDescription(String title, String description);

    ListObject findByDescriptionAndTitle(String description, String title);

}
