package com.smartstudy.dao;

import com.smartstudy.model.Exam;
import java.util.List;

/**
 * DAO interface for Exam operations.
 */
public interface ExamDAO {
    List<Exam> findBySubjectId(int subjectId);
    List<Exam> findUpcomingByStudentId(int studentId);
    List<Exam> findAllByStudentId(int studentId);
    List<Exam> findByStudentId(int studentId);
    Exam findById(int id);
    int insert(Exam exam);
    boolean update(Exam exam);
    boolean updateMarksObtained(int examId, double marks);
    boolean delete(int id);
}
