package edu.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArquivoCSV {
  private String caminhoArquivo;
  private String arquivo;
  private long quantidadeLinhasValidas;
  private List<String> nomeColunasArquivo;

  public ArquivoCSV(String caminhoArquivo) {
    this.caminhoArquivo = caminhoArquivo;
    nomeColunasArquivo = new ArrayList<>();
  }

  public void carregarArquivo(String nomeArquivo) {
    BufferedReader varredorArquivo = null;
    arquivo = caminhoArquivo + nomeArquivo + ".csv";

    try {
      varredorArquivo = new BufferedReader(new FileReader(arquivo));
      String[] primeiraLinha = varredorArquivo.readLine().split(";");

      quantidadeLinhasValidas = varredorArquivo.lines().count();

      nomeColunasArquivo = Arrays.asList(primeiraLinha);
      varredorArquivo.close();
    } catch (IOException e) {
      System.err.println("O arquivo não foi encontrado: " + arquivo);
      System.err.println("Informe o nome do arquivo corretamente!");
    }
  }

  public long obterQuantidadeLinhasValidas() {
    return quantidadeLinhasValidas;
  }
  
  public List<String> obterColuna(String nomeColuna) {
    BufferedReader varredorArquivo = null;
    List<String> coluna = new ArrayList<>();
    int indiceColuna = -1;

    try {
      varredorArquivo = new BufferedReader(new FileReader(arquivo));

      for (int i = 0; i < nomeColunasArquivo.size(); i++) {
        if (nomeColunasArquivo.get(i).equals(nomeColuna))
          indiceColuna = i;
      }

      if (indiceColuna == -1)
        throw new IllegalArgumentException("Coluna não encontrada: " + nomeColuna);

      // Eliminar primeira linha do arquivo que consiste na linha de nomes das colunas
      varredorArquivo.readLine();

      while (varredorArquivo.ready()) {
        List<String> linha = Arrays.asList(varredorArquivo.readLine().split(";"));
        String elementoColuna = "";
        try {
          elementoColuna = linha.get(indiceColuna);
        } catch (Exception e) {
          System.err.println("Elemento coluna vazio: " + elementoColuna);
        }
        coluna.add(elementoColuna);
      }

      varredorArquivo.close();
    } catch (IOException e) {
      System.err.println("O arquivo não foi encontrado: " + arquivo);
      System.err.println("Carregue o arquivo correto novamente!");
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      System.err.println("Informe uma coluna que está presente no arquivo.");
      System.err.println("Colunas do arquivo: " + this.nomeColunasArquivo);
    }

    return coluna;
  }
}
