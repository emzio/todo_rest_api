package com.emzio.todo_api.logic;

import com.emzio.todo_api.model.TaskGroups;
import com.emzio.todo_api.model.TaskGroupsRepository;
import com.emzio.todo_api.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when group has undone tasks")
    void toggleGroup_groupHasUndoneTasks_throwsIllegalStateException() {
//        given
        TaskRepository taskRepository = getTaskRepository(true);
//        system under test
        var toTest = new TaskGroupService(null, taskRepository);
//        when
        var exception = catchThrowable(() -> toTest.toggleGroup(0));
//        then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has undone tasks");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when no group and tasks for given id")
    void toggleGroup_noUndoneTasks_And_noGroup_throwsIllegalArgumentException() {
//        given
        TaskRepository taskRepository = getTaskRepository(false);
//        and
        TaskGroupsRepository repository = mock(TaskGroupsRepository.class);
        when(repository.findById(0)).thenReturn(Optional.empty());
//        system under test
        var toTest = new TaskGroupService(repository, taskRepository);
//        when
        var exception = catchThrowable(() -> toTest.toggleGroup(0));
//        then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");
    }

    @Test
    @DisplayName("should toggle group isDone and save group")
    void toggleGroup_noUndoneTasks_And_existingGroup_toggleGroupIsDoneAndSaves() {
//        given
        TaskRepository taskRepository = getTaskRepository(false);
//        and
        TaskGroups group = new TaskGroups();
        boolean beforeToggle = group.isDone();
//        and
        TaskGroupsRepository repository = mock(TaskGroupsRepository.class);
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(group));
//        system under test
        var toTest = new TaskGroupService(repository, taskRepository);
//        when
        toTest.toggleGroup(group.getId());
//        then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }

//    private static TaskGroupsRepository getTaskGroupsRepository() {
//        return new TaskGroupsRepository() {
//            private static int index = 0;
////             TODO z List<TaskGroups>
////            private List<TaskGroups> list = new ArrayList<>();
//            private Map<Integer, TaskGroups> taskGroupsMap = new HashMap<>();
//            @Override
//            public List<TaskGroups> findAll() {
//                return taskGroupsMap.values().stream()
//                        .toList();
//            }
//
//            @Override
//            public Optional<TaskGroups> findById(Integer integer) {
////                if(integer>=list.size()){
////                    return Optional.empty();
////                }
////                return Optional.ofNullable(list.get(integer));
//
//                return Optional.ofNullable(taskGroupsMap.get(integer));
//            }
//
//            @Override
//            public TaskGroups save(TaskGroups taskGroups) {
//                taskGroupsMap.put(index, taskGroups);
//                index++;
//                return taskGroupsMap.get(index-1);
//            }
//
//            @Override
//            public boolean existsByDoneIsFalseAndProjectId(int projectId) {
//                return false;
//            }
//        };
//    }

    private static TaskRepository getTaskRepository(boolean result) {
        TaskRepository taskRepository = mock(TaskRepository.class);
        when(taskRepository.existsByDoneIsFalseAndGroupId(anyInt())).thenReturn(result);
        return taskRepository;
    }
}