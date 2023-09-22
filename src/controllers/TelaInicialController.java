package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TelaInicialController extends ControllerBase {

    @FXML
    private Button botaoEnemAntigo;

    @FXML
    private Button botaoEnemNovo;

    @FXML
    void analisarEnemAntigo(ActionEvent event) throws IOException {
        localizacaoArquivo = "../views/enem_antigo/";
        setStage(loadFXML(localizacaoArquivo, "tela_inicial"), event);
    }

    @FXML
    void analisarEnemNovo(ActionEvent event) throws IOException {
        localizacaoArquivo = "../views/enem_novo/";
        setStage(loadFXML(localizacaoArquivo, "tela_inicial"), event);
    }

}
