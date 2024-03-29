package lk.ijse.ArtWoodLayered.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.ArtWoodLayered.bo.BOFactory;
import lk.ijse.ArtWoodLayered.bo.custom.EmployeeBO;
import lk.ijse.ArtWoodLayered.bo.custom.FinishedStockBO;
import lk.ijse.ArtWoodLayered.bo.custom.PendingStockBO;
import lk.ijse.ArtWoodLayered.bo.custom.WoodPiecesStockBO;
import lk.ijse.ArtWoodLayered.db.DbConnection;
import lk.ijse.ArtWoodLayered.dto.EmployeeDto;
import lk.ijse.ArtWoodLayered.dto.FinishedStockDto;
import lk.ijse.ArtWoodLayered.dto.PendingStockDto;
import lk.ijse.ArtWoodLayered.dto.WoodPiecesDto;
import lk.ijse.ArtWoodLayered.dto.tm.PendingStockTm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PendingStockController {
    @FXML
    private ComboBox<String> cmbEmpId;

    @FXML
    private ComboBox<String> cmbFinshedId;

    @FXML
    private ComboBox<String> cmbWoodId;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colIsFinished;

    @FXML
    private TableColumn<?, ?> colEmpId;

    @FXML
    private TableColumn<?, ?> colFinishedId;

    @FXML
    private TableColumn<?, ?> colPendingId;

    @FXML
    private TableColumn<?, ?> colWoodPieceId;

    @FXML
    private Label lblId;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<PendingStockTm> tblPending;

    PendingStockBO pendingStockBO = (PendingStockBO) BOFactory.getBoFactory().getBoo(BOFactory.BoTypes.PENDING_STOCK);
    EmployeeBO employeeBO = (EmployeeBO) BOFactory.getBoFactory().getBoo(BOFactory.BoTypes.EMPLOYEE);
    WoodPiecesStockBO woodPiecesStockBO = (WoodPiecesStockBO) BOFactory.getBoFactory().getBoo(BOFactory.BoTypes.WOOD_PIECES);
    FinishedStockBO finishedStockBO = (FinishedStockBO) BOFactory.getBoFactory().getBoo(BOFactory.BoTypes.FINISHED_STOCK);

    public void initialize() {
        setCellValueFactory();
        loadAllLogs();
        generateNextPendingId();
        loadEmpId();
        loadFinishedId();
        loadWoodId();
        setListener();
    }

    private void loadEmpId() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<EmployeeDto> list = employeeBO.getAllEmployeesForCombo();

            for (EmployeeDto dto : list) {
                obList.add(dto.getEmp_id());
            }

            cmbEmpId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFinishedId() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<FinishedStockDto> list = finishedStockBO.getAllFinishedStock();

            for (FinishedStockDto dto : list) {
                obList.add(dto.getFinished_id());
            }

            cmbFinshedId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadWoodId() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<WoodPiecesDto> list = woodPiecesStockBO.getAllWoodForCombo();

            for (WoodPiecesDto dto : list) {
                obList.add(dto.getWood_piece_id());
            }

            cmbWoodId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateNextPendingId() {
        try {
            String pendingId = pendingStockBO.generateNextPendingId();
            lblId.setText(pendingId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
        colPendingId.setCellValueFactory(new PropertyValueFactory<>("pending_id"));
        colFinishedId.setCellValueFactory(new PropertyValueFactory<>("finished_id"));
        colWoodPieceId.setCellValueFactory(new PropertyValueFactory<>("wood_piece_id"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
        colIsFinished.setCellValueFactory(new PropertyValueFactory<>("btn1"));
    }

    private void loadAllLogs() {
        try {
            List<PendingStockDto> dtoList = pendingStockBO.getAllPendings();

            ObservableList<PendingStockTm> obList = FXCollections.observableArrayList();

            for(PendingStockDto dto : dtoList){
                Button btn = new Button("Remove");
                btn.setCursor(Cursor.HAND);

                Button btn1 = new Button("Finished");
                btn1.setCursor(Cursor.HAND);

                btn.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

                    if (type.orElse(no) == yes){
                        int index = tblPending.getSelectionModel().getFocusedIndex();
                        String id = (String) colPendingId.getCellData(index);

                        deletePending(id);

                        obList.remove(index);
                        tblPending.refresh();
                    }

                });

                btn1.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to finished?", yes, no).showAndWait();

                    if (type.orElse(no) == yes){
                        int index = tblPending.getSelectionModel().getFocusedIndex();
                        String id = (String) colPendingId.getCellData(index);

                        try {
                            finishedPending(id);
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                            throw new RuntimeException(ex);
                        }

                        tblPending.refresh();
                    }

                });

                var tm = new PendingStockTm(dto.getPending_id(), dto.getEmp_id(), dto.getWood_piece_id(), dto.getFinished_id(), btn, btn1);

                obList.add(tm);

            }

            tblPending.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void finishedPending(String id) throws SQLException {
        String finished_id = cmbFinshedId.getValue();
        String emp_id = cmbEmpId.getValue();

        Connection connection = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pendingStockBO.finishedPending(id, finished_id, emp_id);
            deletePending(id);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            connection.rollback();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void deletePending(String id) {
        try {
            boolean isDeleted = pendingStockBO.deletePending(id);

            if(isDeleted) {
                tblPending.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "Pending item deleted!").show();
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
        lblId.setText("");
        cmbWoodId.setValue("");
        cmbFinshedId.setValue("");
        cmbEmpId.setValue("");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String pending_id = lblId.getText();
        String emp_id = cmbEmpId.getValue();
        String wood_piece_id = cmbWoodId.getValue();
        String finished_id = cmbFinshedId.getValue();

        var dto = new PendingStockDto(pending_id, emp_id, wood_piece_id, finished_id);

        Connection connection = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            boolean isSaved = pendingStockBO.save(dto, wood_piece_id, emp_id);

            if(isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "pending saved!").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            connection.rollback();
        }finally {
            connection.setAutoCommit(true);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String pending_id = lblId.getText();
        String emp_id = cmbEmpId.getValue();
        String wood_piece_id = cmbWoodId.getValue();
        String finished_id = cmbFinshedId.getValue();
        int amount = 1;

        var dto = new PendingStockDto(pending_id, amount, emp_id, wood_piece_id, finished_id);

        try {
            boolean isUpdated = pendingStockBO.updatePending(dto);

            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "pending updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setListener() {
        tblPending.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    var dto = new PendingStockDto(
                            newValue.getPending_id(),
                            newValue.getEmp_id(),
                            newValue.getWood_piece_id(),
                            newValue.getFinished_id()
                    );
                    setFields(dto);
                });
    }

    private void setFields(PendingStockDto dto) {
        lblId.setText(dto.getPending_id());
        cmbFinshedId.setValue(dto.getFinished_id());
        cmbWoodId.setValue(dto.getWood_piece_id());
        cmbEmpId.setValue(dto.getEmp_id());
    }

}
