package org.example.classes;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class Schedule {
    private int id;
    private SimpleStringProperty course;
    private SimpleStringProperty instructor;
    private SimpleStringProperty room;
    private SimpleObjectProperty<Date> date;

    public Schedule() {
        this.course = new SimpleStringProperty();
        this.instructor = new SimpleStringProperty();
        this.room = new SimpleStringProperty();
        this.date = new SimpleObjectProperty<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourse() {
        return course.get();
    }

    public SimpleStringProperty courseProperty() {
        return course;
    }

    public void setCourse(String course) {
        this.course.set(course);
    }

    public String getInstructor() {
        return instructor.get();
    }

    public SimpleStringProperty instructorProperty() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor.set(instructor);
    }

    public String getRoom() {
        return room.get();
    }

    public SimpleStringProperty roomProperty() {
        return room;
    }

    public void setRoom(String room) {
        this.room.set(room);
    }

    public Date getDate() {
        return date.get();
    }

    public SimpleObjectProperty<Date> dateProperty() {
        return date;
    }

    public void setDate(Date date) {
        this.date.set(date);
    }
}
