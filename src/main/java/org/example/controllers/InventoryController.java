package org.example.controllers;


import javafx.collections.ObservableList;
import org.example.models.Tea;

import java.sql.*;

public class InventoryController {
    public void checkInventoryAndNotify(ObservableList<Tea> teaData) throws SQLException {
        for (Tea item : teaData) {
            if (item.getQuantity() < 50) {
                System.out.println("Пополните запас чая ID=" + item.getId() + " " + item.getName());
            }
        }
    }
}