package com.smartstudy.dao;

import com.smartstudy.model.Subject;
import java.util.List;

/**
 * DAO interface for Subject operations.
 */
public interface SubjectDAO {
    List<Subject> findByStudentId(int studentId);
    Subject findById(int id);
    int insert(Subject subject);
    boolean update(Subject subject);
    boolean delete(int id);
}
