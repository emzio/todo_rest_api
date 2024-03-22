package com.emzio.todo_api.reports;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersistedEventTaskRepository extends JpaRepository<PersistedTaskEvents, Integer> {
    List<PersistedTaskEvents> findByTaskId(int id);
}
