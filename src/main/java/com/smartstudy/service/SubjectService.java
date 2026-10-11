package com.smartstudy.service;

import com.smartstudy.dao.SubjectDAO;
import com.smartstudy.dao.impl.SubjectDAOImpl;
import com.smartstudy.model.Subject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Business logic for subject management.
 * Demonstrates: HashMap usage for quick subject lookup.
 */
public class SubjectService {

    private final SubjectDAO subjectDAO;

    public SubjectService() {
        this.subjectDAO = new SubjectDAOImpl();
    }

    public List<Subject> getSubjectsByStudentId(int studentId) {
        return subjectDAO.findByStudentId(studentId);
    }

    /**
     * Returns a HashMap of subjects keyed by subject ID for fast lookup.
     * Demonstrates: HashMap collection usage.
     */
    public Map<Integer, Subject> getSubjectMapByStudentId(int studentId) {
        Map<Integer, Subject> map = new HashMap<>();
        List<Subject> subjects = subjectDAO.findByStudentId(studentId);
        for (Subject s : subjects) {
            map.put(s.getId(), s);
        }
        return map;
    }

    public Subject getSubjectById(int id) {
        return subjectDAO.findById(id);
    }

    public int addSubject(Subject subject) {
        return subjectDAO.insert(subject);
    }

    public boolean updateSubject(Subject subject) {
        return subjectDAO.update(subject);
    }

    public boolean deleteSubject(int id) {
        return subjectDAO.delete(id);
    }
}
