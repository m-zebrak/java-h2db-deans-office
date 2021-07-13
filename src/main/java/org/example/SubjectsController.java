package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class SubjectsController extends Controller {
    private ObservableList<ASubject> subjects = FXCollections.observableArrayList();

    @FXML
    private TextField subject_field;
    @FXML
    private TextField rate_field;
    @FXML
    private TableColumn<ASubject, String> subject_col;
    @FXML
    private TableColumn<ASubject, Integer> rate_col;

    @Override
    protected void clearFields() {
        super.clearFields();
        subject_field.setText("");
        rate_field.setText("");
    }

    private void createSubjectsList() {
        /**
         * A function that creates a list of all subjects from passed table name
         */
        subjects.clear();
        getListOfAllObjectsValues("subjects")
                .forEach(arr -> subjects.add(new ASubject((String[]) arr)));
    }

    private void fillTable() {
        /**
         * A function that fills up the entire table with values stored in the Student class
         */
        id_col.setCellValueFactory(new PropertyValueFactory("id"));
        index_number_col.setCellValueFactory(new PropertyValueFactory("index_number"));
        subject_col.setCellValueFactory(new PropertyValueFactory("subject"));
        rate_col.setCellValueFactory(new PropertyValueFactory("rate"));

        myTable.setItems(subjects);
    }

    private boolean validateFields(String index_number, String subject, String rate) {
        /**
         * A function that validates if all field was completed correctly
         * return: true if validation passed or else if not
         */
        boolean passed = true;

        if (!StringUtils.isNumeric(index_number) || Integer.parseInt(index_number) < 1) {
            index_number_field.setText("Enter Number > 0!");
            passed = false;
        }

        if (subject.equals("") || subject.equals("Enter subject!")) {
            subject_field.setText("Enter subject!");
            passed = false;
        }

        if (!StringUtils.isNumeric(rate) || Integer.parseInt(rate) < 1 || Integer.parseInt(rate) > 5) {
            rate_field.setText("Enter rate between 1-5!");
            passed = false;
        }

        return passed;
    }

    private boolean fieldsUnedited(String index_number, String subject, String rate) {
        /**
         * A function that checks if there were no changes in any of the field
         * return true if all fields are the same as before or false if fields were edited
         */
        if (index_number.equals(index_number_col.getCellData(index).toString()) && subject.equals(subject_col.getCellData(index)) && rate.equals(rate_col.getCellData(index).toString())) {
            return true;
        }

        return false;
    }

    public void rowSelected() {
        /**
         *  A function that gets all the data from the selected row and inserts them in all the fields
         */
        //sets index of selected row
        setIndex();
        //if someone fills up the fields on the left side and then click back on the same row - return and don't update the form
        if (!setId()) return;

        index_number_field.setText(index_number_col.getCellData(index).toString());
        subject_field.setText(subject_col.getCellData(index));
        rate_field.setText(rate_col.getCellData(index).toString());
    }

    public void initialize() {
        createSubjectsList();
        fillTable();
    }

    public void refreshButton() {
        initialize();
    }

    public void studentsButton() throws IOException {
        App.setRoot("students");
    }

    public void addButton() {
        /**
         * This function is called when the add button is pressed. Retrieves data from the fields on the left,
         * then validates them and sends a query if validations have been passed.
         */
        String index_number = index_number_field.getText();
        String subject = subject_field.getText();
        String rate = rate_field.getText();

        if (!validateFields(index_number, subject, rate)) return;
        if(!isIndexNumberPresent(index_number)) {
            System.out.println("NO STUDENT WITH A GIVEN INDEX NUMBER");
            index_number_field.setText("Index not found!");
            return;
        }

        String sql = String.format("INSERT INTO subjects VALUES (null, %s, \'%s\', %s);", index_number, subject,
                rate);
        QueryExecutor.executeUpdate(sql);
        initialize();
    }

    public void editButton() {
        /**
         * This function is called when the edit button is pressed. Retrieves data from the fields on the left only if
         * a row is selected, then validates them and sends a query if validations have been passed.
         */
        // if no one row is selected
        if (!setIndex()) return;

        String index_number = index_number_field.getText();
        String id = id_col.getCellData(index).toString();
        String subject = subject_field.getText();
        String rate = rate_field.getText();

        if (fieldsUnedited(index_number, subject, rate)) return;
        if (!validateFields(index_number, subject, rate)) return;
        if(!isIndexNumberPresent(index_number)) {
            System.out.println("NO STUDENT WITH A GIVEN INDEX NUMBER");
            index_number_field.setText("Index not found!");
            return;
        }

        String sql = String.format("UPDATE subjects SET index_number=%s, subject=\'%s\', rate=%s WHERE id=%s;",
                index_number, subject, rate, id);
        QueryExecutor.executeUpdate(sql);
        initialize();
    }

    public void removeButton() {
        /**
         * This function is called when the remove button is pressed. Retrieves data from the fields on the left only if
         * a row is selected, then sends a delete query.
         */
        if (!setIndex()) return;

        String id = id_col.getCellData(index).toString();

        String sql = String.format("DELETE FROM subjects WHERE id=%s", id);
        QueryExecutor.executeUpdate(sql);
        clearFields();
        initialize();
    }
}