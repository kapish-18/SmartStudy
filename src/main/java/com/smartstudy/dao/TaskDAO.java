package com.smartstudy.dao;

import com.smartstudy.model.Task;
import java.util.List;

/**
 * DAO interface for Task operations.
 */
public interface TaskDAO {
    List<Task> findByStudentId(int studentId);
    List<Task> findBySubjectId(int subjectId);
    Task findById(int id);
    int insert(Task task);
    boolean update(Task task);
    boolean updateStatus(int id, String status);
    boolean delete(int id);
    int countByStudentIdAndStatus(int studentId, String status);
}
