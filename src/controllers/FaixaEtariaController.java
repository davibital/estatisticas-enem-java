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
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.text.Text;
import models.DadosEnemAntigo;
import models.DadosEnemNovo;

public class FaixaEtariaController extends ControllerBase implements Initializable {

  @FXML
  private ToolBar barraAnalise;

  @FXML
  private Button botaoInicio;

  @FXML
  private Button botaoLimparGrafico;

  @FXML
  private Button botaoPlotar;

  @FXML
  private LineChart graficoFaixaEtaria;

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

  @FXML
  private Text tituloTexto;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    configurarMenu(menuFaixaEtaria);
  }

  @FXML
  void voltarAoInicio(ActionEvent event) throws IOException {
    localizacaoArquivo = "../views/";
    setStage(loadFXML(localizacaoArquivo, "tela_inicial"), event);
  }
  
  @FXML
  void iniciarAnalise(ActionEvent event) {
    int anoPeriodoInicial;
    int anoPeriodoFinal;
    if (event.getSource().equals(menuItemEnemAntigo)) {
      menuModeloEnem.setText(menuItemEnemAntigo.getText());
      anoPeriodoInicial = 1998;
      anoPeriodoFinal = 2008;
    }
    else {
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
    graficoFaixaEtaria.setVisible(true);

    configurarMenu(menuPeriodoInicial);
    configurarMenu(menuPeriodoFinal);
  }

  @FXML
  void limparGrafico(ActionEvent event) {
    graficoFaixaEtaria.getData().clear();
    menuPeriodoInicial.setDisable(false);
    menuPeriodoFinal.setDisable(false);
  }

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
      return;
    }

    menuPeriodoInicial.setDisable(true);
    menuPeriodoFinal.setDisable(true);

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
