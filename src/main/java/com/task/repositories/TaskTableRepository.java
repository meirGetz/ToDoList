package com.task.repositories;

import com.task.entities.TaskTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTableRepository extends JpaRepository<TaskTable, Long> {
}
