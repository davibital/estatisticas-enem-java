package edu.controllers;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public abstract class ControllerBase {

  @FXML
  private ToolBar barraAnalise;

  @FXML
  private MenuItem menuItemEnemAntigo;

  @FXML
  private MenuItem menuItemEnemNovo;

  @FXML
  private MenuButton menuModeloEnem;

  @FXML
  private MenuButton menuPeriodoFinal;

  @FXML
  private MenuButton menuPeriodoInicial;

  @FXML
  private BorderPane painelGrafico;

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

  protected void voltarAoInicioBase(ActionEvent event) throws IOException {
    localizacaoArquivo = "../views/";
    setStage(loadFXML(localizacaoArquivo, "tela_inicial"), event);
  }

  protected void iniciarAnaliseBase(ActionEvent event) {
    int anoPeriodoInicial;
    int anoPeriodoFinal;
    if (event.getSource().equals(menuItemEnemAntigo)) {
      menuModeloEnem.setText(menuItemEnemAntigo.getText());
      anoPeriodoInicial = 1998;
      anoPeriodoFinal = 2008;
    } else {
      menuModeloEnem.setText(menuItemEnemNovo.getText());
      anoPeriodoInicial = 2009;
      anoPeriodoFinal = 2022;
    }

    menuPeriodoInicial.getItems().clear();
    menuPeriodoInicial.setText("Início");
    menuPeriodoFinal.getItems().clear();
    menuPeriodoFinal.setText("Fim");

    for (int ano = anoPeriodoInicial; ano <= anoPeriodoFinal; ano++) {
      MenuItem itemAnoInicial = new MenuItem("" + ano);
      MenuItem itemAnoFinal = new MenuItem("" + ano);
      menuPeriodoInicial.getItems().add(itemAnoInicial);
      menuPeriodoFinal.getItems().add(itemAnoFinal);
    }

    barraAnalise.setVisible(true);
    painelGrafico.setVisible(true);

    configurarMenu(menuPeriodoInicial);
    configurarMenu(menuPeriodoFinal);
  }

  protected boolean periodoValido(Integer periodoInicial, Integer periodoFinal) {
    try {
      int diferencaPeriodo = periodoFinal - periodoInicial;
      if (diferencaPeriodo < 0)
        throw new Exception("O período inicial não pode ser maior que o período final");
      else if (diferencaPeriodo == 0)
        throw new Exception("A diferença entre os períodos deve ser de pelo menos 1 ano");
    } catch (Exception e) {
      Alert alerta = new Alert(Alert.AlertType.ERROR);
      alerta.setTitle("Erro!");
      alerta.setContentText(e.getMessage());
      alerta.showAndWait();
      return false;
    }

    menuPeriodoInicial.setDisable(true);
    menuPeriodoFinal.setDisable(true);
    return true;
  }
  
  protected boolean itemEstaDesabilitado(String nomeItem, MenuButton menu) {
    boolean estaDesabilitado = false;
    MenuItem itemDoMenu = menu.getItems().stream().reduce(new MenuItem(), (itemAntigo, item) -> {
      if (item.getText().equals(nomeItem))
        itemAntigo = item;
      return itemAntigo;
    });

    if (itemDoMenu.isDisable())
      estaDesabilitado = true;

    return estaDesabilitado;
  }

  protected void configurarMenu(MenuButton menu) {
    menu.getItems()
        .stream()
        .forEach(item -> item.setOnAction(new EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent arg0) {
            menu.setText(item.getText());
          }
        }));
  }
  
  protected void desabilitarItensSelecionados(MenuButton menu) {
    menu.getItems().forEach(itemMenu -> {
      if (itemMenu instanceof CustomMenuItem) {
        CustomMenuItem itemCustomizado = (CustomMenuItem) itemMenu;
        CheckBox item = (CheckBox) itemCustomizado.getContent();
        if (item.isSelected())
          item.setDisable(true);
      }
    });
  }

  protected void reiniciarMenu(MenuButton menu) {
    menu.setDisable(false);
    menu.getItems().forEach(itemMenu -> {
      if (itemMenu instanceof CustomMenuItem) {
        CustomMenuItem itemCustomizado = (CustomMenuItem) itemMenu;
        CheckBox item = (CheckBox) itemCustomizado.getContent();
        if (item.isSelected()) {
          item.setDisable(false);
          item.setSelected(false);
        }
      } else {
        itemMenu.setDisable(false);
      }
    });
  }

  protected boolean dadoExisteNoGrafico(String dado, ObservableList<Series<String, Number>> conjuntoDados) {
    boolean dadoExistente = false;
    for (Series<String, Number> serie : conjuntoDados) {
      if (serie.getName().equals(dado)) {
        dadoExistente = true;
        break;
      }
    }

    return dadoExistente;
  }
}
