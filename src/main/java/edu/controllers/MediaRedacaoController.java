package edu.controllers;

import java.io.IOException;

import edu.models.DadosEnemAntigo;
import edu.models.DadosEnemNovo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.text.Text;

public class MediaRedacaoController extends ControllerBase {

  @FXML
  private ToolBar barraAnalise;

  @FXML
  private Button botaoInicio;

  @FXML
  private Button botaoLimparGrafico;

  @FXML
  private Button botaoPlotar;

  @FXML
  private LineChart<String, Number> graficoRedacao;

  @FXML
  private MenuItem menuItemEnemAntigo;

  @FXML
  private MenuItem menuItemEnemNovo;

  @FXML
  private MenuButton menuModeloEnem;

  @FXML
  private MenuButton menuNota;

  @FXML
  private MenuButton menuPeriodoFinal;

  @FXML
  private MenuButton menuPeriodoInicial;

  @FXML
  private Text tituloTexto;

  @FXML
  void iniciarAnalise(ActionEvent event) {
    limparGrafico(event);
    iniciarAnaliseBase(event);
  }

  @FXML
  void limparGrafico(ActionEvent event) {
    graficoRedacao.getData().clear();
    reiniciarMenu(menuPeriodoInicial);
    reiniciarMenu(menuPeriodoFinal);
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

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Média da redação");

    for (int ano = periodoInicial; ano <= periodoFinal; ano++) {
      double dado = 0;
      if (ano <= 2008)
        dado = new DadosEnemAntigo(ano).obterMediaNotasRedacao();
      else
        dado = new DadosEnemNovo(ano).obterMediaNotasRedacao();

      series.getData().add(new XYChart.Data<>("" + ano, dado));
    }
    
    graficoRedacao.getData().add(series);
  }

  @FXML
  void voltarAoInicio(ActionEvent event) throws IOException {
    voltarAoInicioBase(event);
  }

}
