package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_vg;

    @FXML
    private Button btn_pf;

    @FXML
    private Button btn_rw;

    static void changeScene(Button nameButton, String nameScene) {
        nameButton.setOnAction(event -> {
            nameButton.getScene().getWindow().hide();
            openScene(nameScene);
        });
    }

    public static void openScene(String sceneName) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Controller.class.getResource("/sample/" + sceneName + ".fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Лабораторная работа №1");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void initialize() {
        changeScene(btn_rw,"railFence");
        changeScene(btn_vg,"vigenere");
        changeScene(btn_pf,"playfair");
    }
}
