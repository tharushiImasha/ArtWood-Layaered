package lk.ijse.ArtWoodLayered.controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.ArtWoodLayered.bo.BOFactory;
import lk.ijse.ArtWoodLayered.bo.custom.LoginBO;
import lk.ijse.ArtWoodLayered.dto.LoginDto;

import java.io.IOException;

public class LoginFormController {
    @FXML
    private AnchorPane rootNode;

    @FXML
    private PasswordField txtPw;

    @FXML
    private TextField txtPwShow;

    @FXML
    private TextField txtUserName;

    @FXML
    private JFXCheckBox chkHidePw;

    LoginBO loginBO = (LoginBO) BOFactory.getBoFactory().getBoo(BOFactory.BoTypes.LOGIN);

    public void initialize() {
        txtPwShow.setVisible(false);
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String user_name = txtUserName.getText();
        String pw = txtPw.getText();

        try {

            LoginDto dto = loginBO.login(user_name, pw);

            if (dto != null){

                if (dto.getJob_role().equals("owner")){
                    Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_pane.fxml"));

                    Scene scene = new Scene(rootNode);
                    Stage stage = (Stage) this.rootNode.getScene().getWindow();

                    stage.setTitle("Owner Dashboard");
                    stage.setScene(scene);
                    stage.centerOnScreen();

                }else if (dto.getJob_role().equals("stock_manager")){
                    Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/stock_manager_dashboard_pane.fxml"));

                    Scene scene = new Scene(rootNode);
                    Stage stage = (Stage) this.rootNode.getScene().getWindow();

                    stage.setTitle("Stock Manager Dashboard");
                    stage.setScene(scene);
                    stage.centerOnScreen();

                }else if (dto.getJob_role().equals("cashier")){
                    Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/cashier_dashboard_pane.fxml"));

                    Scene scene = new Scene(rootNode);
                    Stage stage = (Stage) this.rootNode.getScene().getWindow();

                    stage.setTitle("Cashier Dashboard");
                    stage.setScene(scene);
                    stage.centerOnScreen();
                }

            }else {
                new Alert(Alert.AlertType.INFORMATION, "Invalid Username or Password").show();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void lblForgotPwOnAction(MouseEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/forgot_pw.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    String password = "";

    @FXML
    void showPwOnAction(ActionEvent event) {
        if (chkHidePw.isSelected()){
            password = txtPw.getText();
            txtPwShow.setText(password);

            txtPw.setVisible(false);
            txtPwShow.setVisible(true);
        } else {
            password = txtPwShow.getText();
            txtPw.setText(password);

            txtPwShow.setVisible(false);
            txtPw.setVisible(true);
        }
    }

}
