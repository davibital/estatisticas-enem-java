package edu.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class MediaController extends ControllerBase {

  @FXML
  private ToolBar barraAnalise;

  @FXML
  private Button botaoInicio;

  @FXML
  private Button botaoLimparGrafico;

  @FXML
  private Button botaoPlotar;

  @FXML
  private BarChart<String, Number> graficoMedia;

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

  @FXML
  private Text tituloTexto;

  @FXML
  void iniciarAnalise(ActionEvent event) {
    limparGrafico(event);
    iniciarAnaliseBase(event);
  }

  @FXML
  void limparGrafico(ActionEvent event) {
    graficoMedia.getData().clear();
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

    if (menuModeloEnem.getText().equals("Modelo Novo")) {
      for (String area : new DadosEnemNovo(2022).obterAreasConhecimento()) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(area);

        for (int ano = periodoInicial; ano <= periodoFinal; ano++) {
          double dado = 0;
          DadosEnemNovo enemAtual = new DadosEnemNovo(ano);
          switch (area) {
            case "Ciencias da Natureza e suas Tecnologias":
              dado = enemAtual.obterMediaProvaCN();
              break;
            case "Ciencias Humanas e suas Tecnologias":
              dado = enemAtual.obterMediaProvaCH();
              break;
            case "Linguagens, Códigos e suas Tecnologias":
              dado = enemAtual.obterMediaProvaLC();
              break;
            case "Matemática e suas Tecnologias":
              dado = enemAtual.obterMediaProvaMT();
              break;
            default:
              break;
          }

          series.getData().add(new XYChart.Data<>("" + ano, dado));
        }

        graficoMedia.getData().add(series);
      }
    } else {
      XYChart.Series<String, Number> series = new XYChart.Series<>();
      series.setName("Média das provas objetivas");
      for (int ano = periodoInicial; ano <= periodoFinal; ano++) {
        double dado = new DadosEnemAntigo(ano).obterMediaNotasProvaObjetiva();
        series.getData().add(new XYChart.Data<>("" + ano, dado));
      }

      graficoMedia.getData().add(series);
    }
  }

  @FXML
  void voltarAoInicio(ActionEvent event) throws IOException {
    voltarAoInicioBase(event);
  }

}
