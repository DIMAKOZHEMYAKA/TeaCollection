package potato;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import potato.dao.*;
import potato.controllers.ClassificationController;
import potato.controllers.InventoryController;
import potato.controllers.RecommendationController;
import potato.models.Tea;


import java.sql.*;
import java.util.*;

public class MainController {
    @FXML private Label typeResult;
    @FXML private TextField brewTimeField;
    @FXML private Label strengthResult;
    @FXML private Label inventoryResult;
    @FXML private ComboBox<Tea> teaSelector;
    @FXML private CheckBox preferenceStronger;
    @FXML private Label recommendationResult;
    @FXML private Button importSQL;
    @FXML private TableView<Tea> teaTable = new TableView<>();


    @FXML private TextField nameField;
    @FXML private TextField typeField;
    @FXML private TextField descriptionField;
    @FXML private TextField flavorField;
    @FXML private TextField quantityField;

    @FXML private ToggleGroup dataSourceGroup;
    @FXML private RadioButton jsonRadio;
    @FXML private RadioButton postgresRadio;
    @FXML private RadioButton restRadio;

    @FXML private TextField updateFieldName;
    @FXML private TextField updateFieldType;
    @FXML private TextField updateFieldDescription;
    @FXML private TextField updateFieldFlavor;
    @FXML private TextField updateFieldQuantity;


// Таблица
    private TeaService teaService;
    private Stage primaryStage;
    private ObservableList<Tea> teaData = FXCollections.observableArrayList();

    @FXML private TableColumn<Tea, Integer> idColumn = new TableColumn<>("ID");
    @FXML private TableColumn<Tea, String> nameColumn = new TableColumn<>("Название");
    @FXML private TableColumn<Tea, String> typeColumn = new TableColumn<>("Тип");
    @FXML private TableColumn<Tea, String> descriptionColumn = new TableColumn<>("Описание");
    @FXML private TableColumn<Tea, String> flavorColumn = new TableColumn<>("Вкус");
    @FXML private TableColumn<Tea, Integer> quantityColumn = new TableColumn<>("Количество");

    //Работа с базой
    @FXML
    private void initialize() {
//        TeaImporter teaImporter = new TeaImporter(new TeaSQL());
//        teaImporter.importFromJson("teaSQL.json");
//         Выбор источника данных по умолчанию
//         jsonRadio.setSelected(true);
//         Инициализация таблицы

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        flavorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFlavor_profile()));
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        teaTable.getColumns().add(idColumn);
        teaTable.getColumns().add(typeColumn);
        teaTable.getColumns().add(descriptionColumn);
        teaTable.getColumns().add(flavorColumn);
        teaTable.getColumns().add(quantityColumn);
        ToggleGroup group = new ToggleGroup();
        jsonRadio.setToggleGroup(group);
        postgresRadio.setToggleGroup(group);
        restRadio.setToggleGroup(group);

        switchToPostgres();

        // Слушатели для переключения источников данных
        jsonRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) switchToJson();
        });

        postgresRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) switchToPostgres();
        });

        restRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) switchToRest();
        });

        // Загрузка данных
        refreshTeaTable();
    }

    private void refreshTeaTable() {
        try {
            teaData.clear();
            teaData.addAll(teaService.getAllTeas());
            teaTable.setItems(teaData);
            teaSelector.setItems(teaData);
        } catch (Exception e) {
            showError("Error loading teas", e.getMessage());
        }
    }

    private void switchToJson() {
        teaService = new TeaService(new TeaJSON());
        refreshTeaTable();
    }

    private void switchToPostgres() {
        teaService = new TeaService(new TeaSQL());
        refreshTeaTable();
    }

    private void switchToRest() {
        teaService = new TeaService(new TeaAPI());
        refreshTeaTable();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void onClassify(ActionEvent event) {
        String desc = descriptionField.getText();
        String teaType = ClassificationController.classifyTeaType(desc);
        typeResult.setText("Определённый тип: " + teaType);
    }

    public void onCalculateStrength(ActionEvent event) {
        try {
            int brewTime = Integer.parseInt(brewTimeField.getText());
            String strength = ClassificationController.calculateStrength(brewTime);
            strengthResult.setText("Крепость: " + strength);
        } catch (NumberFormatException e) {
            strengthResult.setText("Введите число");
        }
    }

    public void onCheckInventory(ActionEvent event) throws SQLException {
        InventoryController ic = new InventoryController();
        ic.checkInventoryAndNotify(teaData);
        inventoryResult.setText("Проверка выполнена (смотрите консоль)");
    }

    public void onGetRecommendation(ActionEvent event) {
        Tea selectedTea = teaSelector.getValue();
        if (selectedTea == null) {
            recommendationResult.setText("Выберите чай");
            return;
        }

        Map<String, Object> prefs = Map.of("stronger", preferenceStronger.isSelected());
        Map<String, Object> rec = RecommendationController.recommendBrewing(selectedTea, prefs);
        recommendationResult.setText(
                "Рекомендуемая температура: " + rec.get("temperature") + "°C, " +
                        "Время заваривания: " + rec.get("time_seconds") + " сек."
        );
    }

    @FXML
    private void handleRefresh() {
        refreshTeaTable();
    }

    @FXML
    private void handleAddTea() {
        try {
            Tea tea = new Tea();
            tea.setName(nameField.getText());
            tea.setType((typeField.getText()));
            tea.setDescription(descriptionField.getText());
            tea.setFlavor_profile(flavorField.getText());
            tea.setQuantity(Integer.parseInt(quantityField.getText()));
            teaService.addTea(tea);
            refreshTeaTable();
            clearFields();
        } catch (Exception e) {
            showError("Error adding tea", e.getMessage());
        }
    }

    @FXML
    private void handleUpdateTea() {
        Tea selectedTea = teaTable.getSelectionModel().getSelectedItem();

        if (selectedTea != null) {
            try {
                selectedTea.setName(updateFieldName.getText());
                selectedTea.setType(updateFieldType.getText());
                selectedTea.setDescription(updateFieldDescription.getText());
                selectedTea.setFlavor_profile(updateFieldFlavor.getText());
                selectedTea.setQuantity(Integer.parseInt(updateFieldQuantity.getText()));
                teaService.updateTea(selectedTea);
                refreshTeaTable();
            } catch (Exception e) {
                showError("Error updating tea", e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteTea() {
        Tea selectedTea = teaTable.getSelectionModel().getSelectedItem();
        if (selectedTea != null) {
            try {
                teaService.deleteTea(selectedTea.getId());
                refreshTeaTable();
                clearFields();
            } catch (Exception e) {
                showError("Error deleting tea", e.getMessage());
            }
        }
    }

    @FXML
    private void onClickTableView(MouseEvent event){
        updateFieldName.setText(teaTable.getSelectionModel().getSelectedItem().getName());
        updateFieldType.setText(teaTable.getSelectionModel().getSelectedItem().getType());
        updateFieldDescription.setText(teaTable.getSelectionModel().getSelectedItem().getDescription());
        updateFieldFlavor.setText(teaTable.getSelectionModel().getSelectedItem().getFlavor_profile());
        updateFieldQuantity.setText(String.valueOf(teaTable.getSelectionModel().getSelectedItem().getQuantity()));
    }


    private void clearFields() {
        nameField.clear();
        typeField.clear();
        descriptionField.clear();
        flavorField.clear();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
