package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import models.DadosEnemAntigo;
import models.DadosEnemNovo;

public class FaixaEtariaController extends ControllerBase implements Initializable {

  @FXML
  private Button botaoInicio;

  @FXML
  private Button botaoPlotar;

  @FXML
  private Button botaoLimparGrafico;

  @FXML
  private LineChart graficoFaixaEtaria;

  @FXML
  private MenuButton menuFaixaEtaria;

  @FXML
  private MenuButton menuPeriodoFinal;

  @FXML
  private MenuButton menuPeriodoInicial;

  @FXML
  void plotarGrafico(ActionEvent event) {
    Integer periodoInicial;
    Integer periodoFinal;
    String faixaEtaria;
    try {
      periodoInicial = Integer.parseInt(menuPeriodoInicial.getText());
      periodoFinal = Integer.parseInt(menuPeriodoFinal.getText());
      faixaEtaria = menuFaixaEtaria.getText();
      if (faixaEtaria.equals("Selecione a faixa etária"))
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

    XYChart.Series series = new XYChart.Series();
    series.setName(faixaEtaria);

    if (periodoInicial > periodoFinal) {
      Alert alerta = new Alert(Alert.AlertType.ERROR);
      alerta.setTitle("Erro!");
      alerta.setContentText("O período inicial não pode ser maior que o período final");
      alerta.showAndWait();
      return;
    }

    for (int ano = periodoInicial; ano <= periodoFinal; ano++) {
      int dado = 0;
      if (ano <= 2008)
        dado = new DadosEnemAntigo(ano).obterRelacaoIdade().get(faixaEtaria);
      else
        dado = new DadosEnemNovo(ano).obterRelacaoIdade().get(faixaEtaria);
        
      series.getData().add(new XYChart.Data("" + ano, dado));
    }

    graficoFaixaEtaria.getData().add(series);
  }

  @FXML
  void limparGrafico(ActionEvent event) {
    graficoFaixaEtaria.getData().clear();
  }

  @FXML
    void voltarAoInicio(ActionEvent event) throws IOException {
      localizacaoArquivo = "../views/";
      setStage(loadFXML(localizacaoArquivo, "tela_inicial"), event);
    }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    configurarMenu(menuFaixaEtaria);
    configurarMenu(menuPeriodoInicial);
    configurarMenu(menuPeriodoFinal);
  }

  private void configurarMenu(MenuButton menu) {
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
