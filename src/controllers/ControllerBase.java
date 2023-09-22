package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class ControllerBase {

  private Stage stage;
  private static Scene scene;
  protected String localizacaoArquivo;

  protected void setStage(Parent root, ActionEvent event) throws IOException {
    scene = new Scene(root, 640, 480);
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  protected static Parent loadFXML(String localizacaoArquivo, String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(ControllerBase.class.getResource(localizacaoArquivo + fxml + ".fxml"));
    return fxmlLoader.load();
  }
}
