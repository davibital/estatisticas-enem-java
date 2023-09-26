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
  }

  @FXML
  void plotarGrafico(ActionEvent event) {
    Integer periodoInicial = null;
    Integer periodoFinal = null;
    if (!periodoValido(periodoInicial, periodoFinal))
      return;

    Set<String> categorias = new DadosEnemAntigo(1998).obterNumeroInscritosPorGenero().keySet();
    Map<String, XYChart.Series<String, Number>> seriesMap = new TreeMap<>();
    seriesMap.put("Masculino", new XYChart.Series<>());
    seriesMap.put("Feminino", new XYChart.Series<>());
    seriesMap.put("Dados não informados", new XYChart.Series<>());

    seriesMap.forEach((nomeSerie, serie) -> serie.setName(nomeSerie));

    for (int ano = periodoInicial; ano <= periodoFinal; ano++) {
      Map<String, Integer> dadosGenero;
      if (ano <= 2008) {
        dadosGenero = new DadosEnemAntigo(ano).obterNumeroInscritosPorGenero();
      } else {
        dadosGenero = new DadosEnemNovo(ano).obterNumeroInscritosPorGenero();
      }

      for (String categoriaGenero : categorias) {
        XYChart.Series<String, Number> serieAtual = seriesMap.get(categoriaGenero);
        int dado = dadosGenero.get(categoriaGenero);
        serieAtual.getData().add(new XYChart.Data<>("" + ano, dado));
      }
    }

    seriesMap.forEach((nomeSerie, serie) -> graficoGenero.getData().add(serie));
    botaoPlotar.setDisable(true);
  }

}
