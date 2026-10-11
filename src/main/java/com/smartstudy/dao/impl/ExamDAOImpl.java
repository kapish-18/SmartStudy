package com.smartstudy.dao.impl;

import com.smartstudy.dao.ExamDAO;
import com.smartstudy.model.Exam;
import com.smartstudy.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of ExamDAO.
 */
public class ExamDAOImpl implements ExamDAO {

    @Override
    public List<Exam> findBySubjectId(int subjectId) {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT e.*, s.name AS subject_name FROM exams e " +
                     "JOIN subjects s ON e.subject_id = s.id WHERE e.subject_id = ? ORDER BY e.exam_date";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { exams.add(mapExam(rs)); }
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return exams;
    }

    @Override
    public List<Exam> findUpcomingByStudentId(int studentId) {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT e.*, s.name AS subject_name FROM exams e " +
                     "JOIN subjects s ON e.subject_id = s.id " +
                     "WHERE s.student_id = ? AND e.exam_date >= CURDATE() AND e.marks_obtained < 0 " +
                     "ORDER BY e.exam_date";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { exams.add(mapExam(rs)); }
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return exams;
    }

    @Override
    public List<Exam> findAllByStudentId(int studentId) {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT e.*, s.name AS subject_name FROM exams e " +
                     "JOIN subjects s ON e.subject_id = s.id " +
                     "WHERE s.student_id = ? ORDER BY e.exam_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { exams.add(mapExam(rs)); }
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return exams;
    }

    @Override
    public Exam findById(int id) {
        String sql = "SELECT e.*, s.name AS subject_name FROM exams e " +
                     "JOIN subjects s ON e.subject_id = s.id WHERE e.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { return mapExam(rs); }
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return null;
    }

    @Override
    public int insert(Exam exam) {
        String sql = "INSERT INTO exams (subject_id, exam_name, exam_date, target_marks, marks_obtained) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, exam.getSubjectId());
            stmt.setString(2, exam.getExamName());
            stmt.setDate(3, Date.valueOf(exam.getExamDate()));
            stmt.setDouble(4, exam.getTargetMarks());
            stmt.setDouble(5, exam.getMarksObtained());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) { return keys.getInt(1); }
        } catch (SQLException e) { System.err.println("Error inserting exam: " + e.getMessage()); }
        return -1;
    }

    @Override
    public boolean update(Exam exam) {
        String sql = "UPDATE exams SET subject_id = ?, exam_name = ?, exam_date = ?, target_marks = ?, marks_obtained = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exam.getSubjectId());
            stmt.setString(2, exam.getExamName());
            stmt.setDate(3, Date.valueOf(exam.getExamDate()));
            stmt.setDouble(4, exam.getTargetMarks());
            stmt.setDouble(5, exam.getMarksObtained());
            stmt.setInt(6, exam.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("Error updating exam: " + e.getMessage()); }
        return false;
    }

    @Override
    public List<Exam> findByStudentId(int studentId) {
        return findAllByStudentId(studentId);
    }

    @Override
    public boolean updateMarksObtained(int examId, double marks) {
        String sql = "UPDATE exams SET marks_obtained = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, marks);
            stmt.setInt(2, examId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("Error updating exam marks: " + e.getMessage()); }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM exams WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("Error deleting exam: " + e.getMessage()); }
        return false;
    }

    private Exam mapExam(ResultSet rs) throws SQLException {
        Exam e = new Exam();
        e.setId(rs.getInt("id"));
        e.setSubjectId(rs.getInt("subject_id"));
        e.setExamName(rs.getString("exam_name"));
        e.setExamDate(rs.getDate("exam_date").toLocalDate());
        e.setTargetMarks(rs.getDouble("target_marks"));
        e.setMarksObtained(rs.getDouble("marks_obtained"));
        try { e.setSubjectName(rs.getString("subject_name")); } catch (SQLException ignored) {}
        return e;
    }
}
