package com.smartstudy.dao.impl;

import com.smartstudy.dao.TaskDAO;
import com.smartstudy.model.Task;
import com.smartstudy.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of TaskDAO.
 */
public class TaskDAOImpl implements TaskDAO {

    @Override
    public List<Task> findByStudentId(int studentId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, s.name AS subject_name FROM tasks t " +
                     "LEFT JOIN subjects s ON t.subject_id = s.id " +
                     "WHERE t.student_id = ? ORDER BY t.priority ASC, t.deadline ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tasks.add(mapTask(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding tasks: " + e.getMessage());
        }
        return tasks;
    }

    @Override
    public List<Task> findBySubjectId(int subjectId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, s.name AS subject_name FROM tasks t " +
                     "LEFT JOIN subjects s ON t.subject_id = s.id " +
                     "WHERE t.subject_id = ? ORDER BY t.deadline ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tasks.add(mapTask(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding tasks by subject: " + e.getMessage());
        }
        return tasks;
    }

    @Override
    public Task findById(int id) {
        String sql = "SELECT t.*, s.name AS subject_name FROM tasks t " +
                     "LEFT JOIN subjects s ON t.subject_id = s.id WHERE t.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapTask(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding task: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int insert(Task task) {
        String sql = "INSERT INTO tasks (student_id, subject_id, title, description, priority, deadline, status, estimated_minutes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, task.getStudentId());
            stmt.setInt(2, task.getSubjectId());
            stmt.setString(3, task.getTitle());
            stmt.setString(4, task.getDescription());
            stmt.setInt(5, task.getPriority());
            stmt.setDate(6, task.getDeadline() != null ? Date.valueOf(task.getDeadline()) : null);
            stmt.setString(7, task.getStatus());
            stmt.setInt(8, task.getEstimatedMinutes());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error inserting task: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public boolean update(Task task) {
        String sql = "UPDATE tasks SET subject_id = ?, title = ?, description = ?, priority = ?, " +
                     "deadline = ?, status = ?, estimated_minutes = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, task.getSubjectId());
            stmt.setString(2, task.getTitle());
            stmt.setString(3, task.getDescription());
            stmt.setInt(4, task.getPriority());
            stmt.setDate(5, task.getDeadline() != null ? Date.valueOf(task.getDeadline()) : null);
            stmt.setString(6, task.getStatus());
            stmt.setInt(7, task.getEstimatedMinutes());
            stmt.setInt(8, task.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating task status: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
        }
        return false;
    }

    @Override
    public int countByStudentIdAndStatus(int studentId, String status) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE student_id = ? AND status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setString(2, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting tasks: " + e.getMessage());
        }
        return 0;
    }

    private Task mapTask(ResultSet rs) throws SQLException {
        Task t = new Task();
        t.setId(rs.getInt("id"));
        t.setStudentId(rs.getInt("student_id"));
        t.setSubjectId(rs.getInt("subject_id"));
        t.setTitle(rs.getString("title"));
        t.setDescription(rs.getString("description"));
        t.setPriority(rs.getInt("priority"));
        Date deadline = rs.getDate("deadline");
        t.setDeadline(deadline != null ? deadline.toLocalDate() : null);
        t.setStatus(rs.getString("status"));
        t.setEstimatedMinutes(rs.getInt("estimated_minutes"));
        try {
            t.setSubjectName(rs.getString("subject_name"));
        } catch (SQLException ignored) {}
        return t;
    }
}
