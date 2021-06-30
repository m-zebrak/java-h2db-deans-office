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
    ObservableList<ASubject> subjects = FXCollections.observableArrayList();

    @FXML
    private TextField subject_field;
    @FXML
    private TextField rate_field;
    @FXML
    private TableColumn<ASubject, String> subject_col;
    @FXML
    private TableColumn<ASubject, Integer> rate_col;


    private void createSubjectsList() {
        subjects.clear();
        getListOfAllObjectsValues("subjects")
                .forEach(arr -> subjects.add(new ASubject((String[]) arr)));
    }


    public void fillTable() {
        id_col.setCellValueFactory(new PropertyValueFactory("id"));
        index_number_col.setCellValueFactory(new PropertyValueFactory("index_number"));
        subject_col.setCellValueFactory(new PropertyValueFactory("subject"));
        rate_col.setCellValueFactory(new PropertyValueFactory("rate"));

        myTable.setItems(subjects);
    }


    public boolean validateFields(String index_number, String subject, String rate) {
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
        if (index_number.equals(index_number_col.getCellData(index).toString()) && subject.equals(subject_col.getCellData(index)) && rate.equals(rate_col.getCellData(index).toString())) {
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
        subject_field.setText(subject_col.getCellData(index));
        rate_field.setText(rate_col.getCellData(index).toString());
    }


    public void initialize() {
        createSubjectsList();
        fillTable();
    }


    @FXML
    public void refreshButton() {
        initialize();
    }


    @FXML
    public void studentsButton() throws IOException {
        App.setRoot("students");
    }


    /**
     * This function is called when the add button is pressed. Retrieves data from the fields on the left,
     * then validates them and sends a query if validations have been passed.
     */
    public void addButton() {
        //TODO: check czy wprowadzony index jest w studentach
        String index_number = index_number_field.getText();
        String subject = subject_field.getText();
        String rate = rate_field.getText();

        if (!validateFields(index_number, subject, rate)) return;

        String sql = String.format("INSERT INTO subjects VALUES (null, %s, \'%s\', %s);", index_number, subject,
                rate);
        QueryExecutor.executeUpdate(sql);
        initialize();
    }


    /**
     * This function is called when the edit button is pressed. Retrieves data from the fields on the left only if
     * a row is selected, then validates them and sends a query if validations have been passed.
     */
    public void editButton() {
        //TODO: check czy wprowadzony index jest w studentach
        //if no one row is selected
        if (!setIndex()) return;

        String index_number = index_number_field.getText();
        String id = id_col.getCellData(index).toString();
        String subject = subject_field.getText();
        String rate = rate_field.getText();

        if (!validateFields(index_number, subject, rate)) return;

        String sql = String.format("UPDATE subjects SET index_number=%s, subject=\'%s\', rate=%s WHERE id=%s;",
                index_number, subject, rate, id);
        System.out.println(sql);
        QueryExecutor.executeUpdate(sql);
        initialize();
    }


    /**
     * This function is called when the remove button is pressed. Retrieves data from the fields on the left only if
     * a row is selected, then sends a delete query.
     */
    public void removeButton() {
        if (!setIndex()) return;

        String id = id_col.getCellData(index).toString();

        String sql = String.format("DELETE FROM subjects WHERE id=%s", id);
        QueryExecutor.executeUpdate(sql);
        initialize();
    }
}
