package com.emzio.todo_api.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.emzio.todo_api.model.TaskGroupsRepository;
import com.emzio.todo_api.model.TaskRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("Should throw IllegalStateException when undone task exists")
    void toggleGroup() {
        // given
        var mockTaskRepository = taskRepositoryRetuning(true);

        var mockTaskGroupsRepo = taskGroupRepositoryReturning();
        // when
        TaskGroupService toTest = new TaskGroupService(mockTaskGroupsRepo, mockTaskRepository);
        // then
//        assertThrows(IllegalStateException.class, ()-> toTest.toggleGroup(anyInt()))
        assertThatThrownBy(() -> toTest.toggleGroup(anyInt()))
                .isInstanceOf(IllegalStateException.class);
    }

    private static TaskGroupsRepository taskGroupRepositoryReturning() {
        TaskGroupsRepository mockTaskGroupsRepository = mock(TaskGroupsRepository.class);
        when(mockTaskGroupsRepository.findById(anyInt())).thenReturn(Optional.empty());
        return mockTaskGroupsRepository;
    }

    private static TaskRepository taskRepositoryRetuning(boolean result) {
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }
}