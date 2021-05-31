package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentsController {
    private int index = -1;
    ObservableList<Student> students = FXCollections.observableArrayList();


    @FXML
    private TextField index_number_field;

    @FXML
    private TextField name_field;

    @FXML
    private TextField surname_field;

    @FXML
    private TableView<Student> myTable;

    @FXML
    private TableColumn<Student, Integer> id_col;

    @FXML
    private TableColumn<Student, Integer> index_number_col;

    @FXML
    private TableColumn<Student, String> name_col;

    @FXML
    private TableColumn<Student, String> surname_col;




    private ResultSet getAllStudents() {
        String sql = "SELECT * FROM students;";
        return QueryExecutor.executeQuery(sql);
    }


    private void createStudentsList() {
        ResultSet rs = getAllStudents();
        students.clear();

        try {
            while(rs.next()) {
                Student student = new Student(rs.getInt("id"),
                                              rs.getInt("index_number"),
                                              rs.getString("name"),
                                              rs.getString("surname"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void fillTable() {
        id_col.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        index_number_col.setCellValueFactory(new PropertyValueFactory<Student, Integer>("index_number"));
        name_col.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        surname_col.setCellValueFactory(new PropertyValueFactory<Student, String>("surname"));

        myTable.setItems(students);
    }


    public int getIndex() {
        return myTable.getSelectionModel().getSelectedIndex();
    }


    public boolean validateForm(String index_number, String name, String surname) {
        if (!StringUtils.isNumeric(index_number) || Integer.parseInt(index_number) <= 0) {
            index_number_field.setText("ENTER NUMBER >0");
            return false;
        }

        if (name.equals("") || surname.equals("") || name.equals("name") || surname.equals("surname")){
            return false;
        }

        return true;
    }


    public void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        index = getIndex();

        if (index <= -1) return;

        index_number_field.setText(index_number_col.getCellData(index).toString());
        name_field.setText(name_col.getCellData(index).toString());
        surname_field.setText(surname_col.getCellData(index).toString());
    }


    @FXML
    public void initialize() {
        createStudentsList();
        fillTable();
    }



    @FXML
    public void refreshButton(ActionEvent event) throws IOException {
        initialize();
    }

    @FXML
    public void subjectsButton(ActionEvent event) throws IOException {
        App.setRoot("subjects");
    }

    @FXML
    public void addButton(ActionEvent event) throws IOException {
        String index_number = index_number_field.getText().toString();
        String name = name_field.getText().toString();
        String surname = surname_field.getText().toString();

        if(!validateForm(index_number, name, surname)) return;

        String sql = String.format("INSERT INTO students VALUES (null, %s, \"%s\", \"%s\");", index_number, name, surname);
        QueryExecutor.execute(sql);
        initialize();
    }

    @FXML
    public void editButton(ActionEvent event) throws IOException {
        index = getIndex();

        if (index <= -1) return;

        String index_number = index_number_field.getText().toString();
        String index_number_old = index_number_col.getCellData(index).toString();
        String name = name_field.getText().toString();
        String surname = surname_field.getText().toString();

        if(!validateForm(index_number, name, surname)) return;

        String sql = String.format("UPDATE students SET index_number=%s, name=\"%s\", surname=\"%s\" WHERE index_number=%s;",
                                    index_number, name, surname,index_number_old);
        System.out.println(sql);
        QueryExecutor.execute(sql);
        initialize();
    }

    @FXML
    public void removeButton(ActionEvent event) throws IOException {
        index = getIndex();

        if (index <= -1) return;

        String index_number = index_number_col.getCellData(index).toString();

        String sql = String.format("DELETE FROM students WHERE index_number=%s",index_number);
        QueryExecutor.execute(sql);
        initialize();
    }


}
