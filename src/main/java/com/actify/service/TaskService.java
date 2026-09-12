package com.actify.service;

import com.actify.dto.TaskRequest;
import com.actify.dto.TaskResponse;
import com.actify.entity.Task;
import com.actify.entity.User;
import com.actify.exception.ResourceNotFoundException;
import com.actify.repository.TaskRepository;
import com.actify.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return taskRepository.findByAssignedTo(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return toResponse(task);
    }

    public TaskResponse createTask(TaskRequest request) {
        User assignedTo = userRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getAssignedTo()));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setAssignedTo(assignedTo);

        return toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    // -------- Helper: convert Task entity → TaskResponse DTO --------
    private TaskResponse toResponse(Task task) {
        Long userId = null;
        String userName = null;
        String userEmail = null;
        if (task.getAssignedTo() != null) {
            userId = task.getAssignedTo().getId();
            userName = task.getAssignedTo().getName();
            userEmail = task.getAssignedTo().getEmail();
        }
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                userId,
                userName,
                userEmail
        );
    }
}