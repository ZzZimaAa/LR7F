// Запускать в терминале командой: mvn clean javafx:run

package org.example.classes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.Optional;

public class ScheduleApp extends Application {
    private TableView<Schedule> scheduleTable = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.createScheduleTable();

        scheduleTable.getItems().addAll(DatabaseManager.getAllSchedules());

        TableColumn<Schedule, String> courseColumn = new TableColumn<>("Course");
        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseProperty());

        TableColumn<Schedule, String> instructorColumn = new TableColumn<>("Instructor");
        instructorColumn.setCellValueFactory(cellData -> cellData.getValue().instructorProperty());

        TableColumn<Schedule, String> roomColumn = new TableColumn<>("Room");
        roomColumn.setCellValueFactory(cellData -> cellData.getValue().roomProperty());

        TableColumn<Schedule, Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        TableColumn<Schedule, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    Schedule schedule = getTableView().getItems().get(getIndex());
                    showUpdateScheduleDialog(schedule);
                });

                deleteButton.setOnAction(event -> {
                    Schedule schedule = getTableView().getItems().get(getIndex());
                    showDeleteConfirmationDialog(schedule);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    VBox buttons = new VBox(editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        scheduleTable.getColumns().addAll(courseColumn, instructorColumn, roomColumn, dateColumn, actionsColumn);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> showAddScheduleDialog());

        VBox vBox = new VBox(scheduleTable, addButton);
        Scene scene = new Scene(vBox, 600, 400);

        primaryStage.setTitle("Schedule App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddScheduleDialog() {
        Dialog<Schedule> dialog = new Dialog<>();
        dialog.setTitle("Add Schedule");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField courseField = new TextField();
        courseField.setPromptText("Course");

        TextField instructorField = new TextField();
        instructorField.setPromptText("Instructor");

        TextField roomField = new TextField();
        roomField.setPromptText("Room");

        DatePicker dateField = new DatePicker();
        dateField.setPromptText("Date");

        dialog.getDialogPane().setContent(new VBox(courseField, instructorField, roomField, dateField));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    Schedule schedule = new Schedule();
                    schedule.setCourse(courseField.getText());
                    schedule.setInstructor(instructorField.getText());
                    schedule.setRoom(roomField.getText());
                    schedule.setDate(Date.valueOf(dateField.getValue()));

                    DatabaseManager.insertSchedule(schedule);

                    scheduleTable.getItems().add(schedule);

                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMessage("Invalid input. Please check your data.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showUpdateScheduleDialog(Schedule schedule) {
        Dialog<Schedule> dialog = new Dialog<>();
        dialog.setTitle("Update Schedule");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextField courseField = new TextField(schedule.getCourse());
        TextField instructorField = new TextField(schedule.getInstructor());
        TextField roomField = new TextField(schedule.getRoom());
        DatePicker dateField = new DatePicker(schedule.getDate().toLocalDate());

        dialog.getDialogPane().setContent(new VBox(courseField, instructorField, roomField, dateField));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    schedule.setCourse(courseField.getText());
                    schedule.setInstructor(instructorField.getText());
                    schedule.setRoom(roomField.getText());
                    schedule.setDate(Date.valueOf(dateField.getValue()));

                    DatabaseManager.updateSchedule(schedule);
                    scheduleTable.refresh();

                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMessage("Invalid input. Please check your data.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showDeleteConfirmationDialog(Schedule schedule) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this schedule?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DatabaseManager.deleteSchedule(schedule.getId());
            scheduleTable.getItems().remove(schedule);
        }
    }
}

//Comments