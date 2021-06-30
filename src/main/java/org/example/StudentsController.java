package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class StudentsController extends Controller {
    ObservableList<Student> students = FXCollections.observableArrayList();

    @FXML
    private TextField name_field;
    @FXML
    private TextField surname_field;
    @FXML
    private TableColumn<Student, String> name_col;
    @FXML
    private TableColumn<Student, String> surname_col;


    private void createStudentsList() {
        students.clear();
        getListOfAllObjectsValues("students")
                .forEach(arr -> students.add(new Student((String[]) arr)));
    }


    public void fillTable() {
        id_col.setCellValueFactory(new PropertyValueFactory("id"));
        index_number_col.setCellValueFactory(new PropertyValueFactory("index_number"));
        name_col.setCellValueFactory(new PropertyValueFactory("name"));
        surname_col.setCellValueFactory(new PropertyValueFactory("surname"));

        myTable.setItems(students);
    }


    public boolean validateFields(String index_number, String name, String surname) {
        boolean passed = true;

        if (!StringUtils.isNumeric(index_number) || Integer.parseInt(index_number) < 1) {
            index_number_field.setText("Enter Number > 0!");
            passed = false;
        }

        if (name.equals("") || name.equals("Enter name!")) {
            name_field.setText("Enter name!");
            passed = false;
        }

        if (surname.equals("") || surname.equals("Enter surname!")) {
            surname_field.setText("Enter surname!");
            passed = false;
        }

        return passed;
    }


    private boolean fieldsUnedited(String index_number, String name, String surname) {
        if (index_number.equals(index_number_col.getCellData(index).toString()) && name.equals(name_col.getCellData(index)) && surname.equals(surname_col.getCellData(index))) {
            return true;
        }

        return false;
    }


    public void rowSelected() {
        //sets index of selected row
        setIndex();
        //if someone fills up the fields on the left side and then click back on the same row - return and don't update the form
        if (!setId()) return;

        index_number_field.setText(index_number_col.getCellData(index).toString());
        name_field.setText(name_col.getCellData(index));
        surname_field.setText(surname_col.getCellData(index));
    }


    public void initialize() {
        createStudentsList();
        fillTable();
    }


    @FXML
    public void refreshButton() {
        initialize();
    }


    @FXML
    public void subjectsButton() throws IOException {
        App.setRoot("subjects");
    }


    /**
     * This function is called when the add button is pressed. Retrieves data from the fields on the left,
     * then validates them and sends a query if validations have been passed.
     */
    @FXML
    public void addButton() {
        String index_number = index_number_field.getText();
        String name = name_field.getText();
        String surname = surname_field.getText();

        if (!validateFields(index_number, name, surname)) return;

        String sql = String.format("INSERT INTO students VALUES (null, %s, \'%s\', \'%s\');", index_number, name, surname);
        QueryExecutor.executeUpdate(sql);
        initialize();
    }


    /**
     * This function is called when the edit button is pressed. Retrieves data from the fields on the left only if
     * a row is selected, then validates them and sends a query if validations have been passed.
     */
    @FXML
    public void editButton() {
        //if no one row is selected
        if (!setIndex()) return;

        String index_number = index_number_field.getText();
        String index_number_old = index_number_col.getCellData(index).toString();
        String name = name_field.getText();
        String surname = surname_field.getText();

        if (fieldsUnedited(index_number, name, surname)) return;
        if (!validateFields(index_number, name, surname)) return;

        String sql = String.format("UPDATE students SET index_number=%s, name=\'%s\', surname=\'%s\' WHERE index_number=%s;",
                index_number, name, surname, index_number_old);
        System.out.println(sql);
        QueryExecutor.executeUpdate(sql);
        initialize();
    }


    /**
     * This function is called when the remove button is pressed. Retrieves data from the fields on the left only if
     * a row is selected, then sends a delete query.
     */
    @FXML
    public void removeButton() {
        if (!setIndex()) return;

        String index_number = index_number_col.getCellData(index).toString();

        String sql1 = String.format("DELETE FROM students WHERE index_number=%s", index_number);
        String sql2 = String.format("DELETE FROM subjects WHERE index_number=%s", index_number);

        QueryExecutor.executeUpdate(sql1);
        QueryExecutor.executeUpdate(sql2);
        initialize();
    }
}