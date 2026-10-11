package com.smartstudy.dao.impl;

import com.smartstudy.dao.SubjectDAO;
import com.smartstudy.model.Subject;
import com.smartstudy.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of SubjectDAO.
 */
public class SubjectDAOImpl implements SubjectDAO {

    @Override
    public List<Subject> findByStudentId(int studentId) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects WHERE student_id = ? ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subjects.add(mapSubject(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding subjects: " + e.getMessage());
        }
        return subjects;
    }

    @Override
    public Subject findById(int id) {
        String sql = "SELECT * FROM subjects WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapSubject(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding subject: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int insert(Subject subject) {
        String sql = "INSERT INTO subjects (student_id, name, course_code, credits, target_marks) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, subject.getStudentId());
            stmt.setString(2, subject.getName());
            stmt.setString(3, subject.getCourseCode());
            stmt.setInt(4, subject.getCredits());
            stmt.setDouble(5, subject.getTargetMarks());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error inserting subject: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public boolean update(Subject subject) {
        String sql = "UPDATE subjects SET name = ?, course_code = ?, credits = ?, target_marks = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subject.getName());
            stmt.setString(2, subject.getCourseCode());
            stmt.setInt(3, subject.getCredits());
            stmt.setDouble(4, subject.getTargetMarks());
            stmt.setInt(5, subject.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating subject: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM subjects WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting subject: " + e.getMessage());
        }
        return false;
    }

    private Subject mapSubject(ResultSet rs) throws SQLException {
        Subject s = new Subject();
        s.setId(rs.getInt("id"));
        s.setStudentId(rs.getInt("student_id"));
        s.setName(rs.getString("name"));
        s.setCourseCode(rs.getString("course_code"));
        s.setCredits(rs.getInt("credits"));
        s.setTargetMarks(rs.getDouble("target_marks"));
        return s;
    }
}
