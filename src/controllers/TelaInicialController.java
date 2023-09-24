package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TelaInicialController extends ControllerBase {

  @FXML
  private Button botaoFaixaEtaria;

  @FXML
  private Button botaoGeneroInscritos;

  @FXML
  private Button botaoInscritos;

  @FXML
  private Button botaoInscritosUF;

  @FXML
  private Button botaoMediaProvaObjetiva;

  @FXML
  private Button botaoMediaRedacao;

  @FXML
  private Button botaoPresenca;

  @FXML
  void analisarPresenca(ActionEvent event) throws IOException {
    localizacaoArquivo = "../views/";
    setStage(loadFXML(localizacaoArquivo, "presenca"), event);
  }

  @FXML
  void analisarFaixaEtaria(ActionEvent event) throws IOException {
    localizacaoArquivo = "../views/";
    setStage(loadFXML(localizacaoArquivo, "faixa_etaria"), event);
  }

  @FXML
  void analisarGeneroInscritos(ActionEvent event) throws IOException {
    localizacaoArquivo = "../views/";
    setStage(loadFXML(localizacaoArquivo, "genero"), event);
  }

  @FXML
  void analisarInscritosUF(ActionEvent event) throws IOException {
    localizacaoArquivo = "../views/";
    setStage(loadFXML(localizacaoArquivo, "uf_inscritos"), event);
  }

  @FXML
  void analisarMediaProvaObjetiva(ActionEvent event) throws IOException {
    localizacaoArquivo = "../views/";
    setStage(loadFXML(localizacaoArquivo, "media_objetiva"), event);
  }

  @FXML
  void analisarMediaRedacao(ActionEvent event) throws IOException {
    localizacaoArquivo = "../views/";
    setStage(loadFXML(localizacaoArquivo, "media_redacao"), event);
  }

  @FXML
  void analisarTotalInscritos(ActionEvent event) throws IOException {
    localizacaoArquivo = "../views/";
    setStage(loadFXML(localizacaoArquivo, "total_inscritos"), event);
  }
}
