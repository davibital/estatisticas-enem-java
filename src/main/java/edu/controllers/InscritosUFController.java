package edu.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import edu.models.DadosEnemAntigo;
import edu.models.DadosEnemNovo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToolBar;

public class InscritosUFController extends ControllerBase implements Initializable {

  private List<String> ufSelecionadas;

  @FXML
  private ToolBar barraAnalise;

  @FXML
  private LineChart<String, Number> graficoInscritosUF;

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
  private MenuButton menuUF;

  @FXML
  void iniciarAnalise(ActionEvent event) {
    limparGrafico(event);
    iniciarAnaliseBase(event);
  }

  @FXML
  void limparGrafico(ActionEvent event) {
    graficoInscritosUF.getData().clear();
    ufSelecionadas.clear();
    reiniciarMenu(menuPeriodoInicial);
    reiniciarMenu(menuPeriodoFinal);
    reiniciarMenu(menuUF);
  }

  @FXML
  void plotarGrafico(ActionEvent event) {
    Integer periodoInicial = null;
    Integer periodoFinal = null;
    try {
      if (ufSelecionadas.isEmpty())
        throw new IOException("É preciso selecionar pelo menos 1 UF!");

      periodoInicial = Integer.parseInt(menuPeriodoInicial.getText());
      periodoFinal = Integer.parseInt(menuPeriodoFinal.getText());
    } catch (IOException e) {
      Alert alerta = new Alert(Alert.AlertType.ERROR);
      alerta.setTitle("Erro!");
      alerta.setContentText(e.getMessage());
      alerta.showAndWait();
      return;
    } catch (NumberFormatException e) {
      Alert alerta = new Alert(Alert.AlertType.ERROR);
      alerta.setTitle("Erro!");
      alerta.setContentText("É necessário informar o período a ser analisado!");
      alerta.showAndWait();
      return;
    }

    if (!periodoValido(periodoInicial, periodoFinal))
      return;

    for (String uf : ufSelecionadas) {
      if (dadoExisteNoGrafico(uf, graficoInscritosUF.getData()))
        continue;
      XYChart.Series<String, Number> series = new XYChart.Series<>();
      series.setName(uf);

      menuPeriodoInicial.setDisable(true);
      menuPeriodoFinal.setDisable(true);
      desabilitarItensSelecionados(menuUF);

      for (int ano = periodoInicial; ano <= periodoFinal; ano++) {
        int dado = 0;
        if (ano <= 2008)
          dado = new DadosEnemAntigo(ano).obterRelacaoEstados().get(uf);
        else
          dado = new DadosEnemNovo(ano).obterRelacaoEstados().get(uf);

        series.getData().add(new XYChart.Data<>("" + ano, dado));
      }

      graficoInscritosUF.getData().add(series);
    }
  }

  @FXML
  void voltarAoInicio(ActionEvent event) throws IOException {
    voltarAoInicioBase(event);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Set<String> listaUF = new DadosEnemNovo(2022).obterRelacaoEstados().keySet();
    ufSelecionadas = new ArrayList<>();

    for (String uf : listaUF) {
      CheckBox itemUF = new CheckBox(uf);
      itemUF.setOnAction(new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
          if (ufSelecionadas.contains(uf)) {
            ufSelecionadas.remove(uf);
          } else {
            ufSelecionadas.add(uf);
          }
        }
        
      });

      CustomMenuItem itemMenu = new CustomMenuItem(itemUF);
      itemMenu.setHideOnClick(false);


      menuUF.getItems().add(itemMenu);
    }
  }

}
