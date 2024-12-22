package com.todolist.commonmodule.repositories;

import com.todolist.commonmodule.DTO.ListObjectRequest;
import com.todolist.commonmodule.entities.ListObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<ListObject, Long> {
    ListObject findByTitle(String title);
    @Query("SELECT new com.todolist.commonmodule.DTO.ListObjectRequest(l.title, l.description, l.m_priority, l.status, l.startTime) " +
            "FROM ListObject l WHERE l.user.id = :userId")
    List<ListObjectRequest> findByUserId(@Param("userId") Long userId);
    ListObject findByTitleAndDescription(String title, String description);

    ListObject findByDescriptionAndTitle(String description, String title);

}
