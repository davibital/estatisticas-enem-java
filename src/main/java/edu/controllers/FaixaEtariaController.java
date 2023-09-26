package edu.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import edu.models.DadosEnemAntigo;
import edu.models.DadosEnemNovo;

public class FaixaEtariaController extends ControllerBase implements Initializable {

  @FXML
  private LineChart<String, Number> graficoFaixaEtaria;

  @FXML
  private MenuButton menuFaixaEtaria;

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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    configurarMenu(menuFaixaEtaria);
  }

  @FXML
  void voltarAoInicio(ActionEvent event) throws IOException {
    voltarAoInicioBase(event);
  }

  @FXML
  void limparGrafico(ActionEvent event) {
    graficoFaixaEtaria.getData().clear();
    menuPeriodoInicial.setDisable(false);
    menuPeriodoFinal.setDisable(false);
    menuFaixaEtaria.getItems().forEach(item -> item.setDisable(false));
  }

  @FXML
  void iniciarAnalise(ActionEvent event) {
    limparGrafico(event);
    iniciarAnaliseBase(event);
  }

  @FXML
  void plotarGrafico(ActionEvent event) {
    Integer periodoInicial = null;
    Integer periodoFinal = null;
    String faixaEtaria;
    try {
      periodoInicial = Integer.parseInt(menuPeriodoInicial.getText());
      periodoFinal = Integer.parseInt(menuPeriodoFinal.getText());
      faixaEtaria = menuFaixaEtaria.getText();
      if (itemEstaDesabilitado(faixaEtaria, menuFaixaEtaria))
        throw new IOException("A faixa etária já foi selecionada!");
      else if (faixaEtaria.equals("Selecione a faixa etária"))
        throw new IOException("É preciso selecionar a faixa etária!");
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

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName(faixaEtaria);

    menuPeriodoInicial.setDisable(true);
    menuPeriodoFinal.setDisable(true);
    menuFaixaEtaria.getItems().forEach(item -> {
      if (item.getText().equals(faixaEtaria))
        item.setDisable(true);
    });

    for (int ano = periodoInicial; ano <= periodoFinal; ano++) {
      int dado = 0;
      if (ano <= 2008)
        dado = new DadosEnemAntigo(ano).obterRelacaoIdade().get(faixaEtaria);
      else
        dado = new DadosEnemNovo(ano).obterRelacaoIdade().get(faixaEtaria);

      series.getData().add(new XYChart.Data<>("" + ano, dado));
    }

    graficoFaixaEtaria.getData().add(series);
  }
}
