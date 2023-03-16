package com.emzio.todo_api.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    Optional<Task> findById(Integer id);

    Page<Task> findAll(Pageable page);

    List<Task> findByDoneIs(@Param("state") Boolean done);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroupId(Integer groupId);

    Task save(Task entity);

    List<Task> findAllByGroup_Id(int groupId);

    List<Task> findAllByDeadlineIsNullAndDoneIsFalseOrDoneIsFalseAndDeadlineIsBefore(LocalDateTime dateTime);

    default List<Task> tasksForDate(LocalDate date){
        return findAllByDeadlineIsNullAndDoneIsFalseOrDoneIsFalseAndDeadlineIsBefore(date.atStartOfDay().plusDays(1));
    }
}
