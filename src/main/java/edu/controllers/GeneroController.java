package edu.controllers;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.models.DadosEnemAntigo;
import edu.models.DadosEnemNovo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;

public class GeneroController extends ControllerBase {

  @FXML
  private ToolBar barraAnalise;

  @FXML
  private Button botaoInicio;

  @FXML
  private Button botaoLimparGrafico;

  @FXML
  private Button botaoPlotar;

  @FXML
  private BarChart<String, Number> graficoGenero;

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
  void voltarAoInicio(ActionEvent event) throws IOException {
    voltarAoInicioBase(event);
  }

  @FXML
  void iniciarAnalise(ActionEvent event) {
    limparGrafico(event);
    iniciarAnaliseBase(event);
  }

  @FXML
  void limparGrafico(ActionEvent event) {
    graficoGenero.getData().clear();
    menuPeriodoInicial.setDisable(false);
    menuPeriodoFinal.setDisable(false);
    botaoPlotar.setDisable(false);
  }

  @FXML
  void plotarGrafico(ActionEvent event) {
    Integer periodoInicial = null;
    Integer periodoFinal = null;
    try {
      periodoInicial = Integer.parseInt(menuPeriodoInicial.getText());
      periodoFinal = Integer.parseInt(menuPeriodoFinal.getText());
    } catch (NumberFormatException e) {
      Alert alerta = new Alert(Alert.AlertType.ERROR);
      alerta.setTitle("Erro!");
      alerta.setContentText("É necessário informar o período a ser analisado!");
      alerta.showAndWait();
      return;
    }

    if (!periodoValido(periodoInicial, periodoFinal))
      return;

    Set<String> generos = new DadosEnemAntigo(1998).obterNumeroInscritosPorGenero().keySet();
    
    for (String genero : generos) {
      XYChart.Series<String, Number> serie = new XYChart.Series<>();
      serie.setName(genero);

      for (int ano = periodoInicial; ano <= periodoFinal; ano++) {
        int dado = 0;
        if (ano <= 2008) {
          dado = new DadosEnemAntigo(ano).obterNumeroInscritosPorGenero().get(genero);
        } else {
          dado = new DadosEnemNovo(ano).obterNumeroInscritosPorGenero().get(genero);
        }

        serie.getData().add(new XYChart.Data<>("" + ano, dado));
      }

      graficoGenero.getData().add(serie);
    }
    
    botaoPlotar.setDisable(true);
  }

}
