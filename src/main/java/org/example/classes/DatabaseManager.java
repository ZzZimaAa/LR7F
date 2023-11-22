package org.example.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost/lr_7";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void createScheduleTable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS schedule (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "course VARCHAR(255) NOT NULL," +
                    "instructor VARCHAR(255) NOT NULL," +
                    "room VARCHAR(255) NOT NULL," +
                    "date DATE NOT NULL)";
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertSchedule(Schedule schedule) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO schedule (course, instructor, room, date) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, schedule.getCourse());
            preparedStatement.setString(2, schedule.getInstructor());
            preparedStatement.setString(3, schedule.getRoom());
            preparedStatement.setDate(4, schedule.getDate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteSchedule(int scheduleId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM schedule WHERE id = ?")) {
            preparedStatement.setInt(1, scheduleId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateSchedule(Schedule schedule) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE schedule SET course = ?, instructor = ?, room = ?, date = ? WHERE id = ?")) {
            preparedStatement.setString(1, schedule.getCourse());
            preparedStatement.setString(2, schedule.getInstructor());
            preparedStatement.setString(3, schedule.getRoom());
            preparedStatement.setDate(4, schedule.getDate());
            preparedStatement.setInt(5, schedule.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM schedule")) {
            while (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(resultSet.getInt("id"));
                schedule.setCourse(resultSet.getString("course"));
                schedule.setInstructor(resultSet.getString("instructor"));
                schedule.setRoom(resultSet.getString("room"));
                schedule.setDate(resultSet.getDate("date"));
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }
}
