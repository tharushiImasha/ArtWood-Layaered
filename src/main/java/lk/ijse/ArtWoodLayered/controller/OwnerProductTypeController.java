package lk.ijse.ArtWoodLayered.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import lk.ijse.ArtWoodLayered.bo.BOFactory;
import lk.ijse.ArtWoodLayered.bo.custom.ProductTypeBO;
import lk.ijse.ArtWoodLayered.dto.ProductTypeDto;
import lk.ijse.ArtWoodLayered.dto.tm.ProductTypeTm;


import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class OwnerProductTypeController {
    @FXML
    private ComboBox<String> cmbQuality;

    @FXML
    private ComboBox<String>  cmbWoodType;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQuality;

    @FXML
    private TableView<ProductTypeTm> tblProduct;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    ProductTypeBO productTypeBO = (ProductTypeBO) BOFactory.getBoFactory().getBoo(BOFactory.BoTypes.PRODUCT_TYPE);

    public void initialize() {
        setCellValueFactory();
        loadAllProducts();
        setListener();
        loadType();
        loadQuality();
    }

    private void loadType() {
        cmbWoodType.getItems().add("Teak");
        cmbWoodType.getItems().add("Rosewood");
    }

    private void loadQuality() {
        cmbQuality.getItems().add("High Quality");
        cmbQuality.getItems().add("Low Quality");
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("quality"));
        colType.setCellValueFactory(new PropertyValueFactory<>("wood_type"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void loadAllProducts() {

        ObservableList<ProductTypeTm> obList = FXCollections.observableArrayList();

        try {
            List<ProductTypeDto> dtoList;
            dtoList = productTypeBO.getAllProduct();

            for(ProductTypeDto dto : dtoList) {
                obList.add(new ProductTypeTm(dto.getProduct_id(), dto.getProduct_name(), dto.getQuality(), dto.getWood_type(), dto.getPrice()));
            }

            tblProduct.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        cmbQuality.setValue("");
        cmbWoodType.setValue("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            boolean isDeleted = productTypeBO.deleteProduct(id);

            if(isDeleted) {
                tblProduct.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "product deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String quality = cmbQuality.getValue();
        String wood_type = cmbWoodType.getValue();
        double price = Double.parseDouble(txtPrice.getText());

        var dto = new ProductTypeDto(id, name, quality, wood_type, price);

        try {
            if (validateProduct()) {

                boolean isSaved = productTypeBO.saveProduct(dto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "product saved!").show();
                    clearFields();
                }else {
                    new Alert(Alert.AlertType.CONFIRMATION, "product not saved!").show();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private boolean validateProduct() {
        String id = txtId.getText();
        boolean isValid = Pattern.matches("[P][0-9]{1,}", id);

        if (!isValid){

            if (txtId.getText().isEmpty()){
                flashBorder(txtId);
                return false;
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid ID").show();
                return false;
            }
        }

        String name = txtName.getText();
        boolean isValidName = Pattern.matches("([a-zA-Z\\s]+)", name);

        if (!isValidName){

            if (txtName.getText().isEmpty()){
                flashBorder(txtName);
                return false;
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid Name").show();
                return false;
            }
        }

        String price = txtPrice.getText();
        boolean isValidPrice = Pattern.matches("([0-9]{3,})", price);

        if (!isValidPrice){

            if (txtPrice.getText().isEmpty()){
                flashBorder(txtPrice);
                return false;
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid Price").show();
                return false;
            }
        }

        return true;
    }

    private void flashBorder(TextField textField) {
        textField.setStyle("-fx-border-color: #000000;-fx-background-color: rgba(255,0,0,0.13)");
        setBorderResetAnimation(textField);
    }

    private void setBorderResetAnimation(TextField textField) {

        Timeline timeline1 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(textField.styleProperty(), "-fx-background-color:rgba(255,0,0,0.13);-fx-border-color: rgba(128,128,128,0.38);-fx-background-radius:10;-fx-border-radius:10")),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(textField.styleProperty(), "-fx-background-color: white;-fx-border-color: rgba(128,128,128,0.38);-fx-background-radius:10;-fx-border-radius:10")),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(textField.styleProperty(), "-fx-background-color:rgba(255,0,0,0.13);-fx-border-color: rgba(128,128,128,0.38);-fx-background-radius:10;-fx-border-radius:10")),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(textField.styleProperty(), "-fx-background-color: white;-fx-border-color: rgba(128,128,128,0.38);-fx-background-radius:10;-fx-border-radius:10")),
                new KeyFrame(Duration.seconds(0.4), new KeyValue(textField.styleProperty(), "-fx-background-color:rgba(255,0,0,0.13);-fx-border-color: rgba(128,128,128,0.38);-fx-background-radius:10;-fx-border-radius:10")),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(textField.styleProperty(), "-fx-background-color: white;-fx-border-color: rgba(128,128,128,0.38);-fx-background-radius:10;-fx-border-radius:10"))
        );
        timeline1.play();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String quality = cmbQuality.getValue();
        String wood_type = cmbWoodType.getValue();
        double price = Double.parseDouble(txtPrice.getText());

        var dto = new ProductTypeDto(id, name, quality, wood_type, price);

        try {
            boolean isUpdated = productTypeBO.updateProduct(dto);
            System.out.println(isUpdated);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Product updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtIdOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            ProductTypeDto dto = productTypeBO.searchProduct(id);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Product not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void fillFields(ProductTypeDto dto) {
        txtId.setText(dto.getProduct_id());
        txtName.setText(dto.getProduct_name());
        cmbQuality.setValue(dto.getQuality());
        cmbWoodType.setValue(dto.getWood_type());
        txtPrice.setText(String.valueOf(dto.getPrice()));
    }

    private void setListener() {
        tblProduct.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    var dto = new ProductTypeDto(
                            newValue.getProduct_id(),
                            newValue.getProduct_name(),
                            newValue.getQuality(),
                            newValue.getWood_type(),
                            newValue.getPrice()
                    );
                    setFields(dto);
                });
    }

    private void setFields(ProductTypeDto dto) {

        txtId.setText(dto.getProduct_id());
        txtName.setText(dto.getProduct_name());
        cmbQuality.setValue(dto.getQuality());
        txtPrice.setText(String.valueOf(dto.getPrice()));
        cmbWoodType.setValue(dto.getWood_type());
    }

}
