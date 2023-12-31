package lk.ijse.ArtWoodLayered.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.ArtWoodLayered.bo.BOFactory;
import lk.ijse.ArtWoodLayered.bo.custom.LogStockBO;
import lk.ijse.ArtWoodLayered.bo.custom.WoodPiecesStockBO;
import lk.ijse.ArtWoodLayered.dto.LogsDto;
import lk.ijse.ArtWoodLayered.dto.WoodPiecesDto;
import lk.ijse.ArtWoodLayered.dto.tm.WoodPiecesTm;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class WoodPiecesStockController {
    @FXML
    private ComboBox<String> cmbLogId;

    @FXML
    private Label lblAmount;

    @FXML
    private Label lblQuality;

    @FXML
    private Label lblWoodId;

    @FXML
    private TextField txtLength;

    @FXML
    private TextField txtRadius;

    @FXML
    private TextField txtWeight;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colLogId;

    @FXML
    private TableColumn<?, ?> colQuality;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<WoodPiecesTm> tblWood;

    WoodPiecesStockBO woodPiecesStockBO = (WoodPiecesStockBO) BOFactory.getBoFactory().getBoo(BOFactory.BoTypes.WOOD_PIECES);
    LogStockBO logStockBO = (LogStockBO) BOFactory.getBoFactory().getBoo(BOFactory.BoTypes.LOGS);

    public void initialize() {
        setCellValueFactory();
        loadAllWood();
        generateNextWoodId();
        setListener();
        loadLogId();
    }

    private void loadLogId() {

        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<LogsDto> list = logStockBO.getAllLogsForCombo();

            for (LogsDto dto : list) {
                obList.add(dto.getLogs_id());
            }

            cmbLogId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateNextWoodId() {
        try {
            String orderId = woodPiecesStockBO.generateNextWoodId();
            lblWoodId.setText(orderId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("wood_piece_id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("wood_type"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("quality"));
        colLogId.setCellValueFactory(new PropertyValueFactory<>("logs_id"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    private void loadAllWood() {
        try {
            List<WoodPiecesDto> dtoList = woodPiecesStockBO.getAllWood();

            ObservableList<WoodPiecesTm> obList = FXCollections.observableArrayList();

            for(WoodPiecesDto dto : dtoList){
                Button btn = new Button("Remove");
                btn.setCursor(Cursor.HAND);

                btn.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

                    if (type.orElse(no) == yes){
                        int index = tblWood.getSelectionModel().getFocusedIndex();
                        String id = (String) colId.getCellData(index);

                        deleteWood(id);

                        obList.remove(index);
                        tblWood.refresh();
                    }

                });

                var tm = new WoodPiecesTm(dto.getWood_piece_id(), dto.getQuality(), dto.getAmount(), dto.getWood_type(), dto.getLogs_id(), btn);

                obList.add(tm);

            }

            tblWood.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteWood(String id) {
        try {
            boolean isDeleted = woodPiecesStockBO.deleteWood(id);

            if(isDeleted) {
                tblWood.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "wood piece deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) this.rootNode.getScene().getWindow();

        stage.close();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    void clearFields() {
        lblQuality.setText("");
        cmbLogId.setValue("");
        lblAmount.setText("");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = lblWoodId.getText();
        String quality = lblQuality.getText();
        int amount = Integer.parseInt(lblAmount.getText());
        String type = "";
        String log_id = cmbLogId.getValue();

        var dto = new WoodPiecesDto(id, quality, amount, type, log_id);

        try {
                boolean isSaved = woodPiecesStockBO.saveWood(dto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "wood saved!").show();
                    clearFields();

                }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private boolean validateWood() {

        String length = txtLength.getText();
        boolean isValidLength = Pattern.matches("([0-9]{1,})", length);

        if (!isValidLength){

            if (txtLength.getText().isEmpty()){
                flashBorder(txtLength);
                return false;
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid length").show();
                return false;
            }
        }

        String radius = txtRadius.getText();
        boolean isValidRadius = Pattern.matches("([0-9]{1,})", radius);

        if (!isValidRadius){

            if (txtRadius.getText().isEmpty()){
                flashBorder(txtRadius);
                return false;
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid radius").show();
                return false;
            }
        }

        String weight = txtWeight.getText();
        boolean isValidWeight = Pattern.matches("([0-9]{1,})", weight);

        if (!isValidWeight){

            if (txtWeight.getText().isEmpty()){
                flashBorder(txtWeight);
                return false;
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid Weight").show();
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
        String id = lblWoodId.getText();
        String quality = lblQuality.getText();
        int amount = Integer.parseInt(lblAmount.getText());
        String type = "";
        String log_id = cmbLogId.getValue();

        var dto = new WoodPiecesDto(id, quality, amount, type, log_id);

        try {
            boolean isUpdated = woodPiecesStockBO.updateWood(dto);
            System.out.println(isUpdated);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "wood updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnEnterOnAction(ActionEvent event) {
        if (validateWood()) {

            double length = Double.parseDouble(txtLength.getText());
            double radius = Double.parseDouble(txtRadius.getText());
            double weight = Double.parseDouble(txtWeight.getText());

            int size = 1;

            try {

                int amount = (int) ((2 * 3.14 * radius * (length + radius)) / size);
                String quality = "";

                if (weight > 30) {
                    quality = "High Quality";
                } else {
                    quality = "Low Quality";
                }

                lblAmount.setText(String.valueOf(amount));
                lblQuality.setText(quality);

            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

    private void setListener() {
        tblWood.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    var dto = new WoodPiecesDto(
                            newValue.getWood_piece_id(),
                            newValue.getQuality(),
                            newValue.getAmount(),
                            newValue.getWood_type(),
                            newValue.getLogs_id()
                    );
                    setFields(dto);
                });
    }

    private void setFields(WoodPiecesDto dto) {
        lblWoodId.setText(dto.getWood_piece_id());
        lblAmount.setText(String.valueOf(dto.getAmount()));
        lblQuality.setText(dto.getQuality());
        cmbLogId.setValue(dto.getLogs_id());
    }

}
