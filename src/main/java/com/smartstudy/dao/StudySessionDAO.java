package com.smartstudy.dao;

import com.smartstudy.model.StudySession;
import java.util.List;

/**
 * DAO interface for StudySession operations.
 */
public interface StudySessionDAO {
    List<StudySession> findByStudentId(int studentId);
    List<StudySession> findBySubjectId(int subjectId);
    StudySession findById(int id);
    int insert(StudySession session);
    boolean delete(int id);
    int getTotalMinutesByStudentId(int studentId);
    int getTotalMinutesBySubjectId(int subjectId);
}
