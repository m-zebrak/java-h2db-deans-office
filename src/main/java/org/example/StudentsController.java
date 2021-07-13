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
    protected static  ObservableList<Student> students = FXCollections.observableArrayList();

    @FXML
    private TextField name_field;
    @FXML
    private TextField surname_field;
    @FXML
    private TableColumn<Student, String> name_col;
    @FXML
    private TableColumn<Student, String> surname_col;

    @Override
    protected void clearFields() {
        super.clearFields();
        name_field.setText("");
        surname_field.setText("");
    }

    private void createStudentsList() {
        /**
         * A function that creates a list of all students from passed table name
         */
        students.clear();
        getListOfAllObjectsValues("students")
                .forEach(arr -> students.add(new Student((String[]) arr)));
    }

    private void fillTable() {
        /**
         * A function that fills up the entire table with values stored in the Student class
         */
        id_col.setCellValueFactory(new PropertyValueFactory("id"));
        index_number_col.setCellValueFactory(new PropertyValueFactory("index_number"));
        name_col.setCellValueFactory(new PropertyValueFactory("name"));
        surname_col.setCellValueFactory(new PropertyValueFactory("surname"));

        myTable.setItems(students);
    }

    private boolean validateFields(String index_number, String name, String surname) {
        /**
         * A function that validates if all field was completed correctly
         * return: true if validation passed or else if not
         */
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
        /**
         * A function that checks if there were no changes in any of the field
         * return true if all fields are the same as before or false if fields were edited
         */
        if (index_number.equals(index_number_col.getCellData(index).toString()) && name.equals(name_col.getCellData(index)) && surname.equals(surname_col.getCellData(index)))
            return true;

        return false;
    }

    public void rowSelected() {
        /**
         *  A function that gets all the data from the selected row and inserts them in all the fields
         */
        // sets index of selected row
        setIndex();
        // if someone fills up the fields on the left side and then click back on the same row - return and don't
        // update the form fields
        if (!setId()) return;

        index_number_field.setText(index_number_col.getCellData(index).toString());
        name_field.setText(name_col.getCellData(index));
        surname_field.setText(surname_col.getCellData(index));
    }

    public void initialize() {
        createStudentsList();
        fillTable();
    }

    @Override
    public void refreshButton() {
        /**
         * Clears all fields of the form and calls initialize()
         */
        super.refreshButton();
        initialize();
    }

    public void subjectsButton() throws IOException {
        App.setRoot("subjects");
    }

    public void addButton() {
        /**
         * This function is called when the add button is pressed. Retrieves data from the fields on the left,
         * then validates them and sends a query if validations have been passed.
         */
        String index_number = index_number_field.getText();
        String name = name_field.getText();
        String surname = surname_field.getText();

        if (!validateFields(index_number, name, surname)) return;
        if(isIndexNumberPresent(index_number)) {
            System.out.println("STUDENT WITH A GIVEN INDEX NUMBER ALREADY OCCURS");
            index_number_field.setText("Index duplicate!");
            return;
        }

        String sql = String.format("INSERT INTO students VALUES (null, %s, \'%s\', \'%s\');", index_number, name, surname);

        QueryExecutor.executeUpdate(sql);
        refreshButton();
    }

    public void editButton() {
        /**
         * This function is called when the edit button is pressed. Retrieves data from the fields on the left only if
         * a row is selected, then validates them and sends a query if validations have been passed.
         */
        //if no one row is selected
        if (!setIndex()) return;

        String index_number = index_number_field.getText();
        String index_number_old = index_number_col.getCellData(index).toString();
        String name = name_field.getText();
        String surname = surname_field.getText();

        if (fieldsUnedited(index_number, name, surname)) return;
        if (!validateFields(index_number, name, surname)) return;
        if(isIndexNumberPresent(index_number) && !index_number.equals(index_number_old)) {
            System.out.println("STUDENT WITH A GIVEN INDEX NUMBER ALREADY OCCURS");
            index_number_field.setText("Index duplicate!");
            return;
        }

        String sql = String.format("UPDATE students SET index_number=%s, name=\'%s\', surname=\'%s\' WHERE index_number=%s;",
                index_number, name, surname, index_number_old);

        QueryExecutor.executeUpdate(sql);
        refreshButton();
    }

    public void removeButton() {
        /**
         * This function is called when the remove button is pressed. Retrieves data from the fields on the left only if
         * a row is selected, then sends a delete query.
         */
        if (!setIndex()) return;

        String index_number = index_number_col.getCellData(index).toString();

        String sql1 = String.format("DELETE FROM subjects WHERE index_number=%s", index_number);
        String sql2 = String.format("DELETE FROM students WHERE index_number=%s", index_number);

        QueryExecutor.executeUpdate(sql1);
        QueryExecutor.executeUpdate(sql2);
        refreshButton();
    }
}