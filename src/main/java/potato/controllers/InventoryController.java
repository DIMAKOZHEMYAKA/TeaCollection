package potato.controllers;


import javafx.collections.ObservableList;
import potato.models.Tea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryController {
    public void checkInventoryAndNotify(ObservableList<Tea> teaData) throws SQLException {
        for (Tea item : teaData) {
            if (item.getQuantity() < 50) {
                System.out.println("Пополните запас чая ID=" + item.getId() + " " + item.getName());
            }
        }
    }
}