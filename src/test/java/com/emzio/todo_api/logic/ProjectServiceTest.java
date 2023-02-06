package com.emzio.todo_api.logic;

import com.emzio.todo_api.TaskConfigurationProperties;
import com.emzio.todo_api.model.*;
import com.emzio.todo_api.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {
//        given
        var mockGroupRepository = groupRepositoryReturning(true);
//         and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
//        system under test
        var toTest = new ProjectService(null, mockGroupRepository,null, mockConfig);
//        when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
//        then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {
//        given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
//        and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
//        system under test
        var toTest = new ProjectService(mockRepository, null, null, mockConfig);
//        when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
//        then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_And_noProjects_throwsIllegalArgumentException() {
//        given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
//         and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
//        and
        var mockGroupsRepository = groupRepositoryReturning(false);
//        system under test
        var toTest = new ProjectService(mockRepository, mockGroupsRepository,null, mockConfig);
//        when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
//        then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should create new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup() {
        // given
        LocalDateTime today = LocalDate.now().atStartOfDay();
        //and
        var project = projectWith("bar", Set.of(-1,-2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        // and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceWithInMemGroupRepo = dummyTaskGroupService(inMemoryGroupRepo);
        int countBeforeCall = inMemoryGroupRepo.count();
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo,serviceWithInMemGroupRepo, mockConfig);
        // when
        GroupReadModel result = toTest.createGroup(2, today);
        // then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));

        assertThat(countBeforeCall+1).isEqualTo(inMemoryGroupRepo.count());

    }

    private static TaskGroupService dummyTaskGroupService(InMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo, null);
    }

    private Project projectWith(String description, Set<Integer> daysToDeadline){
        var result = mock(Project.class);
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    ProjectStep projectStep = mock(ProjectStep.class);
                    when(projectStep.getDescription()).thenReturn("foo");
                    when(projectStep.getDaysToDeadline()).thenReturn(Long.valueOf(days));
                    return projectStep;
                }).collect(Collectors.toSet());
        when(result.getDescription()).thenReturn(description);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private TaskGroupsRepository groupRepositoryReturning(boolean result) {
        var mockGroupRepository = mock(TaskGroupsRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProjectId(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(boolean value) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(value);
//        and
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupsRepository{
            Map<Integer, TaskGroups> groupsMap = new HashMap<>();
            Integer index = 0;

            public int count(){
                return groupsMap.size();
            }
            @Override
            public List<TaskGroups> findAll() {
            return new ArrayList<>(groupsMap.values());
        }

            @Override
            public Optional<TaskGroups> findById(Integer integer) {
            return Optional.ofNullable(groupsMap.get(integer));
            }

            @Override
            public TaskGroups save(TaskGroups entity) {
            if (entity.getId() == 0) {
                try {
//                    B.class.getSuperclass().getDeclaredFields();
//                    TODO
                    // for superClass:
                    var field = TaskGroups.class.getSuperclass().getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
            groupsMap.put(index, entity);
            return entity;
            }

            @Override
            public boolean existsByDoneIsFalseAndProjectId(int projectId) {
            return groupsMap.values().stream()
                    .filter(groups -> !groups.isDone())
                    .anyMatch(groups -> groups.getProject() != null && groups.getProject().getId() == projectId);
            }
    }

}

