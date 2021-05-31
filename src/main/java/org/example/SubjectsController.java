package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectsController {
    private int index = -1;
    ObservableList<ASubject> subjects = FXCollections.observableArrayList();

    @FXML
    private TextField index_number_field;

    @FXML
    private TextField subject_field;

    @FXML
    private TextField rate_field;

    @FXML
    private TableView<ASubject> myTable;

    @FXML
    private TableColumn<ASubject, Integer> id_col;

    @FXML
    private TableColumn<ASubject, Integer> index_number_col;

    @FXML
    private TableColumn<ASubject, String> subject_col;

    @FXML
    private TableColumn<ASubject, Integer> rate_col;


    private ResultSet getAllSubjects() {
        String sql = "SELECT * FROM subjects;";
        return QueryExecutor.executeQuery(sql);
    }


    private void createSubjectsList() {
        ResultSet rs = getAllSubjects();
        subjects.clear();
        try {
            while(rs.next()) {
                ASubject subject = new ASubject(rs.getInt("id"),
                                              rs.getInt("index_number"),
                                              rs.getString("subject"),
                                              rs.getInt("rate"));
                subjects.add(subject);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void fillTable() {
        id_col.setCellValueFactory(new PropertyValueFactory<ASubject, Integer>("id"));
        index_number_col.setCellValueFactory(new PropertyValueFactory<ASubject, Integer>("index_number"));
        subject_col.setCellValueFactory(new PropertyValueFactory<ASubject, String>("subject"));
        rate_col.setCellValueFactory(new PropertyValueFactory<ASubject, Integer>("rate"));

        myTable.setItems(subjects);
    }


    public int getIndex() {
        return myTable.getSelectionModel().getSelectedIndex();
    }

    public void getSelected(MouseEvent mouseEvent) {
        index = getIndex();

        if (index <= -1) return;

        index_number_field.setText(index_number_col.getCellData(index).toString());
        subject_field.setText(subject_col.getCellData(index).toString());
        rate_field.setText(rate_col.getCellData(index).toString());
    }

    @FXML
    public void initialize() {
        createSubjectsList();
        fillTable();
    }


    @FXML
    public void refreshButton(ActionEvent event) throws IOException {
        initialize();
    }


    @FXML
    public void studentsButton(ActionEvent event) throws IOException {
        App.setRoot("students");
    }


    public void removeButton(ActionEvent event) {
    }

    public void editButton(ActionEvent event) {
    }

    public void addButton(ActionEvent event) {
    }
}
