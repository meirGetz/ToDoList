package com.task.note.repositories;

import com.task.note.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findByTitle(String title);
    @Query("SELECT l FROM Note l WHERE l.user_id = :userId")
    List<Note> findByUserId(@Param("userId") long userId);
}
