package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    protected int index;
    protected int id = -1;

    @FXML
    protected TextField index_number_field;

    @FXML
    protected TableColumn<Object, Integer> id_col;

    @FXML
    protected TableColumn<Object, Integer> index_number_col;

    @FXML
    protected TableView myTable;

    protected boolean setIndex() {
        int index = myTable.getSelectionModel().getSelectedIndex();

        if (index < 0) return false;
        else {
            this.index = index;
            return true;
        }
    }

    protected boolean setId() {
        int id = id_col.getCellData(index);

        if (this.id == id) return false;
        else {
            this.id = id;
            return true;
        }
    }

    protected ResultSet getAll(String table) {
        String sql = "SELECT * FROM " + table + ";";
        return QueryExecutor.executeQuery(sql);
    }

    protected ArrayList getListOfAllObjectsValues(String table) {
        /**
         * This function gets list of all objects where every element in list is an array, which contains information about objects values
         * return: List of arrays
         */
        ResultSet resultSet = getAll(table);
        ArrayList<String[]> objects = new ArrayList();

        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int numOfColumns = resultSetMetaData.getColumnCount();


            while (resultSet.next()) {
                String[] objectValues = new String[numOfColumns];

                for (int i = 1; i <= numOfColumns; ++i) {
                    if (resultSetMetaData.getColumnTypeName(i).equals("INTEGER")) {
                        int val = resultSet.getInt(i);
                        objectValues[i - 1] = Integer.toString(val);
                    } else if (resultSetMetaData.getColumnTypeName(i).equals("VARCHAR")) {
                        String val = resultSet.getString(i);
                        objectValues[i - 1] = val;
                    }

                }
                objects.add(objectValues);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return objects;
    }


}