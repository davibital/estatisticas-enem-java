package edu.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;

public abstract class ControllerBase {

  private Stage stage;
  private static Scene scene;
  protected String localizacaoArquivo;

  protected void setStage(Parent root, ActionEvent event) throws IOException {
    scene = new Scene(root, 700, 450);
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.setMinWidth(640);
    stage.setMinWidth(400);
    stage.show();
  }

  protected static Parent loadFXML(String localizacaoArquivo, String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(ControllerBase.class.getResource(localizacaoArquivo + fxml + ".fxml"));
    return fxmlLoader.load();
  }

  protected void configurarMenu(MenuButton menu) {
    menu.getItems()
        .stream()
        .forEach(item -> 
          item.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
              menu.setText(item.getText());
            }
          })
        );
  }
}
