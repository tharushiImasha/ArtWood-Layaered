package lk.ijse.ArtWoodLayered.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import lk.ijse.ArtWoodLayered.bo.BOFactory;
import lk.ijse.ArtWoodLayered.bo.custom.SupplierBO;
import lk.ijse.ArtWoodLayered.dto.SupplierDto;
import lk.ijse.ArtWoodLayered.dto.tm.SupplierTm;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class OwnerSupplierController {
    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableView<SupplierTm> tblSupplier;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    SupplierBO supplierBO = (SupplierBO) BOFactory.getBoFactory().getBoo(BOFactory.BoTypes.SUPPLIER);

    public void initialize() {
        setCellValueFactory();
        loadAllSuppliers();
        setListener();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("sup_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("sup_name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadAllSuppliers() {
        ObservableList<SupplierTm> obList = FXCollections.observableArrayList();

        try {
            List<SupplierDto> dtoList;
            dtoList = supplierBO.getAllSuppliers();

            for(SupplierDto dto : dtoList) {
                obList.add(new SupplierTm(dto.getId(), dto.getName(), dto.getAddress(), dto.getEmail()));
            }

            tblSupplier.setItems(obList);
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
        txtAddress.setText("");
        txtEmail.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            boolean isDeleted = supplierBO.deleteSupplier(id);

            if(isDeleted) {
                tblSupplier.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();

        var dto = new SupplierDto(id, name, address, email);

        try {
            if(validateSupplier()) {

                boolean isSaved = supplierBO.saveSupplier(dto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplier saved!").show();
                    clearFields();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private boolean validateSupplier() {
        String id = txtId.getText();
        boolean isValid = Pattern.matches("[S][0-9]{1,}", id);

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

        String address = txtAddress.getText();
        boolean isValidAddress = Pattern.matches("([a-zA-Z0-9\\s]+)", address);

        if (!isValidAddress){

            if (txtAddress.getText().isEmpty()){
                flashBorder(txtAddress);
                return false;
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid Address").show();
                return false;
            }
        }

        String email = txtEmail.getText();
        boolean isValidJob = Pattern.matches("(^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$)", email);

        if (!isValidJob){

            if (txtEmail.getText().isEmpty()){
                flashBorder(txtEmail);
                return false;
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid Email").show();
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
        String address = txtAddress.getText();
        String email = txtEmail.getText();

        var dto = new SupplierDto(id, name, address, email);

        try {
            boolean isUpdated = supplierBO.updateSupplier(dto);
            System.out.println(isUpdated);

            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtIdOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            SupplierDto dto = supplierBO.searchSupplier(id);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "supplier not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void fillFields(SupplierDto dto) {
        txtId.setText(dto.getId());
        txtName.setText(dto.getName());
        txtAddress.setText(dto.getAddress());
        txtEmail.setText(dto.getEmail());
    }

    private void setListener() {
        tblSupplier.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    var dto = new SupplierDto(
                            newValue.getSup_id(),
                            newValue.getSup_name(),
                            newValue.getAddress(),
                            newValue.getEmail()
                    );
                    setFields(dto);
                });
    }

    private void setFields(SupplierDto dto) {
        txtId.setText(dto.getId());
        txtName.setText(dto.getName());
        txtAddress.setText(dto.getAddress());
        txtEmail.setText(dto.getEmail());
    }

}
