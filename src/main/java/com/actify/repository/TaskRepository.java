package com.actify.repository;

import com.actify.entity.Task;
import com.actify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(User user);
    List<Task> findByAssignedToId(Long userId);
}