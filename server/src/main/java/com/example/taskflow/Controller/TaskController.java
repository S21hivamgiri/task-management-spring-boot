package com.example.taskflow.Controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;

import com.example.taskflow.Model.Project;
import com.example.taskflow.Model.Task;
import com.example.taskflow.Repository.ProjectRepository;
import com.example.taskflow.Repository.TaskRepository;

public class TaskController {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    
    public TaskController(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @MutationMapping
    public Task createTask(@Argument UUID projectId, @Argument String title, @Argument String description) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setProject(project);

        Task saved = taskRepository.save(task);
        return saved;
    }

    @MutationMapping
    public Task updateTaskStatus(@Argument UUID taskId, @Argument Task.TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        task.setStatus(status);
        Task saved = taskRepository.save(task);
        return saved;
    }
}
