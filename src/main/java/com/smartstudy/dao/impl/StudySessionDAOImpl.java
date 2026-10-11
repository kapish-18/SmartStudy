package com.smartstudy.dao.impl;

import com.smartstudy.dao.StudySessionDAO;
import com.smartstudy.model.StudySession;
import com.smartstudy.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of StudySessionDAO.
 */
public class StudySessionDAOImpl implements StudySessionDAO {

    @Override
    public List<StudySession> findByStudentId(int studentId) {
        List<StudySession> sessions = new ArrayList<>();
        String sql = "SELECT ss.*, s.name AS subject_name FROM study_sessions ss " +
                     "JOIN subjects s ON ss.subject_id = s.id WHERE ss.student_id = ? ORDER BY ss.session_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { sessions.add(mapSession(rs)); }
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return sessions;
    }

    @Override
    public List<StudySession> findBySubjectId(int subjectId) {
        List<StudySession> sessions = new ArrayList<>();
        String sql = "SELECT ss.*, s.name AS subject_name FROM study_sessions ss " +
                     "JOIN subjects s ON ss.subject_id = s.id WHERE ss.subject_id = ? ORDER BY ss.session_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) { sessions.add(mapSession(rs)); }
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return sessions;
    }

    @Override
    public StudySession findById(int id) {
        String sql = "SELECT ss.*, s.name AS subject_name FROM study_sessions ss " +
                     "JOIN subjects s ON ss.subject_id = s.id WHERE ss.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { return mapSession(rs); }
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return null;
    }

    @Override
    public int insert(StudySession session) {
        String sql = "INSERT INTO study_sessions (student_id, subject_id, topic, duration_minutes, session_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, session.getStudentId());
            stmt.setInt(2, session.getSubjectId());
            stmt.setString(3, session.getTopic());
            stmt.setInt(4, session.getDurationMinutes());
            stmt.setDate(5, Date.valueOf(session.getSessionDate()));
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) { return keys.getInt(1); }
        } catch (SQLException e) { System.err.println("Error inserting session: " + e.getMessage()); }
        return -1;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM study_sessions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return false;
    }

    @Override
    public int getTotalMinutesByStudentId(int studentId) {
        String sql = "SELECT COALESCE(SUM(duration_minutes), 0) FROM study_sessions WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { return rs.getInt(1); }
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return 0;
    }

    @Override
    public int getTotalMinutesBySubjectId(int subjectId) {
        String sql = "SELECT COALESCE(SUM(duration_minutes), 0) FROM study_sessions WHERE subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { return rs.getInt(1); }
        } catch (SQLException e) { System.err.println("Error: " + e.getMessage()); }
        return 0;
    }

    private StudySession mapSession(ResultSet rs) throws SQLException {
        StudySession s = new StudySession();
        s.setId(rs.getInt("id"));
        s.setStudentId(rs.getInt("student_id"));
        s.setSubjectId(rs.getInt("subject_id"));
        s.setTopic(rs.getString("topic"));
        s.setDurationMinutes(rs.getInt("duration_minutes"));
        s.setSessionDate(rs.getDate("session_date").toLocalDate());
        try { s.setSubjectName(rs.getString("subject_name")); } catch (SQLException ignored) {}
        return s;
    }
}
